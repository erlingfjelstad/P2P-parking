package eu.vincinity2020.p2p_parking.ui.auth.registeruser

import eu.vincinity2020.p2p_parking.app.common.MvpView
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.RegisterRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RegisterPresenterImpl(private val networkService: NetworkService) : RegistrationPresenter {


    private lateinit var registrationView: RegisterView
    private val disposable: CompositeDisposable = CompositeDisposable()



    override fun attach(view: MvpView) {
        registrationView = view as RegisterView
    }

    override fun registerUser(request: RegisterRequest) {
        disposable.add(networkService.registerUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { registrationView.hideProgress() }
                .subscribe({
                    registrationView.onRegisterSuccessful()
                },{
                    registrationView.onRegisterError(it)
                }))
    }


    override fun detach() {
        disposable.dispose()
    }
}