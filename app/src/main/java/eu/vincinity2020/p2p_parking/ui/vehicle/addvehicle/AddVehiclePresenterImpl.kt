package eu.vincinity2020.p2p_parking.ui.vehicle.addvehicle

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.data.entities.VehicleTypes
import eu.vincinity2020.p2p_parking.data.entities.Vehicles
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Credentials
import java.util.ArrayList


class AddVehiclePresenterImpl(private val networkService: NetworkService) : AddVehiclePresenter {


    lateinit var v: AddVehicleMvpView

    //    var searchView: SearchView? = null            //init views here
    private val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        v = view as AddVehicleMvpView

    }

    override fun detach() {
    }


    override fun registerNewVehicle(email: String, password: String, userId: Long, brandName: String,
                                    model: String, description: String, regNo: String, mfgYear: String,
                                    vehicleType: VehicleTypes, height: String, width: String, length: String, weight: String, fuelType: String) {

        val serverGson = JsonObject()
        serverGson.addProperty("name", model)
        serverGson.addProperty("regno", regNo)
        serverGson.addProperty("description", description)

        serverGson.addProperty("brand", brandName)
        serverGson.addProperty("model", model)
        serverGson.addProperty("height", height)
        serverGson.addProperty("width", width)
        serverGson.addProperty("length", length)
        serverGson.addProperty("weight", weight)
        serverGson.addProperty("year", mfgYear)
        serverGson.add("vehicleType", Gson().toJsonTree(vehicleType))
        serverGson.addProperty("fuelType", fuelType)


        val dishCategory = networkService.saveNewVehicle(Credentials.basic(email, password), serverGson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
                            if (response.get("message") != null)
                                Log.e("Update add api", response.toString());
                        } else {

                        }
                    }


                })
        compositeDisposable.add(dishCategory)


    }

    override fun getVehicleTypes(userId: Long) {

        val dishCategory = networkService.getVehicleTypeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {

                            val listType = object : TypeToken<ArrayList<VehicleTypes>>() {}.type
                            val vehicleTypes = Gson().fromJson<Any>(response.getAsJsonArray("data"), listType) as ArrayList<VehicleTypes>

                            v.updateVehicleTypes(vehicleTypes)
                        } else {

                        }
                    }


                })
        compositeDisposable.add(dishCategory)
    }


}