package eu.vincinity2020.p2p_parking.data.entities.user

import com.google.gson.annotations.SerializedName

data class EditUserRequest (
        @SerializedName("id") val id:String? = null,
        @SerializedName("firstName") val firstName:String? = null,
        @SerializedName("lastName") val lastName:String? = null,
        @SerializedName("gender") val gender:String? = null,
        @SerializedName("birthDate") val birthDate:String? = null,
        @SerializedName("address") val address:String? = null,
        @SerializedName("zip") val zip:String? = null,
        @SerializedName("city") val city:String? = null,
        @SerializedName("phone") val phone:String? = null,
        @SerializedName("mobile") val mobile:String? = null,
        @SerializedName("email") val email:String? = null,
        @SerializedName("userType") val userType:String? = null,
        @SerializedName("parkingPreference") val parkingPreference:String? = null,
        @SerializedName("disabilities") val disabilities:String? = null,
        @SerializedName("role") val role:String? = null
)