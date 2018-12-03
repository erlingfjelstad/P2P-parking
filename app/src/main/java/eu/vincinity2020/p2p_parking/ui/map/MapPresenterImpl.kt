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
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import java.util.ArrayList


class MapPresenterImpl(private val networkService: NetworkService) :MapPresenter {

    lateinit var v:MapMvpView

    //    var searchView: SearchView? = null            //init views here
    val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        v=view as MapMvpView
        getNearbyParking("a")

    }

    override fun detach() {
    }

    override fun getNearbyParking(query: String) {

//        val dishCategory = networkService.getParkingSites("22.176660","76.068152","1")
        val dishCategory = networkService.getParkingSites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
                            val listType = object : TypeToken<ArrayList<ParkingSpot>>() {}.type
                            val markers = Gson().fromJson<Any>(response.getAsJsonArray("data"), listType) as ArrayList<ParkingSpot>
                            v.getMarkers(markers)
                        }
                    }


                })
        compositeDisposable.add(dishCategory)
    }
}