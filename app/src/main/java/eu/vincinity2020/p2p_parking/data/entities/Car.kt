package eu.vincinity2020.p2p_parking.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(indices = [Index(value = ["licencePlate"], name = "licencePlate", unique = true)])
data class Car(val licencePlate: String, val isDefault: Boolean) {
    @PrimaryKey(autoGenerate = true)
    var carId: Long = 0
}