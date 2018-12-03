package eu.vincinity2020.p2p_parking.ui.mylocations.locationlist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import butterknife.OnClick
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.data.entities.Trip
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.ui.mylocations.addlocation.AddLocationFragment
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity
import eu.vincinity2020.p2p_parking.ui.profile.edit.part3.EditProfile3Fragment
import eu.vincinity2020.p2p_parking.utils.AndroidUtils
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.fragment_my_bookings.*
import kotlinx.android.synthetic.main.fragment_my_location_list.*
import kotlinx.android.synthetic.main.fragment_recent_trips.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MyLocationListFragment : BaseFragment(), MyLocationListMvpView {


    @Inject
    lateinit var presenter: MyLocationListPresenter

    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withHamburger()
            .withTitle(R.string.my_location)
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_location_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMyLocations.layoutManager = LinearLayoutManager(view.context)
        val list: ArrayList<Trip> = ArrayList()
        for (i in 1..5) {
            val trip1 = Trip(i.toLong(), Date(), Date(), "ParkingSpot # " + i)
            list.add(trip1)

        }
        btnAddLocation.setOnClickListener {
            (activity as NavigationActivity).selectedFragmentTag = NavigationActivity.ADD_LOCATION_FRAGMENT
            AndroidUtils.attachFragment((activity as NavigationActivity).supportFragmentManager, AddLocationFragment(),
                    R.id.fragment_container, (activity as NavigationActivity).selectedFragmentTag)

        }
        rvMyLocations.adapter = MyLocationsAdapter(view.context, list)
    }

    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .myLocationListComponentBuilder()
                .myLocationListModule(MyLocationListModule())
                .build()
                .inject(this)

        presenter.attach(this)
        presenter.getAllLocations(34)
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

    override fun updateLocations(user: User) {
    }

//    @OnClick(R.id.btnAddLocation)
//    fun onRegisterUserButtonClicked() {
//        Navigation.findNavController(btnAddLocation).navigate(R.id.action_myLocationListFragment_to_addLocationFragment)
//    }


}