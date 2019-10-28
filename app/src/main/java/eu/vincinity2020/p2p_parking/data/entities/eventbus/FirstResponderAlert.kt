package eu.vincinity2020.p2p_parking.data.entities.eventbus

import java.io.Serializable

data class FirstResponderAlert(
        val locationName: String,
        val lat: Double,
        val long: Double
) : Serializable