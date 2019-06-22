package eu.vincinity2020.p2p_parking.ui.auth.registeruser

interface RegisterView {
    fun hideProgress()

    fun showProgress()

    fun onRegisterSuccessful()

    fun onRegisterError(e: Throwable)
}