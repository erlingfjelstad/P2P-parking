package eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments.info


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.data.entities.user.EditUserRequest
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserContract
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserInterface
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserPresenter
import eu.vincinity2020.p2p_parking.utils.P2PDialog
import eu.vincinity2020.p2p_parking.utils.gone
import eu.vincinity2020.p2p_parking.utils.show
import eu.vincinity2020.p2p_parking.utils.toWords
import kotlinx.android.synthetic.main.fragment_info.*
import org.jetbrains.anko.support.v4.runOnUiThread
import javax.inject.Inject

class InfoFragment : Fragment(), EditUserContract.View {

    @Inject
    lateinit var presenter: EditUserPresenter

    private lateinit var listener: EditUserInterface

    companion object {
        operator fun invoke(listener: EditUserInterface): InfoFragment {
            val fragment = InfoFragment()
            fragment.setListener(listener)
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.get(requireContext())
                .appComponent()
                .infoComponentBuilder()
                .infoModule(InfoModule())
                .build()
                .inject(this)
        initViews()
    }

    private fun initViews() {
        val user = App.get(requireContext()).getUser()
        if (user != null) {
            edtFullNameInfo.setText("${user.firstName} ${user.lastName}")
            edtPasswordInfo.setText(user.password)
            edtRepeatPasswordInfo.setText(user.password)
            edtEmailInfo.setText(user.email)
            edtMobileInfo.setText(user.mobile)
            edtAddressInfo.setText(user.address)
            edtZipInfo.setText(user.zip)
            edtBirthdayInfo.setText(user.birthDate)
        }

        btnUpdateInfo.setOnClickListener {
            if (isInputValid()) {
                val fullName = edtFullNameInfo.text.toString()
                val request = EditUserRequest(
                        id = user?.id.toString(),
                        firstName = fullName.toWords().filterIndexed { index, _ -> index != fullName.lastIndex }.joinToString(
                                separator = " "
                        ),
                        lastName = fullName.toWords().last(),
                        address = edtAddressInfo.text.toString(),
                        zip = edtZipInfo.text.toString(),
                        birthDate = edtBirthdayInfo.text.toString(),
                        email = user?.email,
                        mobile = user?.mobile,
                        role = user?.role
                )
                presenter.updateUserRole(this, request)
            }
        }
    }

    private fun isInputValid(): Boolean {
        if (edtFullNameInfo.text.toString().isBlank()) {
            tilFullNameInfo.error = "Field cannot be left blank"
            return false
        } else {
            tilFullNameInfo.isErrorEnabled = false
        }

        if (edtPasswordInfo.text.toString().isBlank()) {
            tilPasswordInfo.error = "Field cannot be left blank"
            return false
        } else {
            tilPasswordInfo.isErrorEnabled = false
        }

        if (edtRepeatPasswordInfo.text.toString().isBlank()) {
            tilRepeatPasswordInfo.error = "Field cannot be left blank"
            return false
        } else {
            tilRepeatPasswordInfo.isErrorEnabled = false
        }

        if (edtEmailInfo.text.toString().isBlank()) {
            tilEmailInfo.error = "Field cannot be left blank"
            return false
        } else {
            tilEmailInfo.isErrorEnabled = false
        }

        if (edtMobileInfo.text.toString().isBlank()) {
            tilMobileInfo.error = "Field cannot be left blank"
            return false
        } else {
            tilMobileInfo.isErrorEnabled = false
        }

        if (edtAddressInfo.text.toString().isBlank()) {
            tilAddressInfo.error = "Field cannot be left blank"
            return false
        } else {
            tilAddressInfo.isErrorEnabled = false
        }

        if (edtZipInfo.text.toString().isBlank()) {
            tilZipInfo.error = "Field cannot be left blank"
            return false
        } else {
            tilZipInfo.isErrorEnabled = false
        }

        if (edtBirthdayInfo.text.toString().isBlank()) {
            tilBirthdayInfo.error = "Field cannot be left blank"
            return false
        } else {
            tilBirthdayInfo.isErrorEnabled = false
        }

        if (edtPasswordInfo.text.toString() != edtRepeatPasswordInfo.text.toString()) {
            tilPasswordInfo.error = "Passwords do not match"
            tilRepeatPasswordInfo.error = "Passwords do not match"
            return false
        } else {
            tilPasswordInfo.isErrorEnabled = false
            tilRepeatPasswordInfo.isErrorEnabled = false
        }

        return true
    }

    private fun setListener(listener: EditUserInterface) {
        this.listener = listener
    }

    override fun showProgress() {
        runOnUiThread {
            pbInfo.show()
        }
    }

    override fun hideProgress() {
        runOnUiThread {
            pbInfo.gone()
        }
    }

    override fun handleSuccess(message: String) {
        listener.onInfoComplete()
    }

    override fun handleFailure(message: String) {
        context?.let { context -> P2PDialog.infoDialog(context, message).show() }
    }


}
