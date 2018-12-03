package eu.vincinity2020.p2p_parking.ui.mylocations.addlocation

import com.google.gson.Gson
import com.google.gson.JsonObject
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.User
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class AddLocationPresenterImpl(private val networkService: NetworkService) :AddLocationPresenter{


    lateinit var v:AddLocationMvpView

    //    var searchView: SearchView? = null            //init views here
    private val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        v=view as AddLocationMvpView

    }

    override fun detach() {
    }


    override fun saveLocation(lat: String, lon : String, desc: String) {
        val serverGson =  JsonObject()
        serverGson.addProperty("lat", lat)
        serverGson.addProperty("lon", lon)
        serverGson.addProperty("description", desc)
        val dishCategory = networkService.saveMyLocation(serverGson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
//                            val userData = Gson().fromJson<User>(response.getAsJsonObject("data"), User::class.java)
//                            v.updateLocations(userData)
                            v.onLoadFinish()
                        }
                        else
                        {
                            v.onLoadFinish()

                        }
                    }


                })
        compositeDisposable.add(dishCategory)
    }

}