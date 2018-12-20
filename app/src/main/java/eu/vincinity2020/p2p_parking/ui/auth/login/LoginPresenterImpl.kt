package eu.vincinity2020.p2p_parking.ui.auth.login

import com.google.gson.Gson
import com.google.gson.JsonObject
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.data.dto.UserDTO
import eu.vincinity2020.p2p_parking.data.entities.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials

class LoginPresenterImpl(private val networkService: NetworkService) : LoginPresenter {



    override fun saveFcmToken(token: String, email: String, password: String)
         {
             val tokenJson = JsonObject()
             tokenJson.addProperty("", token)

            disposable.add(networkService.saveFcmToken(Credentials.basic(email, password), tokenJson)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { loginView?.onLoadFinish() }
                    .subscribeWith(object : NetworkResponse<JsonObject>(loginView) {
                        override fun onSuccess(response: JsonObject) {
                            loginView?.onFcmTokenSaved()
                        }


                    }))


    }

    private var loginView: LoginView? = null
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun attemptLogin(email: String, password: String) {

        disposable.add(networkService.loginUser(Credentials.basic(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : NetworkResponse<JsonObject>(loginView) {
                    override fun onSuccess(response: JsonObject) {
                        loginView?.onSuccessfulLogin(Gson().fromJson(response.getAsJsonObject("user"),User::class.java ))
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        loginView?.onLoadFinish()
                    }


                }))
    }

    override fun attach(view: MvpView) {
        loginView = view as? LoginView
    }

    override fun detach() {
        disposable.clear()
        loginView = null
    }
}