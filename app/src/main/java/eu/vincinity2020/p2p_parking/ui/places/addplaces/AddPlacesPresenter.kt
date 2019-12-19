package eu.vincinity2020.p2p_parking.ui.places.addplaces

import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.locations.PlaceRequest
import eu.vincinity2020.p2p_parking.data.entities.places.Place
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials


class AddPlacesPresenter(private val networkService: NetworkService) : AddPlacesContract.Presenter {
    private val allDisposables = CompositeDisposable()

    override fun getAllPlaces(view: AddPlacesContract.View) {
        val user = App.get((view as Fragment).requireContext()).getUser()
        if (user != null) {
            allDisposables.addAll(
                    networkService.getSavedLocations(Credentials.basic(user.email, user.password), user.id.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                view.handleGetAllPlacesSuccess(it.places)
                            }, {

                            })
            )
        }
    }

    override fun savePlace(view: AddPlacesContract.View, place: Place) {
        //convert place to location request object
        val user = App.get((view as Fragment).requireContext()).getUser()
        if (user != null) {
            var savePlaceRequest: PlaceRequest
            val placeFields = listOf(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG)
            val fetchPlacesRequest = FetchPlaceRequest.newInstance(place.placeId, placeFields)
            Places.createClient((view as Fragment).requireContext()).fetchPlace(fetchPlacesRequest).addOnSuccessListener { fetchPlaceResponse ->
                if (fetchPlaceResponse.place.latLng?.latitude != null && fetchPlaceResponse.place.latLng?.longitude != null) {
                    savePlaceRequest = PlaceRequest(fetchPlaceResponse.place.latLng?.latitude!!,
                            fetchPlaceResponse.place.latLng?.longitude!!, place.name)

                    allDisposables.add(
                            networkService.saveMyLocation(Credentials.basic(user.email, user.password), savePlaceRequest)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        if (it?.isError == false) {
                                            view.handleSavePlaceSuccess()
                                        }
                                    }, {

                                    })
                    )
                }
            }
        }

    }

    override fun detach() {
        allDisposables.clear()
    }
}