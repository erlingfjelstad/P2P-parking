package eu.vincinity2020.p2p_parking.app.common

class AppConstants {
    companion object {
        const val SHARED_PREFS = "sharedPrefs"
        const val USER_LOGGED_IN = "userLoggedIn"
        const val FCM_TOKEN = "fcmToken"
        const val USER_EMAIL = "userEmail"

    }

    enum class Arguments{
        ARGS_USER_EMAIL
    }
}
