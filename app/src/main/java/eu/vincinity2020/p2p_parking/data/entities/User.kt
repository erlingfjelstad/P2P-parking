package eu.vincinity2020.p2p_parking.data.entities

import com.google.gson.annotations.SerializedName
import eu.vincinity2020.p2p_parking.data.dto.Country


data class User(
        @SerializedName("id") val id: Long,
        @SerializedName("firstName") val firstName: String,
        @SerializedName("lastName") val lastName: String,
        @SerializedName("gender") val gender: String,
        @SerializedName("birthDate") val birthDate: String,
        @SerializedName("address") val address: String?,
        @SerializedName("zip") val zip: String?,
        @SerializedName("city") val city: String?,
        @SerializedName("phone") val phone: String,
        @SerializedName("mobile") val mobile: String,
        @SerializedName("role") val role: String,
        @SerializedName("email") val email: String,
        @SerializedName("password") var password: String,
        @SerializedName("image") val image: String?,
        @SerializedName("country") val country: Country,
        @SerializedName("userType") val userType: String?,
        @SerializedName("parkingPreference") val parkingPreference: String?,
        @SerializedName("disability") val disability: String?
)