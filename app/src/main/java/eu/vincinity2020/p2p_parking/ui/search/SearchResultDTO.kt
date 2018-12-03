package eu.vincinity2020.p2p_parking.ui.search

import com.google.gson.annotations.SerializedName
import org.osmdroid.util.BoundingBox

data class Address(val placeId: Long,
                   val licence: String,
                   val osmType: String,
                   val osmId: String,
                   val boundingBox: BoundingBox,
                   @SerializedName("lon") val longitude: Double,
                   @SerializedName("lat") val latitude: Double,
                   @SerializedName("display_name") val displayName: String,
                   @SerializedName("place_rank") val placeRank: Int,
                   @SerializedName("importance") val importance: Double
)