package eu.vincinity2020.p2p_parking.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import eu.vincinity2020.p2p_parking.dto.UserDTO

@Entity
data class User(@PrimaryKey val id: Long, val firstName: String, val surname: String) {
    companion object {
        fun fromDTO(userDTO: UserDTO): User {
            return User(userDTO.userId, userDTO.firstName, userDTO.surname)
        }
    }
}
