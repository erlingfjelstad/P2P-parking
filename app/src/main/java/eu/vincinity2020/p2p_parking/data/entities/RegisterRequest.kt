package eu.vincinity2020.p2p_parking.data.entities

import com.google.gson.annotations.SerializedName

data class RegisterRequest (
        @SerializedName("firstName") val firstName:String,
        @SerializedName("lastName") val lastName:String,
        @SerializedName("mobile") val mobile:String,
        @SerializedName("email") val email:String,
        @SerializedName("password") val password:String,
        @SerializedName("androidDeviceId") val fcmToken:String
)