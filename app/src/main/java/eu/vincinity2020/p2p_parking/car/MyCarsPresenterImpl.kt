package eu.vincinity2020.p2p_parking.car

import eu.vincinity2020.p2p_parking.common.View
import eu.vincinity2020.p2p_parking.entities.Car
import eu.vincinity2020.p2p_parking.repositories.CarRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MyCarsPresenterImpl(private val carRepository: CarRepository) : MyCarsPresenter {

    private val disposable = CompositeDisposable()
    private var myCarView: MyCarsView? = null

    override fun attach(view: View) {
        myCarView = view as? MyCarsView

        getCars()
    }

    private fun getCars() {
        disposable.add(carRepository.allCarsFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { myCarView?.onCarsLoaded(it) }
        )

    }

    override fun detach() {
        disposable.clear()
        myCarView = null
    }

    /**
     * We register a car as default if we don't have any cars in our db. Else not default
     */
    override fun registerCar(registrationNumber: String) {
        disposable.add(Single.fromCallable {
            carRepository.allCars()
        }
                .map { cars ->
                    if (cars.isEmpty()) {
                        return@map carRepository.insert(Car(registrationNumber, true))
                    } else {
                        return@map carRepository.insert(Car(registrationNumber, false))
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _: Long ->
                    myCarView?.onCarAdded()
                })
    }

    override fun setDefault(car: Car) {
        Single.fromCallable {
            carRepository.updateClearDefault()
        }
                .subscribeOn(Schedulers.io())
                .map { _ ->
                    return@map carRepository.setDefault(car)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    myCarView?.onCarDefaulted()

                }
    }
}