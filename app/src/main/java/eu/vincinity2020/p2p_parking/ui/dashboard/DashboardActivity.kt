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
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_VEHICLE_LIST
import eu.vincinity2020.p2p_parking.app.common.routingDummyData
import eu.vincinity2020.p2p_parking.ui.customviews.dialog.FirstResponderAlertDialog
import eu.vincinity2020.p2p_parking.ui.customviews.dialog.timer.TimerDialog
import eu.vincinity2020.p2p_parking.ui.dashboard.adapter.DashboardNavigationAdapter
import eu.vincinity2020.p2p_parking.ui.dashboard.home.HomeFragment
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserFragment
import eu.vincinity2020.p2p_parking.ui.vehiclelist.VehicleListFragment
import eu.vincinity2020.p2p_parking.utils.addFragment
import eu.vincinity2020.p2p_parking.utils.replaceFragmentIfNotAlreadyVisible
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.layout_dashboard.*

@SuppressLint("WrongConstant")
class DashboardActivity: AppCompatActivity() {

    private val homeFragment by lazy { HomeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initViews()
    }

    private fun initViews() {
        setStatusBarColor(R.color.colorWhite)
        setupNavigation()

        supportFragmentManager.addFragment(R.id.frlFragmentContainerDashboard, homeFragment)

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
            "Show alert" -> {
                FirstResponderAlertDialog(this).show()
            }
            "Routing sample" -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, homeFragment)
                Handler().postDelayed({ homeFragment.showDirectionsOnMap(routingDummyData) }, 1000)

            }
            "Timer" ->{
                TimerDialog(this).show()
            }
        }
    }

    private fun setStatusBarColor(@ColorRes color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
        window.navigationBarColor = ContextCompat.getColor(this, color)
    }
}


