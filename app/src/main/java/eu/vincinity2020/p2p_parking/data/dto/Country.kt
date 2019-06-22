package eu.vincinity2020.p2p_parking.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Ankush on 19/11/18.
 */
data class Country(
        @SerializedName("id") val id: Long,
        @SerializedName("country") val country: String,
        @SerializedName("code") val code: String?,
        @SerializedName("descriptor") val descriptor: String?,
        @SerializedName("uncode") val uncode: String?
) {
    override fun toString(): String {
        return country
    }
}