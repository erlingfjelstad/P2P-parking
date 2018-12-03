package eu.vincinity2020.p2p_parking.ui.profile.edit.part2

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.ParkingSpot
import eu.vincinity2020.p2p_parking.data.entities.User
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList


class EditProfile2PresenterImpl(private val networkService: NetworkService) :EditProfile2Presenter{


    lateinit var v:EditProfile2MvpView

    //    var searchView: SearchView? = null            //init views here
    private val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        v=view as EditProfile2MvpView

    }

    override fun detach() {
    }


    override fun getUserProfile(userId: Long) {

        val dishCategory = networkService.getUser(userId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
                            val markers = Gson().fromJson<User>(response.getAsJsonObject("data"), User::class.java)
                            v.updateUserProfileInfo(markers)
                        }
                        else
                        {

                        }
                    }


                })
        compositeDisposable.add(dishCategory)
    }

}