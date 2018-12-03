package eu.vincinity2020.p2p_parking.app.common

interface MvpPresenter {
    fun attach(view: MvpView)
    fun detach()
}
