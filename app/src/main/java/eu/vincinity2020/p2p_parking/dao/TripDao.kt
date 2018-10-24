package eu.vincinity2020.p2p_parking.dao

import android.arch.persistence.room.*
import eu.vincinity2020.p2p_parking.entities.Trip
import io.reactivex.Flowable

@Dao
interface TripDao {

    @Insert
    fun insert(trip: Trip): Long

    @Update
    fun update(trip: Trip): Int

    @Delete
    fun delete(trip: Trip): Int

    @Query("SELECT * FROM Trip")
    fun getTrips(): Flowable<List<Trip>>


}