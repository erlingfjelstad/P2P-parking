package eu.vincinity2020.p2p_parking.data.repositories

import eu.vincinity2020.p2p_parking.data.entities.Trip
import io.reactivex.Flowable

interface TripRepository {
    fun insert(trip: Trip): Long

    fun update(trip: Trip): Int

    fun delete(trip: Trip): Int

    fun getTrips(): Flowable<List<Trip>>
}