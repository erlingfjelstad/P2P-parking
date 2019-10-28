package eu.vincinity2020.p2p_parking.ui.places.listplaces

import androidx.fragment.app.Fragment
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.utils.getApiToken
import eu.vincinity2020.p2p_parking.utils.getErrorMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials

class PlacesListPresenter(private val networkService:NetworkService): PlacesListContract.Presenter {
    private val allDisposables = CompositeDisposable()


    override fun fetchPlaces(view: PlacesListContract.View) {
        val user = App.get((view as Fragment).requireContext()).getUser()
        if (user != null) {
            allDisposables.addAll(
                    networkService.getSavedLocations(Credentials.basic(user.email, user.password), user.id.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                view.handleFetchPlacesSuccess(it.places)
                            }, {

                            })
            )
        }
    }

    override fun deletePlace(view: PlacesListContract.View, id: Int, position: Int) {
        allDisposables.add(
                networkService.deleteLocation(getApiToken() ,id.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if(it.isError == false){
                                view.handleDeleteLocationSuccess(position)
                            }
                        },{
                            view.handleFailure(it.getErrorMessage())
                        })
        )
    }

    override fun detach() {
        allDisposables.dispose()
    }
}