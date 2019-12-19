package eu.vincinity2020.p2p_parking.data.entities.parking

import com.google.gson.annotations.SerializedName
import eu.vincinity2020.p2p_parking.data.entities.GenericResponse

data class ParkingSpotResponse(
        @SerializedName("data") val parkingSpots: ArrayList<ParkingSpot>
): GenericResponse()