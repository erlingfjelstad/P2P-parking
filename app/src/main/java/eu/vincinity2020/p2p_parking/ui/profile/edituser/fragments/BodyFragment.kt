package eu.vincinity2020.p2p_parking.ui.profile.edituser.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserInterface
import eu.vincinity2020.p2p_parking.utils.P2PDialog
import eu.vincinity2020.p2p_parking.utils.disable
import eu.vincinity2020.p2p_parking.utils.enable
import kotlinx.android.synthetic.main.fragment_body.*

class BodyFragment : Fragment() {

    private lateinit var editUserListener: EditUserInterface

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

        initViews()
    }

    private fun setListener(listener: EditUserInterface) {
        editUserListener = listener
    }

    private fun initViews() {
        initGenderGroup()
        initDisabilitiesGroup()

        btnNext_body.setOnClickListener {
            if (isInputValid()) {
                editUserListener.onBodyNext()
            }
        }
    }

    private fun isInputValid(): Boolean {
        if (!isGenderSelected() || !isDisabilitySelected()) {
            context?.let { context -> P2PDialog.infoDialog(context, getString(R.string.edit_user_error)).show() }
            return false
        }

        if(ckbParkingPermit.isEnabled && !ckbParkingPermit.isChecked){
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


}
