package eu.vincinity2020.p2p_parking.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
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
import eu.vincinity2020.p2p_parking.ui.dashboard.adapter.DashboardNavigationAdapter
import eu.vincinity2020.p2p_parking.ui.dashboard.home.HomeFragment
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserFragment
import eu.vincinity2020.p2p_parking.utils.addFragment
import eu.vincinity2020.p2p_parking.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.layout_dashboard.*

@SuppressLint("WrongConstant")
class DashboardActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initViews()
    }

    private fun initViews() {
        setStatusBarColor(R.color.colorWhite)
        setupNavigation()

        supportFragmentManager.addFragment(R.id.frlFragmentContainerDashboard, HomeFragment())

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
                supportFragmentManager.replaceFragment(R.id.frlFragmentContainerDashboard, HomeFragment())
            }
            NAV_EDIT_PROFILE -> {
                supportFragmentManager.replaceFragment(R.id.frlFragmentContainerDashboard, EditUserFragment())
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


