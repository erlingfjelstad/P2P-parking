package eu.vincinity2020.p2p_parking.data.entities.parking

import com.google.gson.annotations.SerializedName

data class ParkingSpot(
        @SerializedName("oid") val oid: String,
        @SerializedName("sensorId") val sensorId: String,
        @SerializedName("network") val network: String,
        @SerializedName("parkingSpaceId") val parkingSpaceId: String,
        @SerializedName("parkingLotName") val parkingLotName: String,
        @SerializedName("status") val status: String,
        @SerializedName("lat") val lat: Double,
        @SerializedName("lon") val lon: Double,
        @SerializedName("carPresence") val carPresence: Int
)