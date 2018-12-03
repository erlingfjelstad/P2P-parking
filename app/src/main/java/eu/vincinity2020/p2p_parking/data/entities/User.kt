package eu.vincinity2020.p2p_parking.data.entities

import eu.vincinity2020.p2p_parking.data.dto.Country


data class User( val id: Long, val firstName: String, val lastName: String, val email: String, val mobile: String
                , val country: Country)