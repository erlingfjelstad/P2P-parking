package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import com.google.gson.Gson
import com.google.gson.JsonObject
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.data.entities.Vehicles
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class VehicleListPresenterImpl(private val networkService: NetworkService) :VehicleListPresenter{


    lateinit var v:VehicleListMvpView

    //    var searchView: SearchView? = null            //init views here
    private val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        v=view as VehicleListMvpView

    }

    override fun detach() {
    }


    override fun getAllVehicleList(userId: Long) {

        val dishCategory = networkService.getUser(userId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
                            val userData = Gson().fromJson<Vehicles>(response.getAsJsonObject("data"), Vehicles::class.java)
                            v.updateVehicleList(userData)
                        }
                        else
                        {

                        }
                    }


                })
        compositeDisposable.add(dishCategory)
    }

}