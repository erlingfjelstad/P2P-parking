package eu.vincinity2020.p2p_parking.data.entities

import com.google.gson.annotations.SerializedName

open class GenericResponse (
        @SerializedName("error") val isError:Boolean? = null,
        @SerializedName("message") val message:String? = null
)