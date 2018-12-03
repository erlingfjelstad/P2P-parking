package eu.vincinity2020.p2p_parking.data.repositories

import eu.vincinity2020.p2p_parking.data.entities.Car
import io.reactivex.Flowable
import io.reactivex.Single

interface CarRepository {
    fun insert(car: Car): Long
    fun delete(car: Car): Int
    fun allCars(): List<Car>
    fun allCarsFlowable(): Flowable<List<Car>>
    fun updateClearDefault(): Int
    fun setDefault(car: Car) : Int
    fun defaultCar(): Single<Car>

}
