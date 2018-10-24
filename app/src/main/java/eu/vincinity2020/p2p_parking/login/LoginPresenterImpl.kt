package eu.vincinity2020.p2p_parking.login

import eu.vincinity2020.p2p_parking.app.NetworkService
import eu.vincinity2020.p2p_parking.common.View
import eu.vincinity2020.p2p_parking.entities.User
import io.reactivex.disposables.CompositeDisposable

class LoginPresenterImpl(private val networkService: NetworkService) : LoginPresenter {
    private var loginView: LoginView? = null
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun attemptLogin(email: String, password: String) {
        loginView?.onSuccessfulLogin(User(1, "firstName", "surName"))

//        disposable.add(networkService.loginUser(email, password)
//                .subscribeOn(Schedulers.io())
//                .map { dto -> User.fromDTO(dto) }
//                .toObservable()
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally { loginView?.onLoadFinish() }
//                .subscribeWith(object : NetworkResponse<User>(loginView) {
//                    override fun onSuccess(response: User) {
//                        loginView?.onSuccessfulLogin(response)
//                    }
//                }))
    }

    override fun attach(view: View) {
        loginView = view as? LoginView
    }

    override fun detach() {
        disposable.clear()
        loginView = null
    }
}