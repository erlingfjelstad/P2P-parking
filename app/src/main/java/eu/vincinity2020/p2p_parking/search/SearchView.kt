package eu.vincinity2020.p2p_parking.search

import eu.vincinity2020.p2p_parking.common.View

interface SearchView : View {
    fun onSearchResultRecieved(result: List<SearchResult>)
    fun onSearchStarted()
    fun onSearchFinished()
    fun renderEmptyState()
}