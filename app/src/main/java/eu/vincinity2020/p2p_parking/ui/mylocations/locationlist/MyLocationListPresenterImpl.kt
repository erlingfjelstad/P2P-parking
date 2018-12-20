package eu.vincinity2020.p2p_parking.ui.mylocations.locationlist

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.MyLocation
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.data.entities.Vehicles
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Credentials


class MyLocationListPresenterImpl(private val networkService: NetworkService) : MyLocationListPresenter {


    lateinit var v: MyLocationListMvpView

    //    var searchView: SearchView? = null            //init views here
    private val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        v = view as MyLocationListMvpView

    }

    override fun detach() {
    }


    override fun getAllLocations(user: User) {

        val dishCategory = networkService.getSavedLocations(Credentials.basic(user.email, user.password), user.id.toString(), "50")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {

                            val listType = object : TypeToken<ArrayList<MyLocation>>() {}.type
                            val allMyLocations = Gson().fromJson<Any>(response.getAsJsonArray("data"), listType) as ArrayList<MyLocation>
                            v.updateLocations(allMyLocations)
                        } else {
                            if (response.has("message"))
                                v.onUnknownError(response.get("message").asString)
                        }
                    }


                })
        compositeDisposable.add(dishCategory)
    }

}