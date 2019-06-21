package eu.vincinity2020.p2p_parking.ui.vehicle.addvehicle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import butterknife.OnClick
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.data.entities.Trip
import eu.vincinity2020.p2p_parking.data.entities.VehicleTypes
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.fragment_add_vehicle.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddVehicleFragment : BaseFragment(), AddVehicleMvpView {


    @Inject
    lateinit var presenter: AddVehiclePresenter

    lateinit var vehicleType : VehicleTypes

    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withBackButton()
            .withTitle(R.string.add_vehicle)
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_vehicle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list: ArrayList<Trip> = ArrayList()
        for (i in 1..5) {
            val trip1 = Trip(i.toLong(), Date(), Date(), "ParkingSensor # " + i)
            list.add(trip1)

        }


    }

    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .addVehicleComponentBuilder()
                .addVehicleModule(AddVehicleModule())
                .build()
                .inject(this)


        presenter.attach(this)
        if (context != null ) {
            presenter.getVehicleTypes(App.get(context!!).getUser()!!.id)
            scrollContainer.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }
    }


    override fun onUnknownError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        scrollContainer.visibility = View.VISIBLE
        progress.visibility = View.GONE
    }

    override fun onTimeout() {
        Toast.makeText(context, resources.getString(R.string.unable_to_connect_to_server), Toast.LENGTH_SHORT).show()
        scrollContainer.visibility = View.VISIBLE
        progress.visibility = View.GONE
    }

    override fun onNetworkError() {
        Toast.makeText(context, resources.getString(R.string.unable_to_connect_to_server), Toast.LENGTH_SHORT).show()
        scrollContainer.visibility = View.VISIBLE
        progress.visibility = View.GONE
    }

    override fun onLoadFinish() {
        scrollContainer.visibility = View.VISIBLE
        progress.visibility = View.GONE

    }

    override fun onVehicleAdded(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        scrollContainer.visibility = View.VISIBLE
        progress.visibility = View.GONE
        activity!!.onBackPressed()
    }


    override fun updateVehicleTypes(vehicleTypes: ArrayList<VehicleTypes>) {

    }



    @OnClick(R.id.tvMale)
    fun onCarSelected() {
        unselectView()
        tvMale.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorBlack))
        Toast.makeText(activity, "car clicked", Toast.LENGTH_SHORT).show()
        vehicleType = VehicleTypes(1, "car",null)
    }

    @OnClick(R.id.tvOwner)
    fun onVanSelected() {
        unselectView()
        tvOwner.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorBlack))
        Toast.makeText(activity, "Van clicked", Toast.LENGTH_SHORT).show()
        vehicleType = VehicleTypes(2, "Van",null)
    }


    @OnClick(R.id.tvElectrical)
    fun onElectricalSelected() {
        unselectView()
        tvElectrical.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorBlack))
        Toast.makeText(activity, "EV clicked", Toast.LENGTH_SHORT).show()
        vehicleType = VehicleTypes(3, "EV",null)
    }



    @OnClick(R.id.tvNormal)
    fun onHandicapSelected() {
        unselectView()
        tvNormal.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorBlack))
        Toast.makeText(activity, "Handicap clicked", Toast.LENGTH_SHORT).show()
        vehicleType = VehicleTypes(4, "Handicap",null)

    }


    @OnClick(R.id.tvElectrical)
    fun onBikeSelected() {
        unselectView()
        tvElectrical.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorBlack))
        Toast.makeText(activity, "Bike clicked", Toast.LENGTH_SHORT).show()
        vehicleType = VehicleTypes(5, "Bike",null)

    }



    @OnClick(R.id.tvHandicap)
    fun onBlueLightSelected() {
        unselectView()
        tvHandicap.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorBlack))
        Toast.makeText(activity, "Ambulance clicked", Toast.LENGTH_SHORT).show()
        vehicleType = VehicleTypes(6, "Ambulance",null)

    }


    @OnClick(R.id.btnCancel)
    fun onCancelClicked()
    {
        activity!!.onBackPressed()
    }

    @OnClick(R.id.btnRegisterVehicle)
    fun validateAndSaveVehicle()
    {
        if (layoutEtDescription.editText?.text?.trim() == "")
        {
            layoutEtDescription.error = "Please enter vehicle description."
            layoutEtDescription.isErrorEnabled = true
            layoutEtDescription.requestFocus()
        } else if (layoutEtBrand.editText?.text?.trim() == "")
        {
            layoutEtBrand.error = "Please enter vehicle brand."
            layoutEtBrand.isErrorEnabled = true
            layoutEtBrand.requestFocus()
        }

    else if (layoutEtName.editText?.text?.trim() == "")
        {
            layoutEtName.error = "Please enter model name."
            layoutEtName.isErrorEnabled = true
            layoutEtName.requestFocus()
        }
         else if (layoutEtRegNo.editText?.text?.trim() == "")
        {
            layoutEtRegNo.error = "Please enter vehicle registration number."
            layoutEtRegNo.isErrorEnabled = true
            layoutEtRegNo.requestFocus()
        }
        else{

            presenter.registerNewVehicle(App.get(context!!).getUser()!!.email, App.get(context!!).getUser()!!.password, App.get(context!!).getUser()!!.id,
                    etBrand.text.toString(), etName.text.toString(), etDescription.text.toString(), etRegNo.text.toString(), etMfgYr.text.toString(),
                    vehicleType, etHeight.text.toString(), etWidth.text.toString(), etLength.text.toString(), etWeight.text.toString(), spFuel.selectedItem as String)
        }


    }




    fun unselectView()
    {
        tvMale.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
        tvOwner.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
        tvElectrical.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
        tvNormal.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
        tvElectrical.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
        tvHandicap.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
    }

}