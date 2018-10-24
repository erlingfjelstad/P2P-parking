package eu.vincinity2020.p2p_parking.repositories

import eu.vincinity2020.p2p_parking.dao.TripDao
import eu.vincinity2020.p2p_parking.entities.Trip
import io.reactivex.Flowable

class TripRepositoryImpl(private val tripDao: TripDao) : TripRepository {

    override fun insert(trip: Trip): Long {
        return tripDao.insert(trip)
    }

    override fun update(trip: Trip): Int {
        return tripDao.update(trip)
    }

    override fun delete(trip: Trip): Int {
        return tripDao.delete(trip)
    }

    override fun getTrips(): Flowable<List<Trip>> {
        return tripDao.getTrips()
    }

}