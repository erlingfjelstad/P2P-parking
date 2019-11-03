package eu.vincinity2020.p2p_parking.data.entities.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.maps.model.LatLng

class LatLngTypeConverters {

    val gson = Gson()

    @TypeConverter
    fun stringToLatlng(data: String?): LatLng? {
        return if (data.isNullOrBlank()) {
            null
        } else {
            gson.fromJson(data, LatLng::class.java)
        }
    }

    @TypeConverter
    fun latLngToString(data: LatLng): String = gson.toJson(data)

}