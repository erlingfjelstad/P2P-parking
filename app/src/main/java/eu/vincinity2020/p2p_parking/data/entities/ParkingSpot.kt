package eu.vincinity2020.p2p_parking.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
class ParkingSpot(val sensorId: Long, val status: String, val oid: String,
                  val lat: Double, val lon: Double) : Parcelable