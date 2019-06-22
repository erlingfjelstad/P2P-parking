package eu.vincinity2020.p2p_parking.data.entities

import com.google.gson.annotations.SerializedName

data class SaveFcmTokenRequest(
        @SerializedName("token") val token:String
)