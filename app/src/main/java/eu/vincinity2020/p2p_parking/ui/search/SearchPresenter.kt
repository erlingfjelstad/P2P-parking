package eu.vincinity2020.p2p_parking.ui.search

import eu.vincinity2020.p2p_parking.app.common.MvpPresenter

interface SearchPresenter : MvpPresenter {
    fun queryAddress(query: String)
}