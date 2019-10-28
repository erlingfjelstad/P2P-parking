package eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.body


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
import eu.vincinity2020.p2p_parking.utils.disable
import eu.vincinity2020.p2p_parking.utils.enable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_body.*
import javax.inject.Inject

class BodyFragment : Fragment(), EditUserContract.View {

    @Inject
    lateinit var presenter: EditUserPresenter
    private lateinit var editUserListener: EditUserInterface
    private val allDisposables = CompositeDisposable()

    companion object {
        operator fun invoke(listener: EditUserInterface): BodyFragment {
            val fragment = BodyFragment()
            fragment.setListener(listener)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_body, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.get(requireContext())
                .appComponent()
                .bodyComponentBuilder()
                .bodyModule(BodyModule())
                .build()
                .inject(this)
        initViews()
    }

    private fun setListener(listener: EditUserInterface) {
        editUserListener = listener
    }

    private fun initViews() {
        allDisposables.add(
                ReactiveNetwork.observeInternetConnectivity()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isConnected ->
                            if (isConnected) {
                                btnNext_body.setBackgroundResource(R.drawable.button_background)
                                btnNext_body.setOnClickListener {
                                    if (isInputValid()) {
                                        val user = App.get(requireContext()).getUser()
                                        editUserListener.onBodyNext()
                                        presenter.updateUserRole(this, EditUserRequest(
                                                id = user?.id.toString(),
                                                gender = getSelectedGender(),
                                                disabilities = getSelectedDisabilities(),
                                                email = user?.email,
                                                mobile = user?.mobile,
                                                role = user?.role
                                        ))
                                    } else {
                                        context?.let { context -> P2PDialog.infoDialog(context, getString(R.string.edit_user_error)).show() }

                                    }
                                }
                            } else {
                                btnNext_body.setBackgroundResource(R.drawable.button_disabled_background)
                                btnNext_body.setOnClickListener {
                                    context?.let { context -> P2PDialog.infoDialog(context, getString(R.string.error_msg_no_internet)).show() }
                                }
                            }
                        }
        )
        initGenderGroup()
        initDisabilitiesGroup()
        val user = App.get(requireContext()).getUser()
        if (user != null) {
            setDefaultGender(user.gender)
            setDefaultDisability(user.disability)
        }
    }

    private fun setDefaultDisability(disability: String?) {
        when (disability) {
            "none" -> tlsNone.callOnClick()
            "wheel_chair" -> tlsWheelchair.callOnClick()
            "other" -> tlsOtherDisability.callOnClick()
        }
    }

    private fun setDefaultGender(gender: String?) {
        when (gender) {
            "Male" -> tlsMale.callOnClick()
            "Female" -> tlsFemale.callOnClick()
            "other" -> tlsOtherGender.callOnClick()
        }
    }

    private fun getSelectedDisabilities(): String {
        return when {
            tlsNone.isChosen() -> "none"
            tlsWheelchair.isChosen() -> "wheel_chair"
            tlsOtherDisability.isChosen() -> "other"
            else -> "none"
        }
    }

    private fun getSelectedGender(): String {
        return when {
            tlsMale.isChosen() -> "Male"
            tlsFemale.isChosen() -> "Female"
            tlsOtherGender.isChosen() -> "other"
            else -> "other"
        }
    }

    private fun isInputValid(): Boolean {
        if (!isGenderSelected() || !isDisabilitySelected()) {
            context?.let { context -> P2PDialog.infoDialog(context, getString(R.string.edit_user_error)).show() }
            return false
        }

        if (ckbParkingPermit.isEnabled && !ckbParkingPermit.isChecked) {
            context?.let { context -> P2PDialog.infoDialog(context, getString(R.string.parking_permit_error)).show() }
            return false
        }

        return true
    }

    private fun initGenderGroup() {
        tlsMale.setOnToggleListener {
            tlsFemale.setTileNotChosen()
            tlsOtherDisability.setTileNotChosen()
        }

        tlsFemale.setOnToggleListener {
            tlsMale.setTileNotChosen()
            tlsOtherGender.setTileNotChosen()
        }

        tlsOtherGender.setOnToggleListener {
            tlsMale.setTileNotChosen()
            tlsFemale.setTileNotChosen()
        }
    }

    private fun initDisabilitiesGroup() {
        tlsNone.setOnToggleListener {
            tlsWheelchair.setTileNotChosen()
            tlsOtherDisability.setTileNotChosen()
            ckbParkingPermit.disable()
            ckbParkingPermit.isChecked = false
        }
        tlsWheelchair.setOnToggleListener {
            tlsNone.setTileNotChosen()
            tlsOtherDisability.setTileNotChosen()
            ckbParkingPermit.enable()

        }
        tlsOtherDisability.setOnToggleListener {
            tlsNone.setTileNotChosen()
            tlsWheelchair.setTileNotChosen()
            ckbParkingPermit.enable()

        }
    }

    private fun isGenderSelected(): Boolean = tlsMale.isChosen() || tlsFemale.isChosen() || tlsOtherGender.isChosen()

    private fun isDisabilitySelected(): Boolean = tlsNone.isChosen() || tlsWheelchair.isChosen() || tlsOtherDisability.isChosen()

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun handleSuccess(message: String) {
    }

    override fun handleFailure(message: String) {
    }

    override fun onDestroy() {
        super.onDestroy()
        allDisposables.dispose()
    }

}
