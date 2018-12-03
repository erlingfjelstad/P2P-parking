package eu.vincinity2020.p2p_parking.ui.auth.registeruser

import com.google.gson.JsonObject
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.data.dto.Country
import eu.vincinity2020.p2p_parking.data.entities.User

interface RegisterView : MvpView {
    fun onSuccessfulRegistration(response: JsonObject)

    fun onCountriesLoaded(countries: ArrayList<Country>)

    fun onLoadFinish()
}