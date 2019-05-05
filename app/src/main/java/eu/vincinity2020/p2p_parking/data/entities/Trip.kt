package eu.vincinity2020.p2p_parking.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
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