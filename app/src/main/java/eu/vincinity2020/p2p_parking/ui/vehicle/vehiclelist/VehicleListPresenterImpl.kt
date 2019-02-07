package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.Vehicles
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Credentials
import java.util.ArrayList


class VehicleListPresenterImpl(private val networkService: NetworkService) :VehicleListPresenter{


    lateinit var v:VehicleListMvpView

    //    var searchView: SearchView? = null            //init views here
    private val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        v=view as VehicleListMvpView

    }

    override fun detach() {
    }


    override fun getVehicles(userId: Long, email:String, password: String) {

        val dishCategory = networkService.getVehicles(Credentials.basic(email, password),userId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {

                            val listType = object : TypeToken<ArrayList<Vehicles>>() {}.type
                            val allVehicles = Gson().fromJson<Any>(response.getAsJsonArray("data"), listType) as ArrayList<Vehicles>

                            v.updateVehicleList(allVehicles)
                        }
                        else
                        {
                            if (response.has("message"))
                                v.onUnknownError(response.get("message").asString)

                        }
                    }


                })
        compositeDisposable.add(dishCategory)
    }



    override fun updateDefaultVehicle(userId: Long, email:String, password: String, selectedVehicle: Vehicles, position: Int) {

        val dishCategory = networkService.updateDefaultVehicle(Credentials.basic(email, password),Gson().toJsonTree(selectedVehicle).asJsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {


                            v.onDefaultVehicleUpdated(position)
                        }
                        else
                        {
                            if (response.has("message"))
                                v.onUnknownError(response.get("message").asString)

                        }
                    }


                })
        compositeDisposable.add(dishCategory)
    }


}