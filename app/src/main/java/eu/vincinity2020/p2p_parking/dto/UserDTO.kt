package eu.vincinity2020.p2p_parking.dto

data class UserDTO(val userId: Long, val firstName: String,
                   val surname: String, val userName: String) : BaseDTO()