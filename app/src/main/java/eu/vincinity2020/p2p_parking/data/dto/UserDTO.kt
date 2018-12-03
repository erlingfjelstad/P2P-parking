package eu.vincinity2020.p2p_parking.data.dto

data class UserDTO(val userId: Long, val firstName: String, val lastName: String, val userName: String,
                   val email: String, val mobile: String , val country: Country) : BaseDTO()