package eu.vincinity2020.p2p_parking.data.entities.locations

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceRequest(
        @SerializedName("lat") val lat: Double,
        @SerializedName("lon") val long: Double,
        @SerializedName("description") val description: String,
        @SerializedName("id") val id: Int? = null
) : Parcelable