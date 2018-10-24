package eu.vincinity2020.p2p_parking.app

import eu.vincinity2020.p2p_parking.dto.UserDTO
import io.reactivex.Single
import retrofit2.http.POST

interface NetworkService {
    @POST("/login")
    fun loginUser(email: String, password: String): Single<UserDTO>
}