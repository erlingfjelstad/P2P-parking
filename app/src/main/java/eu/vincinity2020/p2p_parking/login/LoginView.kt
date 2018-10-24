package eu.vincinity2020.p2p_parking.login

import eu.vincinity2020.p2p_parking.common.View
import eu.vincinity2020.p2p_parking.entities.User

interface LoginView : View {
    fun onSuccessfulLogin(user: User)
    fun onLoadFinish()
}