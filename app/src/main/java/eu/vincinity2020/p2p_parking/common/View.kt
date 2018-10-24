package eu.vincinity2020.p2p_parking.common

interface View {
    fun onUnknownError(errorMessage: String)
    fun onTimeout()
    fun onNetworkError()
}