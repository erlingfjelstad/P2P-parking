package eu.vincinity2020.p2p_parking.ui.bookings

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.Bookings
import eu.vincinity2020.p2p_parking.data.entities.User
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Credentials
import java.util.ArrayList


class ViewBookingsPresenterImpl(private val networkService: NetworkService) :ViewBookingsPresenter{


    lateinit var v:ViewBookingsMvpView

    //    var searchView: SearchView? = null            //init views here
    private val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        v=view as ViewBookingsMvpView

    }

    override fun detach() {
    }


    override fun getAllBookingList(user: User) {

        val dishCategory = networkService.getParkingBookings(Credentials.basic(user.email, user.password), "100", user.id.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { v.onLoadFinish() }


                .subscribeWith(object : NetworkResponse<JsonObject>(v) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
                            val listType = object : TypeToken<ArrayList<Bookings>>() {}.type
                            val userData = Gson().fromJson<Any>(response.getAsJsonObject("data").getAsJsonArray("content"), listType) as ArrayList<Bookings>
                            v.updateBookingList(userData)
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