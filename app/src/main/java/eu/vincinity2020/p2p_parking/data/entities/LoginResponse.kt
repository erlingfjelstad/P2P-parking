package eu.vincinity2020.p2p_parking.data.entities

import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("user") val user:User,
        @SerializedName("accountNonExpired") val isAccountExpired:Boolean,
        @SerializedName("accountNonLocked") val isAccountLocked:Boolean,
        @SerializedName("credentialsNonExpired") val isCredentialExpired:Boolean,
        @SerializedName("username") val username:String,
        @SerializedName("enabled") val isEnabled:Boolean,
        @SerializedName("token") val token:String
)