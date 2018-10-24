package eu.vincinity2020.p2p_parking.trip

import eu.vincinity2020.p2p_parking.common.View
import eu.vincinity2020.p2p_parking.repositories.TripRepository
import io.reactivex.disposables.CompositeDisposable

class TripPresenterImpl(private val tripRepository: TripRepository) : TripPresenter {
    private var tripView: TripView? = null
    private val disposable = CompositeDisposable()

    override fun attach(view: View) {
        tripView = view as TripView
    }

    override fun detach() {
        disposable.clear()
        tripView = null
    }

    override fun getRecentTrips() {
        disposable.add(
                tripRepository.getTrips()
                        .subscribe())
    }
}