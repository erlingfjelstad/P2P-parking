package eu.vincinity2020.p2p_parking.ui.places.listplaces

import eu.vincinity2020.p2p_parking.data.entities.locations.PlaceRequest

class PlacesListContract {

    interface View {
        fun handleFetchPlacesSuccess(places: ArrayList<PlaceRequest>)

        fun handleDeleteLocationSuccess(position: Int)

        fun handleFailure(message: String)
    }

    interface Presenter {
        fun fetchPlaces(view: View)

        fun deletePlace(view: View, id: Int, position: Int)

        fun detach()
    }
}