package eu.vincinity2020.p2p_parking.ui.profile.edit.part1

import com.google.gson.Gson
import com.google.gson.JsonObject
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.User
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class EditProfile1PresenterImpl(private val networkService: NetworkService) :EditProfile1Presenter{


    lateinit var v:EditProfile1MvpView

    //    var searchView: SearchView? = null            //init views here
    private val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        v=view as EditProfile1MvpView

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