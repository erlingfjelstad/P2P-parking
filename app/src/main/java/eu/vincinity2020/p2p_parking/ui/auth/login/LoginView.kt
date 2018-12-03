package eu.vincinity2020.p2p_parking.ui.auth.login

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.entities.User

interface LoginView : MvpView {
    fun onFcmTokenSaved()
    fun onSuccessfulLogin(user: User)
    fun onLoadFinish()
}