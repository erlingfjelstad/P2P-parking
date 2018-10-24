package eu.vincinity2020.p2p_parking.login

import eu.vincinity2020.p2p_parking.common.Presenter

interface LoginPresenter : Presenter {
    fun attemptLogin(email: String, password: String)
}