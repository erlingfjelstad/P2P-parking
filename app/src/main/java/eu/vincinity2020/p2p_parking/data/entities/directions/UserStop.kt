package eu.vincinity2020.p2p_parking.data.entities.directions

import com.google.maps.model.LatLng


data class UserStop(
        val name: String = "",
        val location: LatLng
)