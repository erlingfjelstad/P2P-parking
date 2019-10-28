package eu.vincinity2020.p2p_parking.ui.profile.edituser

import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.data.entities.user.EditUserRequest
import eu.vincinity2020.p2p_parking.utils.getErrorMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EditUserPresenter(private val networkService: NetworkService):EditUserContract.Presenter {

    private val allDisposables = CompositeDisposable()

    override fun updateUserRole(view: EditUserContract.View, editUser: EditUserRequest) {
        allDisposables.add(
                networkService.editUser(editUser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            view.handleSuccess(it.message ?: "Success")
                        }, {
                            view.handleFailure(it.getErrorMessage())
                        })
        )
    }

    override fun detach() {
        allDisposables.dispose()
    }
}