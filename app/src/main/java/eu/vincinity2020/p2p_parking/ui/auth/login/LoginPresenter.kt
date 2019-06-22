package eu.vincinity2020.p2p_parking.ui.auth.login

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface LoginPresenter : MvpPresenter {
    fun attemptLogin(email: String, password: String)
}