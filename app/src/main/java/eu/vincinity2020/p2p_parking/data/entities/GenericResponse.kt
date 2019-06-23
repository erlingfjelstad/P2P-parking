package eu.vincinity2020.p2p_parking.data.entities

import com.google.gson.annotations.SerializedName

data class GenericResponse (
        @SerializedName("error") val isError:Boolean,
        @SerializedName("message") val message:String
)