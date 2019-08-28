package eu.vincinity2020.p2p_parking.app.common

import com.google.maps.model.LatLng
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop

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
        const val NAV_VEHICLE_LIST = "Vehicles"

        val registerNavigationItems = arrayListOf(
                NAV_HOME,
                NAV_PRIVACY,
                NAV_GET_STARTED,
                NAV_ABOUT_US
        )

        val dashboardNavigationItems = arrayListOf(
                Pair(NAV_HOME, R.drawable.ic_nav_home),
                Pair(NAV_EDIT_PROFILE, R.drawable.ic_profile_outline),
                Pair(NAV_VEHICLE_LIST, R.drawable.ic_car),
                Pair("Show alert", R.drawable.ic_warning),
                Pair("Routing sample", R.drawable.ic_map_white),
                Pair("Timer", R.drawable.ic_timer)
        )

        const val NAV_FRAGMENT_ANIMATION_DURATION = 400L
    }

    enum class Arguments {
        ARGS_USER_EMAIL
    }
}

val routingDummyData = arrayListOf<UserStop>(
        UserStop("Strømsbuveien 14\n" +
                "4836 Arendal",
                LatLng(58.460166, 8.7633366)),
        UserStop("Nedre Tyholmsvei 6\n" +
                "4836 Arendal",
                LatLng(58.459383, 8.7673525)),
        UserStop("Batteribakken 2B\n" +
                "4841 Arendal",
                LatLng(58.4593636, 8.7717757)),
        UserStop("Kastellveien 8\n" +
                "4841 Arendal",
                LatLng(58.4606162, 8.7690754)),
        UserStop("Rådhusgaten 12\n" +
                "4836 Arendal",
                LatLng(58.457384, 8.7648293)),
        UserStop("Nygaten 6\n" +
                "4838 Arendal",
                LatLng(58.4620215, 8.7642115)),
        UserStop("Arkenveien 14\n" +
                "4836 Arendal",
                LatLng(58.4585592, 8.7637768)),
        UserStop("Kastellveien 8-30\n" +
                "4841 Arendal",
                LatLng(58.4606162, 8.7690754)),
        UserStop("Batteriveien 1\n" +
                "4841 Arendal",
                LatLng(58.4595669, 8.7696827)),
        UserStop("Strømsbuveien 8\n" +
                "4836 Arendall",
                LatLng(58.4606865, 8.7632991)),
        UserStop("Batteriveien 11\n" +
                "4841 Arendal",
                LatLng(58.4589943, 8.7708858)),
        UserStop("Gamle Kittelsbuktvei 18-28\n" +
                "4836 Arendal",
                LatLng(58.4590679, 8.7620763)),
        UserStop("Neset 4\n" +
                "4841 Arendal",
                LatLng(58.459609, 8.770375)),
        UserStop("Feierheia 21-1\n" +
                "4841 Arendal",
                LatLng(58.4611561, 8.7693627)),
        UserStop("Skytebanen 11\n" +
                "4841 Arendal",
                LatLng(58.461095, 8.7707518))
        )
