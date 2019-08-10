package eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserInterface
import eu.vincinity2020.p2p_parking.utils.P2PDialog
import kotlinx.android.synthetic.main.fragment_role.*

class RoleFragment : Fragment() {

    private lateinit var editUserListener: EditUserInterface

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

        initView()
    }

    private fun setListener(listener: EditUserInterface) {
        editUserListener = listener
    }

    private fun initView() {

        initUserTypeGroup()

        initParkingPreferenceGroup()

        btnNext_role.setOnClickListener {
            if (isInputValid()) {
                editUserListener.onRoleNext()
            } else {
                context?.let { context -> P2PDialog.infoDialog(context, getString(R.string.edit_user_error)).show() }
            }
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

}
