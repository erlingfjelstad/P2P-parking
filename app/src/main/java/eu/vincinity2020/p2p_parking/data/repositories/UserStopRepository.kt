package eu.vincinity2020.p2p_parking.data.repositories

import eu.vincinity2020.p2p_parking.app.P2PDatabase
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

object UserStopRepository {

    fun insertUserStop(userStop: UserStop) {
        doAsync {
            P2PDatabase.db.userStopDao().insert(userStop)
        }
    }

    fun insertUserStop(userStops: ArrayList<UserStop>) {
        doAsync {
            P2PDatabase.db.userStopDao().deleteAll()
            userStops.forEach {
                P2PDatabase.db.userStopDao().insert(it)
            }
        }
    }

    fun updateUserStop(userStop: UserStop){
        doAsync {
            P2PDatabase.db.userStopDao().updateUser(userStop)
        }
    }

    fun getAllUserStops(): Observable<ArrayList<UserStop>> =
            P2PDatabase.db.userStopDao().getAllUserStops()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { ArrayList(it) }

    fun getBlocking(onFound:(stops: List<UserStop>)->Unit){
        doAsync {
            onFound(P2PDatabase.db.userStopDao().getAllUserStopsBlocking())

        }
    }

    fun deleteAll(){
        doAsync {
            P2PDatabase.db.userStopDao().deleteAll()
        }
    }
}