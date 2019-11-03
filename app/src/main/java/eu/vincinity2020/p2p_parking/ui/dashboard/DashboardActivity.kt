package eu.vincinity2020.p2p_parking.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.google.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import eu.vincinity2020.p2p_parking.BuildConfig
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_EDIT_PROFILE
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_HOME
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_LOGOUT
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_LOGS
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_MY_PLACES
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_VEHICLE_LIST
import eu.vincinity2020.p2p_parking.app.common.FragmentOnBackInterface
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import eu.vincinity2020.p2p_parking.data.entities.eventbus.FirstResponderAlert
import eu.vincinity2020.p2p_parking.data.entities.eventbus.RouteUpdateAlert
import eu.vincinity2020.p2p_parking.data.repositories.UserStopRepository
import eu.vincinity2020.p2p_parking.ui.customviews.dialog.FirstResponderAlertDialog
import eu.vincinity2020.p2p_parking.ui.customviews.dialog.timer.TimerDialog
import eu.vincinity2020.p2p_parking.ui.dashboard.adapter.DashboardNavigationAdapter
import eu.vincinity2020.p2p_parking.ui.dashboard.home.HomeFragment
import eu.vincinity2020.p2p_parking.ui.dashboard.home.HomeListener
import eu.vincinity2020.p2p_parking.ui.logs.LogsFragment
import eu.vincinity2020.p2p_parking.ui.logs.LogsListener
import eu.vincinity2020.p2p_parking.ui.places.addplaces.AddPlacesFragment
import eu.vincinity2020.p2p_parking.ui.places.listplaces.PlacesListFragment
import eu.vincinity2020.p2p_parking.ui.places.listplaces.PlacesListListener
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditFinishListener
import eu.vincinity2020.p2p_parking.ui.profile.edituser.EditUserFragment
import eu.vincinity2020.p2p_parking.ui.progress.ProgressFragment
import eu.vincinity2020.p2p_parking.ui.progress.ProgressListener
import eu.vincinity2020.p2p_parking.ui.startpage.StartPageFragment
import eu.vincinity2020.p2p_parking.ui.startpage.StartPageListener
import eu.vincinity2020.p2p_parking.ui.vehiclelist.VehicleListFragment
import eu.vincinity2020.p2p_parking.utils.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.layout_dashboard.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

@SuppressLint("WrongConstant")
class DashboardActivity : AppCompatActivity(), StartPageListener, PlacesListListener,
        ProgressListener, HomeListener, EditFinishListener, LogsListener {

    private val homeFragment by lazy { HomeFragment() }
    private val startPageFragment by lazy { StartPageFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        setStatusBarColor(R.color.colorWhite)
        handlePermissions()
        setupNavigation()
        startPageFragment.setListener(this)
        supportFragmentManager.addFragment(R.id.frlFragmentContainerDashboard, startPageFragment)

        imvNavMenuDashboard.setOnClickListener {
            drlDashboard.openDrawer(Gravity.START, true)
        }

        checkForFirstResponderAlerts()
        txtVersionDashboard.text = "v${BuildConfig.VERSION_NAME}"

    }

    private fun checkForFirstResponderAlerts() {
        Handler().postDelayed({
            val alert = getFirstResponderData()
            if (alert != null) {
                onFirstResponderAlertReceived(alert)
                deleteFirstResponderAlert()
            }
        }, 1000)

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
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, startPageFragment)
            }
            NAV_EDIT_PROFILE -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, EditUserFragment(this))
            }
            NAV_VEHICLE_LIST -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, VehicleListFragment())
            }
            NAV_MY_PLACES -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, AddPlacesFragment())
            }
            "Show alert" -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, homeFragment)
                FirstResponderAlertDialog(this, FirstResponderAlert("Oslo", 43.6425701, -79.3892455)).show()
            }
            "Timer" -> {
                TimerDialog(this).show()
            }
            NAV_LOGS -> {
                supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, LogsFragment(this))
            }
            NAV_LOGOUT -> {
                logUserOut(this)
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
        UserStopRepository.insertUserStop(ArrayList(places))
        homeFragment.showRoute()
    }

    override fun onMapViewSelected(places: List<UserStop>) {
        homeFragment.setListener(this)
        supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, homeFragment)
        UserStopRepository.insertUserStop(ArrayList(places))
        homeFragment.showRoute()
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

    override fun onEditComplete() {
        supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, startPageFragment, true)
    }

    override fun closeDirections() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.addFragment(R.id.frlFragmentContainerDashboard, startPageFragment, StartPageFragment.backstackTag)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFirstResponderAlertReceived(alert: FirstResponderAlert) {
        supportFragmentManager.replaceFragmentIfNotAlreadyVisible(R.id.frlFragmentContainerDashboard, homeFragment)
        val alertDialog = FirstResponderAlertDialog(this, alert) {
            UserStopRepository.insertUserStop(arrayListOf(UserStop(alert.locationName, LatLng(alert.lat, alert.long))))
            homeFragment.showRoute()
        }
        alertDialog.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRouteUpdated(alert: RouteUpdateAlert) {
        MaterialDialog(this).show {
            title(text = "Route updated")
            message(res = R.string.route_updated_prompt)
            positiveButton(text = "Yes", click = {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                onPlacesClicked()
            })
            negativeButton(text = "No", click = {
                dismiss()
            })
            cancelable(false)
        }
    }

    private fun handlePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {

                        } else {
                            handlePermissions()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        MaterialDialog(this@DashboardActivity, BottomSheet()).show {
                            title(text = "Permission required")
                            message(text = "P2P needs to access your location in order to function")
                            cornerRadius(res = R.dimen.bottomsheet_corner_radius)
                            positiveButton(text = "Okay", click = {
                                token?.continuePermissionRequest()
                            })
                            negativeButton(text = "Deny & exit", click = {
                                token?.cancelPermissionRequest()
                                finishAffinity()
                            })
                            cancelable(false)
                        }
                    }

                })
                .onSameThread()
                .check()
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this);
    }
}


