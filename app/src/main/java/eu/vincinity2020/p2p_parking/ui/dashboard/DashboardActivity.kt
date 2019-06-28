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
import eu.vincinity2020.p2p_parking.ui.auth.registeruser.adapter.NavigationAdapter
import eu.vincinity2020.p2p_parking.ui.dashboard.home.HomeFragment
import eu.vincinity2020.p2p_parking.utils.addFragment
import eu.vincinity2020.p2p_parking.utils.addFragmentIfNotAlreadyAdded
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
        val adapter = NavigationAdapter(AppConstants.homeNavigationItems) { position ->
            navigateTo(position)
            drlDashboard.closeDrawer(Gravity.START, true)
        }

        revNavigationDashboard.layoutManager = LinearLayoutManager(this)
        revNavigationDashboard.adapter = adapter
    }

    private fun navigateTo(position: Int) {
        when (AppConstants.homeNavigationItems[position]) {
            AppConstants.NAV_HOME -> {
                supportFragmentManager.addFragmentIfNotAlreadyAdded(R.id.frlFragmentContainerDashboard, HomeFragment())
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
