package eu.vincinity2020.p2p_parking.data.entities.directions

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.maps.model.LatLng

@Entity
data class UserStop(
        val name: String = "",
        @PrimaryKey val location: LatLng,
        var isStopDone:Boolean = false
){
    var eta: String? = null
    var distance:String? = null
}