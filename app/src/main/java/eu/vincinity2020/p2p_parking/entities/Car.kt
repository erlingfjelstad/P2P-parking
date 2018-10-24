package eu.vincinity2020.p2p_parking.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index(value = ["licencePlate"], name = "licencePlate", unique = true)])
data class Car(val licencePlate: String, val isDefault: Boolean) {
    @PrimaryKey(autoGenerate = true)
    var carId: Long = 0
}