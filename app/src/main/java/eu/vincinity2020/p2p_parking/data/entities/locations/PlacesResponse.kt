package eu.vincinity2020.p2p_parking.data.entities.locations

import com.google.gson.annotations.SerializedName
import eu.vincinity2020.p2p_parking.data.entities.GenericResponse

data class PlacesResponse(
        @SerializedName("data") val places:ArrayList<PlaceRequest>
):GenericResponse()