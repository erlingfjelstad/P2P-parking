package eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.role


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.data.entities.user.EditUserRequest
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserContract
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserInterface
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserPresenter
import eu.vincinity2020.p2p_parking.utils.P2PDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_role.*
import javax.inject.Inject

class RoleFragment : Fragment(), EditUserContract.View {

    @Inject
    lateinit var presenter: EditUserPresenter
    private lateinit var editUserListener: EditUserInterface
    private val allDisposables = CompositeDisposable()

    companion object {
        operator fun invoke(listener: EditUserInterface): RoleFragment {
            val fragment = RoleFragment()
            fragment.setListener(listener)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_role, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.get(requireContext())
                .appComponent()
                .roleComponentBuilder()
                .roleModule(RoleModule())
                .build()
                .inject(this)
        initView()
    }

    private fun setListener(listener: EditUserInterface) {
        editUserListener = listener
    }

    private fun initView() {

        allDisposables.add(
                ReactiveNetwork.observeInternetConnectivity()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isConnected ->
                            if (isConnected) {
                                btnNext_role.setBackgroundResource(R.drawable.button_background)
                                btnNext_role.setOnClickListener {
                                    if (isInputValid()) {
                                        val user = App.get(requireContext()).getUser()
                                        editUserListener.onRoleNext()
                                        presenter.updateUserRole(this, EditUserRequest(
                                                id = user?.id.toString(),
                                                userType = getSelectedUserType(),
                                                parkingPreference = getSelectedParkingPreference(),
                                                email = user?.email,
                                                mobile = user?.mobile,
                                                role = user?.role
                                        ))
                                    } else {
                                        context?.let { context -> P2PDialog.infoDialog(context, getString(R.string.edit_user_error)).show() }
                                    }
                                }
                            } else {
                                btnNext_role?.setBackgroundResource(R.drawable.button_disabled_background)
                                btnNext_role?.setOnClickListener {
                                    context?.let { context -> P2PDialog.infoDialog(context, getString(R.string.error_msg_no_internet)).show() }
                                }
                            }
                        }
        )

        initUserTypeGroup()

        initParkingPreferenceGroup()
        val user = App.get(requireContext()).getUser()
        if (user != null) {
            setDefaultUserType(user.userType)
            setDefaultParkingPreference(user.parkingPreference)
        }

    }

    private fun setDefaultUserType(userType: String?) {
        when (userType) {
            "driver" -> tlsDriver.callOnClick()
            "owner" -> tlsOwner.callOnClick()
            "health" -> tlsHealth.callOnClick()
        }
    }

    private fun setDefaultParkingPreference(parkingPreference: String?) {
        when (parkingPreference) {
            "normal" -> tlsNormal.callOnClick()
            "electrical" -> tlsElectrical.callOnClick()
            "handicap" -> tlsHandicap.callOnClick()
        }
    }

    private fun getSelectedParkingPreference(): String {
        return when {
            tlsNormal.isChosen() -> "normal"
            tlsElectrical.isChosen() -> "electrical"
            tlsHandicap.isChosen() -> "handicap"
            else -> "normal"
        }
    }

    private fun getSelectedUserType(): String {
        return when {
            tlsDriver.isChosen() -> "driver"
            tlsOwner.isChosen() -> "owner"
            tlsHealth.isChosen() -> "health"
            else -> "driver"
        }
    }

    private fun isInputValid(): Boolean {
        if (!isUserTypeSelected()) {
            return false
        }

        if (!isParkingPreferenceSelected()) {
            return false
        }

        return true
    }

    private fun initUserTypeGroup() {

        tlsDriver.setOnToggleListener {
            tlsOwner.setTileNotChosen()
            tlsHealth.setTileNotChosen()
        }

        tlsOwner.setOnToggleListener {
            tlsDriver.setTileNotChosen()
            tlsHealth.setTileNotChosen()
        }

        tlsHealth.setOnToggleListener {
            tlsDriver.setTileNotChosen()
            tlsOwner.setTileNotChosen()
        }

    }

    private fun initParkingPreferenceGroup() {
        tlsNormal.setOnToggleListener {
            tlsElectrical.setTileNotChosen()
            tlsHandicap.setTileNotChosen()
        }

        tlsElectrical.setOnToggleListener {
            tlsNormal.setTileNotChosen()
            tlsHandicap.setTileNotChosen()
        }

        tlsHandicap.setOnToggleListener {
            tlsNormal.setTileNotChosen()
            tlsElectrical.setTileNotChosen()
        }
    }

    private fun isUserTypeSelected(): Boolean =
            tlsDriver.isChosen() || tlsOwner.isChosen() || tlsHealth.isChosen()

    private fun isParkingPreferenceSelected(): Boolean =
            tlsNormal.isChosen() || tlsElectrical.isChosen() || tlsHandicap.isChosen()

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun handleSuccess(message: String) {
    }

    override fun handleFailure(message: String) {
        //context?.let { context -> P2PDialog.infoDialog(context, message).show() }
    }

    override fun onDestroy() {
        super.onDestroy()
        allDisposables.dispose()
        presenter.detach()
    }

}
