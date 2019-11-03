package eu.vincinity2020.p2p_parking.ui.navigation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import eu.vincinity2020.p2p_parking.ui.auth.login.LoginActivity
import eu.vincinity2020.p2p_parking.ui.bookings.ViewBookingsFragment
import eu.vincinity2020.p2p_parking.ui.map.MapFragment
import eu.vincinity2020.p2p_parking.ui.mylocations.locationlist.MyLocationListFragment
import eu.vincinity2020.p2p_parking.ui.profile.view.ViewProfileFragment
import eu.vincinity2020.p2p_parking.ui.vehicle.vehiclelist.VehicleListFragment
import eu.vincinity2020.p2p_parking.utils.AndroidUtils
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    //    default behaviour
     var selectedFragmentTag: String = MAP_FRAGMENT

    companion object {
        const val ARG_SELECTED_FRAGMENT = "state:SelectedFragment"


        // individual fragment tags
        const val MAP_FRAGMENT = "MapFragment"
        const val TRIP_FRAGMENT = "TripFragment"
        const val PROFILE_FRAGMENT = "ProfileFragment"
        const val VIEW_BOOKINGS_FRAGMENT = "ViewBookingFragment"

        // Edit profile fragment tags
        const val EDIT_PROFILE_1 = "EditProfile1Frag"
        const val EDIT_PROFILE_2 = "EditProfile2Frag"
        const val EDIT_PROFILE_3 = "EditProfile3Frag"

        //My Locations fragments tags
        const val LOCATION_LIST_FRAGMENT = "LocationListFrag"
        const val ADD_LOCATION_FRAGMENT = "AddLocationFrag"
        const val VIEW_LOCATION_FRAGMENT = "ViewLocationFrag"


        //Vehicles fragment tags
        const val ADD_VEHICLE_FRAGMENT = "AddVehicleFragment"
        const val VEHICLE_LIST_FRAGMENT = "VehicleListFrag"



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_navigation)

        // handling fragment recovery

        val fragment: Fragment

        if (savedInstanceState == null) {
            fragment = MapFragment()
            AndroidUtils.attachFragment(supportFragmentManager, fragment,
                    R.id.fragment_container, MAP_FRAGMENT, false)
        } else {
            fragment = when (selectedFragmentTag) {
                MAP_FRAGMENT -> {
                    MapFragment()
                }
                TRIP_FRAGMENT -> {
                    MapFragment()
                }
                LOCATION_LIST_FRAGMENT -> {
                    MyLocationListFragment()
                }
                PROFILE_FRAGMENT -> {
                    ViewProfileFragment()
                }
                else -> {
                    MapFragment()
                }
            }

            val retainedFragment = supportFragmentManager.findFragmentByTag(selectedFragmentTag)

            if (retainedFragment != null) {
                AndroidUtils.attachFragment(supportFragmentManager, retainedFragment,
                        R.id.fragment_container, selectedFragmentTag, false)
            } else {
                AndroidUtils.attachFragment(supportFragmentManager, fragment,
                        R.id.fragment_container, selectedFragmentTag, false)
            }

        }


     nav_view.setNavigationItemSelectedListener(this@NavigationActivity)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putString(ARG_SELECTED_FRAGMENT, selectedFragmentTag)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            selectedFragmentTag = it.getString(ARG_SELECTED_FRAGMENT, MAP_FRAGMENT)
        }
    }



    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val fragment: Fragment? = when (menuItem.itemId) {
            R.id.menu_home -> {
                selectedFragmentTag = MAP_FRAGMENT
                MapFragment()
            }
            R.id.menu_registered_vehicles-> {
                selectedFragmentTag = VEHICLE_LIST_FRAGMENT
                VehicleListFragment()
            }
            R.id.menu_my_locations -> {
                selectedFragmentTag = LOCATION_LIST_FRAGMENT
                MyLocationListFragment()
            }
            R.id.menu_view_profile-> {
                selectedFragmentTag = PROFILE_FRAGMENT
                ViewProfileFragment()
            }
            R.id.menu_my_bookings->{
                selectedFragmentTag = VIEW_BOOKINGS_FRAGMENT
                ViewBookingsFragment()
            }
            R.id.menu_log_out-> {
                App.get(this@NavigationActivity).setIsLoggedIn(false)
                startActivity(Intent(this@NavigationActivity, LoginActivity::class.java))
                finish()
                null

            }
            else ->
            {
                null
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        fragment?.let {
            AndroidUtils.attachFragment(supportFragmentManager, it,
                    R.id.fragment_container, selectedFragmentTag, false)
        }
        return true
    }
}