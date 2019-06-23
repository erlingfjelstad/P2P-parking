package eu.vincinity2020.p2p_parking.ui.auth.login

import android.content.Context
import com.google.gson.JsonObject
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkResponse
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.SaveFcmTokenRequest
import eu.vincinity2020.p2p_parking.utils.P2PPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials
import retrofit2.HttpException

class LoginPresenterImpl(private val networkService: NetworkService): LoginPresenter {

    private lateinit var loginView: LoginView
    private lateinit var context: Context
    private val disposable: CompositeDisposable = CompositeDisposable()

    fun saveFcmToken(token: String, email: String, password: String) {

        disposable.add(networkService.saveFcmToken(Credentials.basic(email, password), SaveFcmTokenRequest(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { loginView.hideProgress() }
                .subscribe({
                    loginView.onFcmTokenSaved()
                }, {

                }))


    }

    override fun attemptLogin(email: String, password: String) {
        loginView.showProgress()
        disposable.add(networkService.loginUser(Credentials.basic(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loginView.hideProgress()
                    if (it != null) {
                        App.get(context).setIsLoggedIn(true)
                        App.get(context).setUserEmail(it.user.id)
                        it.user.password = password
                        App.get(context).setUser(it.user)

                        P2PPreferences(context).getString(AppConstants.FCM_TOKEN)?.let { fcmToken ->
                            saveFcmToken(fcmToken, email, password)
                        }
                    }
                }, {
                    loginView.hideProgress()
                    loginView.onLoginError(it)
                }))
    }

    override fun attach(view: MvpView) {
        loginView = view as LoginView
        context = view as BaseActivity
    }

    private fun getInvalidCredentialsException() = Exception("Invalid Credentials")

    override fun detach() {
        disposable.dispose()
    }
}