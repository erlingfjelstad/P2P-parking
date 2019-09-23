package eu.vincinity2020.p2p_parking.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_EDIT_PROFILE
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_HOME
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_MY_PLACES
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_VEHICLE_LIST
import eu.vincinity2020.p2p_parking.app.common.FragmentOnBackInterface
import eu.vincinity2020.p2p_parking.app.common.routingDummyData
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import eu.vincinity2020.p2p_parking.ui.customviews.dialog.FirstResponderAlertDialog
import eu.vincinity2020.p2p_parking.ui.customviews.dialog.timer.TimerDialog
import eu.vincinity2020.p2p_parking.ui.dashboard.adapter.DashboardNavigationAdapter
import eu.vincinity2020.p2p_parking.ui.dashboard.home.HomeFragment
import eu.vincinity2020.p2p_parking.ui.dashboard.home.HomeListener
import eu.vincinity2020.p2p_parking.ui.places.addplaces.AddPlacesFragment
import eu.vincinity2020.p2p_parking.ui.places.listplaces.PlacesListFragment
import eu.vincinity2020.p2p_parking.ui.places.listplaces.PlacesListListener
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserFragment
import eu.vincinity2020.p2p_parking.ui.progress.ProgressFragment
import eu.vincinity2020.p2p_parking.ui.progress.ProgressListener
import eu.vincinity2020.p2p_parking.ui.startpage.StartPageFragment
import eu.vincinity2020.p2p_parking.ui.startpage.StartPageListener
import eu.vincinity2020.p2p_parking.ui.vehiclelist.VehicleListFragment
import eu.vincinity2020.p2p_parking.utils.addFragment
import eu.vincinity2020.p2p_parking.utils.replaceFragmentIfNotAlreadyVisible
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.layout_dashboard.*
import org.jetbrains.anko.toast

@SuppressLint("WrongConstant")
class DashboardActivity : AppCompatActivity(), StartPageListener, PlacesListListener,
        ProgressListener, HomeListener {

    private val homeFragment by lazy { HomeFragment() }
    private val startPageFragment by lazy { StartPageFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initViews()
    }

    private fun initViews() {
        setStatusBarColor(R.color.colorWhite)
        setupNavigation()
        startPageFragment.setListener(this)
        supportFragmentManager.addFragment(R.id.frlFragmentContainerDashboard, startPageFragment)

        imvNavMenuDashboard.setOnClickListener {
            drlDashboard.openDrawer(Gravity.START, true)
        }
    }

    private fun setupNavigation() {
        val adapter = DashboardNavigationAdapter(AppConstants.dashboardNavigationItems) { position ->
            navigateTo(position)
            drlDashboard.closeDrawer(Gravity.START, true)
        }

        revNavigationDashboard.layoutManager = LinearLayoutManager(this)
        revNavigationDashboard.adapter = adapter
    }

    private fun navigateTo(position: Int) {
        when (AppConstants.dashboardNavigationItems[position].first) {
            NAV_HOME -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, homeFragment)
            }
            NAV_EDIT_PROFILE -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, EditUserFragment())
            }
            NAV_VEHICLE_LIST -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, VehicleListFragment())
            }
            NAV_MY_PLACES -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, AddPlacesFragment())
            }
            "Show alert" -> {
                FirstResponderAlertDialog(this).show()
            }
            "Routing sample" -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, homeFragment)
                Handler().postDelayed({ homeFragment.showDirectionsOnMap(routingDummyData) }, 1000)

            }
            "Timer" -> {
                TimerDialog(this).show()
            }
        }
    }

    override fun onPlacesClicked() {
        supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, PlacesListFragment.newInstance(this), true)
    }

    override fun onProgressClicked() {
        toast("Coming Soon")
    }

    override fun onSettingsClicked() {
        toast("Coming Soon")
    }

    override fun onBeginVisitClicked() {
        toast("Coming Soon")
    }

    override fun onSearchClicked() {
        supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, AddPlacesFragment(), true)
    }

    override fun onSetDestination(places: List<UserStop>) {
        homeFragment.setListener(this)
        supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, homeFragment, true)
        homeFragment.showDestinations(ArrayList(places))

    }

    override fun onShowRoute(places: List<UserStop>) {
        homeFragment.setListener(this)
        supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, homeFragment, true)
        homeFragment.showRoute(ArrayList(places))
    }

    override fun onMapViewSelected(places: List<UserStop>) {
        homeFragment.setListener(this)
        supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, homeFragment)
        homeFragment.showRoute(ArrayList(places))
    }

    override fun onListViewSelected(places: List<UserStop>) {
        supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, ProgressFragment.newInstance(ArrayList(places), this))
    }

    private fun setStatusBarColor(@ColorRes color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
        window.navigationBarColor = ContextCompat.getColor(this, color)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frlFragmentContainerDashboard)
        if ((fragment as? FragmentOnBackInterface) == null) {
            super.onBackPressed()
        } else {
            (fragment as? FragmentOnBackInterface)?.onBackPressed()?.not()?.let {
                if (it) {
                    super.onBackPressed()
                }
            }
        }
    }
}


