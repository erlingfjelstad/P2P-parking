package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.data.entities.Trip
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.data.entities.Vehicles
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity
import eu.vincinity2020.p2p_parking.ui.profile.edit.part3.EditProfile3Fragment
import eu.vincinity2020.p2p_parking.ui.vehicle.addvehicle.AddVehicleFragment
import eu.vincinity2020.p2p_parking.utils.AndroidUtils
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.fragment_my_bookings.*
import kotlinx.android.synthetic.main.fragment_recent_trips.*
import kotlinx.android.synthetic.main.fragment_view_vehicle_list.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class VehicleListFragment : BaseFragment(), VehicleListMvpView {

    @Inject
    lateinit var presenter: VehicleListPresenter

    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withHamburger()
            .withTitle(R.string.registered_vehicles)
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_vehicle_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvVehicles.layoutManager = LinearLayoutManager(view.context)
        val list: ArrayList<Vehicles> = ArrayList()
        for (i in 1..5) {
            val trip1 = Vehicles(i.toLong(), "Name_1", "lastname_1", "asd@mailinator.com", "9138724362")
            list.add(trip1)

        }
        rvVehicles.adapter = MyVehiclesAdapter(view.context, list)
    }

    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .viewVehiclesComponentBuilder()
                .viewVehiclesModule(VehicleListModule())
                .build()
                .inject(this)

        presenter.attach(this)
        presenter.getAllVehicleList(34)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onUnknownError(errorMessage: String) {
    }

    override fun onTimeout() {
    }

    override fun onNetworkError() {
    }

    override fun onLoadFinish() {
    }

    override fun updateVehicleList(vehicle: Vehicles) {

    }


    @OnClick(R.id.btnAddVehicle)
    fun openAddVehicleFragment() {
        (activity as NavigationActivity).selectedFragmentTag = NavigationActivity.ADD_VEHICLE_FRAGMENT
        AndroidUtils.attachFragment((activity as NavigationActivity).supportFragmentManager, AddVehicleFragment(),
                R.id.fragment_container, (activity as NavigationActivity).selectedFragmentTag)
    }

}