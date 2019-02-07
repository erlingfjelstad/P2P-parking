package eu.vincinity2020.p2p_parking.app.common

interface MvpView {
    fun onUnknownError(errorMessage: String)
    fun onTimeout()
    fun onNetworkError()
}