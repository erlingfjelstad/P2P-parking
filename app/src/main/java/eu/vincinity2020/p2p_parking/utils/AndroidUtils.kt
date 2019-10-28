package eu.vincinity2020.p2p_parking.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.data.entities.eventbus.FirstResponderAlert
import eu.vincinity2020.p2p_parking.ui.auth.login.LoginActivity
import org.jetbrains.anko.startActivity


class AndroidUtils {

    companion object {
        fun attachFragment(fragmentManager: FragmentManager,
                           fragment: Fragment,
                           viewId: Int,
                           tag: String,
                           addToStack: Boolean) {
            var transaction = fragmentManager.beginTransaction().replace(viewId, fragment, tag);
            if (addToStack)
                transaction.addToBackStack(tag)
            transaction.commit()
        }
    }


}

fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
    var drawable = ContextCompat.getDrawable(context, drawableId)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        drawable = DrawableCompat.wrap(drawable!!).mutate()
    }

    val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}

fun saveApiToken(token: String) {
    P2PPreferences(App.applicationContext()).saveString(AppConstants.API_TOKEN, token)
}

fun getApiToken(): String {
    val token = P2PPreferences(App.applicationContext()).getString(AppConstants.API_TOKEN)
    if (token != null) {
        return token
    } else {
        throw IllegalStateException("Token not found")
    }
}

fun isLoggedIn(): Boolean {
    return P2PPreferences(App.applicationContext()).getString(AppConstants.API_TOKEN) != null
}

fun logUserOut(activity: Activity) {
    P2PPreferences(App.applicationContext()).removeKey(AppConstants.API_TOKEN)
    activity.finishAffinity()
    activity.startActivity<LoginActivity>()
}

fun saveFirstResponderData(firstResponderAlert: FirstResponderAlert) {
    P2PPreferences(App.applicationContext()).saveString(AppConstants.PREF_FR_ALERT, Gson().toJson(firstResponderAlert))
}

fun getFirstResponderData(): FirstResponderAlert? {
    val alertJson = P2PPreferences(App.applicationContext()).getString(AppConstants.PREF_FR_ALERT)
    return Gson().fromJson(alertJson, FirstResponderAlert::class.java)
}

fun deleteFirstResponderAlert() {
    P2PPreferences(App.applicationContext()).removeKey(AppConstants.PREF_FR_ALERT)

}