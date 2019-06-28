package eu.vincinity2020.p2p_parking.app.common

class AppConstants {
    companion object {
        const val SHARED_PREFS = "sharedPrefs"
        const val USER_LOGGED_IN = "userLoggedIn"
        const val FCM_TOKEN = "fcmToken"
        const val USER_EMAIL = "userEmail"
        const val USER = "loggedUser"
        const val IsLoggedOut = "loggedOut"

        const val SEARCHACTIVITYCODE= 1001

        const val NAV_HOME = "Home"
        const val NAV_PRIVACY = "Privacy"
        const val NAV_GET_STARTED = "Get Started"
        const val NAV_ABOUT_US = "About Us"

        val registerNavigationItems = arrayListOf(
                NAV_HOME,
                NAV_PRIVACY,
                NAV_GET_STARTED,
                NAV_ABOUT_US
        )

        val homeNavigationItems = arrayListOf(
                NAV_HOME
        )
    }

    enum class Arguments{
        ARGS_USER_EMAIL
    }
}
