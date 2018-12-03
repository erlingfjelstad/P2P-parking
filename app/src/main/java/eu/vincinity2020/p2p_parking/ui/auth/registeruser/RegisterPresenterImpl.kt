package eu.vincinity2020.p2p_parking.ui.auth.registeruser

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.data.dto.Country
import eu.vincinity2020.p2p_parking.data.entities.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class RegisterPresenterImpl(private val networkService: NetworkService) : RegistrationPresenter {


    private var registrationView: RegisterView? = null
    private val disposable: CompositeDisposable = CompositeDisposable()



    override fun attach(view: MvpView) {
        registrationView = view as? RegisterView
    }




    override fun getCountries() {
        disposable.add(networkService.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { registrationView?.onLoadFinish() }
                .subscribeWith(object : NetworkResponse<JsonObject>(registrationView) {
                    override fun onSuccess(response: JsonObject) {
                        if (!response.get("error").asBoolean) {
                            val listType = object : TypeToken<ArrayList<Country>>() {}.type
                            registrationView?.onCountriesLoaded(Gson().fromJson(response.getAsJsonArray("data"), listType))
                        }
                    }


                }))
    }

    override fun attemptRegistration(firstName: String, lastName: String, mobileNo: String, email: String, password: String, country: Country, gender: String, role: String, fcmToken : String) {
        var serverInput = JsonObject()
        serverInput.addProperty("firstName", firstName);
        serverInput.addProperty("lastName", lastName);
        serverInput.addProperty("mobile", mobileNo);
        serverInput.addProperty("email", email);
        serverInput.addProperty("password", password);
        serverInput.addProperty("role", role);
        serverInput.addProperty("gender", gender);
        serverInput.addProperty("androidDeviceId", fcmToken);

        serverInput.add("country", (Gson().toJsonTree(country)))

        disposable.add(networkService.registerUser(serverInput)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { registrationView?.onLoadFinish() }
                .subscribeWith(object : NetworkResponse<JsonObject>(registrationView) {
                    override fun onSuccess(response: JsonObject) {
                        registrationView?.onSuccessfulRegistration(response)
                    }


                }))

    }


    override fun detach() {
        disposable.clear()
        registrationView = null
    }
}