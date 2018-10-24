package eu.vincinity2020.p2p_parking.search

data class SearchResult(val text: String, val isFavorite: Boolean,
                        val latitude: Double, val longitude: Double) {
    companion object {
        fun fromDTO(searchResultDTO: List<Address>): List<SearchResult> {
            return searchResultDTO.map {
                SearchResult(it.displayName,
                        false,
                        it.latitude,
                        it.longitude)
            }
        }
    }
}