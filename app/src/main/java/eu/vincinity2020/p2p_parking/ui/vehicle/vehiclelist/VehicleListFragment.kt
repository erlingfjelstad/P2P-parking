package eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist

import android.os.Bundle
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

    val vehicles: ArrayList<Vehicles> = ArrayList()

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
        rvVehicles.adapter = MyVehiclesAdapter(view.context, vehicles)

    }

    override fun onResume() {
        super.onResume()
        val appVar=
        App.get(requireContext())

                appVar.appComponent()
                .viewVehiclesComponentBuilder()
                .viewVehiclesModule(VehicleListModule())
                .build()
                .inject(this)

        presenter.attach(this)

        progress.visibility = View.VISIBLE
        presenter.getVehicles(appVar.getUser()!!.id, appVar.getUser()!!.email, appVar.getUser()!!.password)


    }

    override fun onPause() {
        super.onPause()
    }

    override fun onUnknownError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        progress.visibility = View.GONE

    }

    override fun onTimeout() {
        Toast.makeText(context, resources.getString(R.string.unable_to_connect_to_server), Toast.LENGTH_SHORT).show()
        progress.visibility = View.GONE
    }

    override fun onNetworkError() {
        Toast.makeText(context, resources.getString(R.string.unable_to_connect_to_server), Toast.LENGTH_SHORT).show()
        progress.visibility = View.GONE
    }

    override fun onLoadFinish() {
        progress.visibility = View.GONE
    }

    override fun updateVehicleList(allVehicles: ArrayList<Vehicles>) {
        vehicles.clear()
        vehicles.addAll(allVehicles)
        rvVehicles.adapter?.notifyDataSetChanged()
        progress.visibility = View.GONE
    }


    @OnClick(R.id.btnAddVehicle)
    fun openAddVehicleFragment() {
        (activity as NavigationActivity).selectedFragmentTag = NavigationActivity.ADD_VEHICLE_FRAGMENT
        AndroidUtils.attachFragment((activity as NavigationActivity).supportFragmentManager, AddVehicleFragment(),
                R.id.fragment_container, (activity as NavigationActivity).selectedFragmentTag, true)
    }

}