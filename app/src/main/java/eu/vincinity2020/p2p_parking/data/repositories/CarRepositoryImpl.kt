package eu.vincinity2020.p2p_parking.data.repositories

import eu.vincinity2020.p2p_parking.data.dao.CarDao
import eu.vincinity2020.p2p_parking.data.entities.Car
import io.reactivex.Flowable
import io.reactivex.Single

class CarRepositoryImpl(private val carDao: CarDao) : CarRepository {
    override fun defaultCar(): Single<Car> {
        return carDao.defaultCar()
    }

    override fun setDefault(car: Car): Int {
        return carDao.setDefault(car.carId)
    }

    override fun updateClearDefault(): Int {
        return carDao.updateClearDefault()
    }

    override fun allCarsFlowable(): Flowable<List<Car>> {
        return carDao.allCarsFlowable()
    }

    override fun insert(car: Car): Long {
        return carDao.insert(car)
    }

    override fun delete(car: Car): Int {
        return carDao.delete(car)
    }

    override fun allCars(): List<Car> {
        return carDao.allCars()
    }

}