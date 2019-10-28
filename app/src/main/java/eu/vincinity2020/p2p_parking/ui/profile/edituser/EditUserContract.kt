package eu.vincinity2020.p2p_parking.ui.profile.edituser

import eu.vincinity2020.p2p_parking.data.entities.user.EditUserRequest

class EditUserContract {

    interface View {
        fun showProgress()

        fun hideProgress()

        fun handleSuccess(message: String)

        fun handleFailure(message: String)
    }

    interface Presenter {
        fun updateUserRole(view:View,editUser: EditUserRequest)

        fun detach()
    }
}