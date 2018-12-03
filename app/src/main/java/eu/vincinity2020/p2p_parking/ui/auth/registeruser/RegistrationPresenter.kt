package eu.vincinity2020.p2p_parking.ui.auth.registeruser

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter
import eu.vincinity2020.p2p_parking.data.dto.Country

interface RegistrationPresenter : MvpPresenter {
    fun attemptRegistration( firstName: String, lastName: String, mobileNo: String, email: String, password: String, country: Country, gender : String, role: String, fcmToken: String)

    fun getCountries()
}