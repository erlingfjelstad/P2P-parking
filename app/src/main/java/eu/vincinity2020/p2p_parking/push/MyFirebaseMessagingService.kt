package eu.vincinity2020.p2p_parking.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.data.entities.eventbus.FirstResponderAlert
import eu.vincinity2020.p2p_parking.ui.dashboard.DashboardActivity
import eu.vincinity2020.p2p_parking.utils.NotificationUtils
import eu.vincinity2020.p2p_parking.utils.isLoggedIn
import eu.vincinity2020.p2p_parking.utils.saveFirstResponderData
import java.util.*

/**
 * Created by Ankush on 15/11/18.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage?.from}")
        // Check if message contains a data payload.
        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            // Handle message within 10 seconds
            sendNotification(remoteMessage.data["title"], remoteMessage.data["message"], NotificationUtils.getFirstResponderAlertFromMessage(remoteMessage.data["message"]))

        }
        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        if (remoteMessage?.data?.get("message")?.contains("location") == true) {
            NotificationUtils.onFirstResponderNotificationReceived(remoteMessage.data["message"])
        }
    }
    // [END receive_message]
    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(AppConstants.FCM_TOKEN, token)
                .apply()
    }
    // [END on_new_token]

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageTitle: String?, messageBody: String?, firstResponderAlert: FirstResponderAlert?) {
        if(messageBody?.contains("route_updated") == true){
            NotificationUtils.onRouteUpdateAlertReceived(messageBody)
            return
        }
        var pendingIntent: PendingIntent? = null
        if (isLoggedIn() && firstResponderAlert != null) {
            saveFirstResponderData(firstResponderAlert)
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            pendingIntent = PendingIntent.getActivity(this, Random().nextInt(10000 + 1) /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT)
        }
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo_p2p)
                .setContentTitle(getString(R.string.fcm_message))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "P2P Parking",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}