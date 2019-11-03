package eu.vincinity2020.p2p_parking.ui.dashboard

import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop

class DashboardContract {

    interface View{

    }

    interface Presenter{

        fun saveStopsInDb(stops: ArrayList<UserStop>)

        fun detach()
    }
}