package eu.vincinity2020.p2p_parking.ui.auth.registeruser

import android.content.Context
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.RegisterRequest
import eu.vincinity2020.p2p_parking.data.entities.SaveFcmTokenRequest
import eu.vincinity2020.p2p_parking.utils.P2PPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials

class RegisterPresenterImpl(private val networkService: NetworkService): RegistrationPresenter {


    private lateinit var registrationView: RegisterView
    private lateinit var context: Context
    private val disposable: CompositeDisposable = CompositeDisposable()


    override fun attach(view: MvpView) {
    }

    override fun attach(view: RegisterView) {
        registrationView = view
        context = view as BaseActivity
    }

    override fun registerUser(request: RegisterRequest) {
        registrationView.showProgress()
        disposable.add(networkService.registerUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { registrationView.hideProgress() }
                .subscribe({
                    attemptLogin("jay2@mail.com", "123456")
                }, {
                    registrationView.onRegisterError(it)
                }))
    }

    private fun attemptLogin(email: String, password: String) {
        registrationView.showProgress()
        disposable.add(networkService.loginUser(Credentials.basic(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { registrationView.hideProgress() }
                .subscribe({
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
                    registrationView.onRegisterError(it)
                }))
    }

    private fun saveFcmToken(token: String, email: String, password: String) {
        registrationView.showProgress()
        disposable.add(networkService.saveFcmToken(Credentials.basic(email, password), SaveFcmTokenRequest(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { registrationView.hideProgress() }
                .subscribe({
                    registrationView.onRegisterSuccessful()
                }, {
                    registrationView.onRegisterError(it)
                }))

    }


    override fun detach() {
        disposable.dispose()
    }
}