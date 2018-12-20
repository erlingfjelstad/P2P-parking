package eu.vincinity2020.p2p_parking.ui.map

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eu.vincinity2020.p2p_parking.app.network.OpenStreetMapService
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.dto.Country
import eu.vincinity2020.p2p_parking.data.entities.ParkingSpot
import eu.vincinity2020.p2p_parking.data.entities.User
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import okhttp3.Credentials
import java.util.ArrayList


class MapPresenterImpl(private val networkService: NetworkService) : MapPresenter {


    lateinit var v: MapMvpView

    //    var searchView: SearchView? = null            //init views here
    val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        v = view as MapMvpView

    }

    override fun detach() {
    }

    override fun getNearbyParkingSensors(email: String, password: String,lat: Double, lon: Double) {

        val dishCategory = networkService.getParkingSites(Credentials.basic(email, password),"22.176660","76.068152","1")
//        val dishCategory = networkService.getParkingSites(Credentials.basic(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
                            val listType = object : TypeToken<ArrayList<ParkingSpot>>() {}.type
                            val markers = Gson().fromJson<Any>(response.getAsJsonArray("data"), listType) as ArrayList<ParkingSpot>
                            v.getMarkers(markers)
                        } else
                            if (response.has("message")) {
                                v.onUnknownError(response.get("message").asString)
                            }
                    }


                })
        compositeDisposable.add(dishCategory)
    }

    override fun getDefaultParkingSensors(email: String, password: String) {

//        val dishCategory = networkService.getParkingSites("22.176660","76.068152","1")
        val dishCategory = networkService.getParkingSites(Credentials.basic(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
                            val listType = object : TypeToken<ArrayList<ParkingSpot>>() {}.type
                            val markers = Gson().fromJson<Any>(response.getAsJsonArray("data"), listType) as ArrayList<ParkingSpot>
                            v.getMarkers(markers)
                        } else
                            if (response.has("message")) {
                                v.onUnknownError(response.get("message").asString)
                            }
                    }


                })
        compositeDisposable.add(dishCategory)
    }



    override fun createNewBooking(user: User, parkingSpot: ParkingSpot, fromTime: String, toTime: String) {

        val serverGson =  JsonObject()
        serverGson.addProperty("fromTime", fromTime)
        serverGson.addProperty("toTime", toTime)
        serverGson.add("user", Gson().toJsonTree(user))
        serverGson.add("sensor", Gson().toJsonTree(parkingSpot))


        val dishCategory = networkService.bookParkingSpace(Credentials.basic(user.email, user.password), serverGson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
                            if (response.has("message")) {
                                v.onUnknownError(response.get("message").asString)
                            }
                        } else
                            if (response.has("message")) {
                                v.onUnknownError(response.get("message").asString)
                            }
                    }


                })
        compositeDisposable.add(dishCategory)
    }



}