package eu.vincinity2020.p2p_parking.app.common

import eu.vincinity2020.p2p_parking.R

class AppConstants {
    companion object {
        const val SHARED_PREFS = "sharedPrefs"
        const val USER_LOGGED_IN = "userLoggedIn"
        const val FCM_TOKEN = "fcmToken"
        const val USER_EMAIL = "userEmail"
        const val USER = "loggedUser"
        const val IsLoggedOut = "loggedOut"

        const val SEARCHACTIVITYCODE = 1001

        const val NAV_HOME = "Home"
        const val NAV_PRIVACY = "Privacy"
        const val NAV_GET_STARTED = "Get Started"
        const val NAV_ABOUT_US = "About Us"
        const val NAV_EDIT_PROFILE = "Edit Profile"

        val registerNavigationItems = arrayListOf(
                NAV_HOME,
                NAV_PRIVACY,
                NAV_GET_STARTED,
                NAV_ABOUT_US
        )

        val dashboardNavigationItems = arrayListOf(
                Pair(NAV_HOME, R.drawable.ic_nav_home),
                Pair(NAV_EDIT_PROFILE, R.drawable.ic_profile)
        )

        const val NAV_FRAGMENT_ANIMATION_DURATION = 400L
    }

    enum class Arguments {
        ARGS_USER_EMAIL
    }
}
