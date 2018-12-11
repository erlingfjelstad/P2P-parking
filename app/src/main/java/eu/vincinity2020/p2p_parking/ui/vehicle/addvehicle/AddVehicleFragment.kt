package eu.vincinity2020.p2p_parking.ui.vehicle.addvehicle

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.OnClick
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.data.entities.Trip
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.fragment_add_vehicle.*
import kotlinx.android.synthetic.main.fragment_my_bookings.*
import kotlinx.android.synthetic.main.fragment_recent_trips.*
import kotlinx.android.synthetic.main.notification_template_lines_media.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddVehicleFragment : BaseFragment(), AddVehicleMvpView {

    @Inject
    lateinit var presenter: AddVehiclePresenter

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
            val trip1 = Trip(i.toLong(), Date(), Date(), "ParkingSpot # " + i)
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
        presenter.getAllBookingList(34)
    }


    override fun onUnknownError(errorMessage: String) {
    }

    override fun onTimeout() {
    }

    override fun onNetworkError() {
    }

    override fun onLoadFinish() {
    }

    override fun updateBookingList(user: User) {
    }


    @OnClick(R.id.tvCar)
    fun onCarSelected() {
        Toast.makeText(activity, "car clicked", Toast.LENGTH_SHORT).show()
    }

    @OnClick(R.id.tvVan)
    fun onVanSelected() {
        unselectView()
        tvVan.setBackgroundColor(ContextCompat.getColor(context!!, R.color.true_black))
        Toast.makeText(activity, "Van clicked", Toast.LENGTH_SHORT).show()
    }


    @OnClick(R.id.tvElectrical)
    fun onElectricalSelected() {
        unselectView()
        tvElectrical.setBackgroundColor(ContextCompat.getColor(context!!, R.color.true_black))
        Toast.makeText(activity, "Van clicked", Toast.LENGTH_SHORT).show()
    }



    @OnClick(R.id.tvHandicap)
    fun onHandicapSelected() {
        unselectView()
        tvHandicap.setBackgroundColor(ContextCompat.getColor(context!!, R.color.true_black))
        Toast.makeText(activity, "Van clicked", Toast.LENGTH_SHORT).show()
    }


    @OnClick(R.id.tvBike)
    fun onBikeSelected() {
        unselectView()
        tvBike.setBackgroundColor(ContextCompat.getColor(context!!, R.color.true_black))
        Toast.makeText(activity, "Van clicked", Toast.LENGTH_SHORT).show()
    }



    @OnClick(R.id.tvBlueLight)
    fun onBlueLightSelected() {
        unselectView()
        tvBlueLight.setBackgroundColor(ContextCompat.getColor(context!!, R.color.true_black))
        Toast.makeText(activity, "Van clicked", Toast.LENGTH_SHORT).show()
    }


    @OnClick(R.id.btnRegisterVehicle)
    fun validateAndSaveVehicle()
    {
        if (layoutEtDescription.text.text.trim() == "")
        {
            layoutEtDescription.error = "Please enter vehicle description."
        }
    }




    fun unselectView()
    {
        tvCar.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
        tvVan.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
        tvElectrical.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
        tvHandicap.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
        tvBike.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
        tvBlueLight.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark));
    }

}