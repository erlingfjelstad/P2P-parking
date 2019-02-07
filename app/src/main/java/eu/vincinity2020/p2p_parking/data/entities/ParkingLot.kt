package eu.vincinity2020.p2p_parking.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by Ankush on 13/12/18.
 */
@Parcelize
data class ParkingLot( val createdAtDate: String, val lastModifiedDate: String, val oid: String, val lotId: String,
                       val lotName: String, val description:String, val streetAddress: String, val cameraId:String,
                       val latitude: Double, val longitude: Double, val owner: String) : Parcelable