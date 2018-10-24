package eu.vincinity2020.p2p_parking.book

import eu.vincinity2020.p2p_parking.common.View
import eu.vincinity2020.p2p_parking.entities.Trip
import eu.vincinity2020.p2p_parking.repositories.CarRepository
import eu.vincinity2020.p2p_parking.repositories.TripRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class BookParkingSpotPresenterImpl(
        private val tripRepository: TripRepository,
        private val carRepository: CarRepository) : BookParkingSpotPresenter {

    private var bookParkingSpoView: BookParkingSpotView? = null
    private val disposable = CompositeDisposable()

    override fun attach(view: View) {
        bookParkingSpoView = view as BookParkingSpotView
    }

    override fun detach() {
        bookParkingSpoView = null
        disposable.clear()
    }

    override fun bookParkingSpot(fromDate: Date, toDate: Date, parkingSpotName: String) {
        disposable.add(carRepository.defaultCar()
                .subscribeOn(Schedulers.io())
                .map { car -> return@map car.carId }
                .map { carId -> return@map Trip(carId, fromDate, toDate, parkingSpotName) }
                .map { trip -> return@map tripRepository.insert(trip) }
                .filter { id -> id >= 0 }
                .subscribe {
                    bookParkingSpoView?.onTripSuccessfullyBooked()
                })
    }
}