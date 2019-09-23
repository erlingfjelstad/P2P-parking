package eu.vincinity2020.p2p_parking.ui.places.addplaces

import eu.vincinity2020.p2p_parking.data.entities.locations.PlaceRequest
import eu.vincinity2020.p2p_parking.data.entities.places.Place


class AddPlacesContract {

    interface View {
        fun showProgress()

        fun hideProgress()

        fun handleSavePlaceSuccess()

        fun handleGetAllPlacesSuccess(places: ArrayList<PlaceRequest>)

        fun handleGetAllPlacesFailure()
    }

    interface Presenter {

        fun savePlace(view: View, place: Place)

        fun getAllPlaces(view: View)

        fun detach()
    }

}