package eu.vincinity2020.p2p_parking.search

import eu.vincinity2020.p2p_parking.common.Presenter

interface SearchPresenter : Presenter {
    fun queryAddress(query: String)
}