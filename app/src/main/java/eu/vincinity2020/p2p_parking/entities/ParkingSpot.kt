package eu.vincinity2020.p2p_parking.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
class ParkingSpot(@PrimaryKey val id: Long, val name: String,
                  val latitude: Double, val longitude: Double) : Parcelable