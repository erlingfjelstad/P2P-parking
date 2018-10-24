package eu.vincinity2020.p2p_parking.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(
        entity = Car::class,
        parentColumns = arrayOf("carId"),
        childColumns = arrayOf("carId"))],
        indices = [Index(value = ["carId"], name = "carId")])
data class Trip(val carId: Long, val fromDate: Date, val toDate: Date,
                val parkingSpotName: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}