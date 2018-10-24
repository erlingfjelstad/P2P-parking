package eu.vincinity2020.p2p_parking.dao

import android.arch.persistence.room.*
import eu.vincinity2020.p2p_parking.entities.Car
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CarDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(car: Car): Long

    @Delete
    fun delete(car: Car): Int

    @Query(value = "SELECT * FROM Car")
    fun allCars(): List<Car>

    @Query(value = "SELECT * FROM Car")
    fun allCarsFlowable(): Flowable<List<Car>>

    @Query(value = "UPDATE Car SET isDefault=0 WHERE isDefault=1")
    fun updateClearDefault(): Int

    @Query(value = "UPDATE Car SET isDefault=1 WHERE carId=:id")
    fun setDefault(id: Long): Int

    @Query(value = "SELECT * FROM Car WHERE isDefault=1")
    fun defaultCar(): Single<Car>
}