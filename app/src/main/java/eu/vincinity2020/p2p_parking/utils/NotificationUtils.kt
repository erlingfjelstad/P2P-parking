package eu.vincinity2020.p2p_parking.utils

import eu.vincinity2020.p2p_parking.data.entities.eventbus.FirstResponderAlert
import org.greenrobot.eventbus.EventBus

object NotificationUtils {

    fun onFirstResponderNotificationReceived(message: String?) {
        if (message != null) {
            val latLong = message.substringAfterLast("location").dropLast(1)
            val lat = latLong.substringBefore("-").toDoubleOrNull()
            val long = latLong.substringAfter("-").toDoubleOrNull()
            if (lat != null && long != null) {
                EventBus.getDefault().postSticky(FirstResponderAlert(message.toWords()[1], lat, long))
            }

        }
    }

    fun getFirstResponderAlertFromMessage(message: String?): FirstResponderAlert? {
        return if (message != null) {
            val latLong = message.substringAfterLast("location").dropLast(1)
            val lat = latLong.substringBefore("-").toDoubleOrNull()
            val long = latLong.substringAfter("-").toDoubleOrNull()
            return if (lat != null && long != null) {
                FirstResponderAlert(message.toWords()[1], lat, long)
            } else {
                null
            }
        } else {
            null
        }
    }
}