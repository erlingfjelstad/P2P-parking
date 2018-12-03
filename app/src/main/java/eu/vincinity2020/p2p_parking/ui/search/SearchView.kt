package eu.vincinity2020.p2p_parking.ui.search

import eu.vincinity2020.p2p_parking.app.common.MvpView

interface SearchView : MvpView {
    fun onSearchResultRecieved(result: List<SearchResult>)
    fun onSearchStarted()
    fun onSearchFinished()
    fun renderEmptyState()
}