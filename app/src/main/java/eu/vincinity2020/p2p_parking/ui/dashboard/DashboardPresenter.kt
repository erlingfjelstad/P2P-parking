package eu.vincinity2020.p2p_parking.ui.dashboard

import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import io.reactivex.disposables.CompositeDisposable

class DashboardPresenter:DashboardContract.Presenter {

    val allDisposables = CompositeDisposable()

    override fun saveStopsInDb(stops: ArrayList<UserStop>) {

    }

    override fun detach() {
    }

}