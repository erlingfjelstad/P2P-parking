package eu.vincinity2020.p2p_parking.data.dao

import androidx.room.*
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import io.reactivex.Observable

@Dao
interface UserStopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userStop: UserStop)

    @Update
    fun updateUser(userStop: UserStop)

    @Query("DELETE FROM UserStop")
    fun deleteAll()

    @Query("SELECT * FROM UserStop")
    fun getAllUserStops(): Observable<List<UserStop>>

    @Query("SELECT * FROM UserStop")
    fun getAllUserStopsBlocking(): List<UserStop>
}