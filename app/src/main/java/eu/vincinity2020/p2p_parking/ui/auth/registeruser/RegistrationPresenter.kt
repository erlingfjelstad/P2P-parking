package eu.vincinity2020.p2p_parking.ui.auth.registeruser

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter
import eu.vincinity2020.p2p_parking.data.dto.Country
import eu.vincinity2020.p2p_parking.data.entities.RegisterRequest

interface RegistrationPresenter: MvpPresenter {

    fun registerUser(request: RegisterRequest)

    fun attach(view: RegisterView)
}