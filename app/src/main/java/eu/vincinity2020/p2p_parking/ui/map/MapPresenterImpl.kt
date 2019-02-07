package eu.vincinity2020.p2p_parking.ui.map

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.ParkingSensor
import eu.vincinity2020.p2p_parking.data.entities.User
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
                            val listType = object : TypeToken<ArrayList<ParkingSensor>>() {}.type
                            val markers = Gson().fromJson<Any>(response.getAsJsonArray("data"), listType) as ArrayList<ParkingSensor>
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
                            val listType = object : TypeToken<ArrayList<ParkingSensor>>() {}.type
                            val markers = Gson().fromJson<Any>(response.getAsJsonArray("data"), listType) as ArrayList<ParkingSensor>
                            v.getMarkers(markers)
                        } else
                            if (response.has("message")) {
                                v.onUnknownError(response.get("message").asString)
                            }
                    }


                })
        compositeDisposable.add(dishCategory)
    }

    override fun createNewBooking(user: User, parkingSensor: ParkingSensor, fromTime: String, toTime: String) {

        val serverGson =  JsonObject()
        serverGson.addProperty("fromTime", fromTime)
        serverGson.addProperty("toTime", toTime)
        serverGson.add("user", Gson().toJsonTree(user))
        serverGson.add("sensor", Gson().toJsonTree(parkingSensor))


        val dishCategory = networkService.bookParkingSpace(Credentials.basic(user.email, user.password), serverGson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
                            if (response.has("message")) {
                                v.onUnknownError("Parking booked.")
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