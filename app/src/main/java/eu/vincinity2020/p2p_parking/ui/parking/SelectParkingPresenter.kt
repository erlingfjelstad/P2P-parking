package eu.vincinity2020.p2p_parking.ui.parking

import com.google.maps.model.LatLng
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SelectParkingPresenter(private val networkService: NetworkService) : SelectParkingContract.Presenter {
    private val allDisposables = CompositeDisposable()

    override fun getParkingSpots(view: SelectParkingContract.View, location: LatLng) {
        allDisposables.add(
                networkService.getParkingSpots(location.lat, location.lng)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            view.handleGetParkingSuccess(it.parkingSpots)
                        }, {
                            view.handleGetParkingFailure(it)
                        })
        )
    }

    override fun detach() {
        allDisposables.clear()
    }

}
