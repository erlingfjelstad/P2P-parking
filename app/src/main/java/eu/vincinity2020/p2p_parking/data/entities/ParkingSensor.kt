package eu.vincinity2020.p2p_parking.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
class ParkingSensor(val sensorId: Long, val status: String, val oid: String, val network: String,
                    val createdAtDate: String, val lastModifiedDate: String, val parkingLotName: String,
                    val carPresence: Int, val lot: ParkingLot,
                    val lat: Double, val lon: Double) : Parcelable