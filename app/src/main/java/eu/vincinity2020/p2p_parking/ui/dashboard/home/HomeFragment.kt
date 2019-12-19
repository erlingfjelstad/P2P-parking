package eu.vincinity2020.p2p_parking.ui.dashboard.home


import android.Manifest
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.model.TravelMode
import com.google.maps.model.Unit
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.AppConstants
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import eu.vincinity2020.p2p_parking.data.repositories.UserStopRepository
import eu.vincinity2020.p2p_parking.ui.dashboard.home.fragmnet.DirectionStatusFragment
import eu.vincinity2020.p2p_parking.ui.dashboard.home.fragmnet.DirectionStatusListener
import eu.vincinity2020.p2p_parking.utils.*
import io.nlopez.smartlocation.SmartLocation
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.uiThread
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(), OnMapReadyCallback, DirectionStatusListener {

    private var mapInstance: GoogleMap? = null
    private val allDisposables = CompositeDisposable()
    private var currentLocationMarker: Marker? = null
    private var allMarkers: ArrayList<Marker> = arrayListOf()
    private var onMapReadyAction: (() -> kotlin.Unit)? = null
    private var isMapReady = false

    private lateinit var listener: HomeListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlePermissions()
    }

    private fun handlePermissions() {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            initMap()
                        } else {
                            handlePermissions()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        MaterialDialog(requireContext(), BottomSheet()).show {
                            title(text = "Permission required")
                            message(text = "P2P needs to access your location in order to function")
                            cornerRadius(res = R.dimen.bottomsheet_corner_radius)
                            positiveButton(text = "Okay", click = {
                                token?.continuePermissionRequest()
                            })
                            negativeButton(text = "Deny & exit", click = {
                                token?.cancelPermissionRequest()
                                activity?.finishAffinity()
                            })
                            cancelable(false)
                        }
                    }

                })
                .onSameThread()
                .check()
    }

    private fun initMap() {
        val mapFragment = SupportMapFragment()
        mapFragment.getMapAsync(this)
        childFragmentManager.addFragment(R.id.mvHome, mapFragment)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mapInstance = googleMap
            isMapReady = true
            onMapReadyAction?.invoke()
        }
    }


    private fun moveCameraToLocation(location: LatLng) {
        mapInstance?.animateCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun animateMarker(marker: Marker, location: Location) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val startLatLng = marker.position
        val startRotation = marker.rotation
        val duration: Long = 500
        val interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation((elapsed.toFloat() / duration))
                val lng = (t * location.longitude + ((1 - t) * startLatLng.longitude))
                val lat = (t * location.latitude + ((1 - t) * startLatLng.latitude))
                val rotation = ((t * location.bearing + ((1 - t) * startRotation))) as Float
                marker.position = LatLng(lat, lng)
                marker.rotation = rotation
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                }
            }
        })
    }

    private fun showDirectionsOnMap(stops: ArrayList<UserStop>) {
        if (stops.isEmpty()) {
            activity?.onBackPressed()
            return
        }
        mapInstance?.clear()
        doAsync({
            runOnUiThread {
                P2PDialog.errorDialog(requireContext(), it.message?:it.localizedMessage).show()
            }
        }) {
            val currentLocation = SmartLocation.with(requireContext()).location().lastLocation
            currentLocationMarker = null
            val currentLatLng = currentLocation?.let { com.google.maps.model.LatLng(it.latitude, it.longitude) }

            if (currentLatLng != null) {
                val totalStops = stops.size

                val directionResult = if (totalStops >= 2) {
                    val waypoints = stops.map { DirectionsApiRequest.Waypoint(it.location) }
                    DirectionsApi.newRequest(getGeoContext())
                            .mode(TravelMode.DRIVING)
                            .origin(currentLatLng)
                            .waypoints(*(waypoints.toTypedArray()))
                            .destination(stops.lastOrNull()?.location)
                            .units(Unit.METRIC)
                            .await()
                } else {
                    DirectionsApi.newRequest(getGeoContext())
                            .mode(TravelMode.DRIVING)
                            .origin(currentLatLng)
                            .destination(stops.lastOrNull()?.location)
                            .units(Unit.METRIC)
                            .await()
                }


                if (directionResult?.routes?.isEmpty() == true) {
                    activity?.onBackPressed()
                    return@doAsync
                }
                val cameraUpdateFactory = if (stops.size > 2) {
                    val path = stops.subList(0, 2).map { LatLng(it.location.lat, it.location.lng) }.toMutableList()
                    path.add(0, LatLng(currentLatLng.lat, currentLatLng.lng))
                    CameraUpdateFactory.newLatLngBounds(getLatLngBounds(path), 40.px)

                } else {
                    val path = stops.map { LatLng(it.location.lat, it.location.lng) }.toMutableList()
                    path.add(0, LatLng(currentLatLng.lat, currentLatLng.lng))
                    CameraUpdateFactory.newLatLngBounds(getLatLngBounds(path), 100.px)
                }
                val decodedPath = PolyUtil.decode(directionResult?.routes?.get(0)?.overviewPolyline?.encodedPath)
                uiThread {
                    mapInstance?.addPolyline(PolylineOptions().color(Color.BLUE).addAll(decodedPath))
                    mapInstance?.animateCamera(cameraUpdateFactory, 1000, null)
                    addStopMarkersOnMap(stops)
                    //show directions in progress ui
                    val statusFragment = DirectionStatusFragment(this@HomeFragment)
                    childFragmentManager.addFragmentIfNotAlreadyAdded(R.id.frlStatusContainerHome, statusFragment)

                    var totalSeconds: Long = 0
                    directionResult?.routes?.get(0)?.legs?.forEach {
                        totalSeconds += it.duration.inSeconds
                    }
                    statusFragment.updateArrivalTime(totalSeconds.toCompoundDuration())
                    statusFragment.showDirectionsButton(stops)
                    statusFragment.showTimerButton()
                    statusFragment.showCloseButton()

                    updateMarker(currentLocation)
                    updateEta(currentLocation)
                    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
        }
    }

    private fun startMyLocationUpdateMonitoring() {
        SmartLocation.with(context)
                .location()
                .continuous()
                .start { location ->

                    //moveCameraToLocation(LatLng(location.latitude, location.longitude))
                }
    }

    private fun updateEta(location: Location) {
        allDisposables.addAll(
                UserStopRepository.getAllUserStops()
                        .subscribe { currentStops ->
                            if (currentStops.isNotEmpty()) {
                                val statusFragment = childFragmentManager.findFragmentById(R.id.frlStatusContainerHome)
                                if (currentStops != null && statusFragment != null && statusFragment is DirectionStatusFragment) {
                                    doAsync {
                                        val nextStop = currentStops.find { !it.isStopDone }
                                        val directionResult = DirectionsApi.newRequest(getGeoContext())
                                                .mode(TravelMode.DRIVING)
                                                .origin(com.google.maps.model.LatLng(location.latitude, location.longitude))
                                                .destination(nextStop?.location)
                                                .units(Unit.METRIC)
                                                .await()

                                        if (isAdded) {
                                            var totalSeconds: Long = 0
                                            directionResult?.routes?.get(0)?.legs?.forEach {
                                                totalSeconds += it.duration.inSeconds
                                            }
                                            uiThread {
                                                removeDoneStopMarkers(currentStops)
                                                txtNextStopHome?.text = nextStop?.name ?: ""
                                                statusFragment.showNextStop(nextStop?.name)
                                                statusFragment.updateArrivalTime(totalSeconds.toCompoundDuration())
                                            }

                                            if (totalSeconds * 1000 < AppConstants.ONE_MINUTE_MILLIS) {
                                                uiThread {
                                                    MaterialDialog(requireContext())
                                                            .title(text = "Arrived")
                                                            .message(text = "Do you wanna mark the current stop as done?")
                                                            .positiveButton(text = "Yes", click = {
                                                                val userStop = currentStops[currentStops.indexOfFirst { stop -> !stop.isStopDone }]
                                                                userStop.isStopDone = true
                                                                UserStopRepository.updateUserStop(userStop)
                                                                it.dismiss()
                                                            })
                                                            .negativeButton(text = "No", click = {
                                                                it.dismiss()
                                                            })
                                                            .show()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }


        )
    }

    private fun updateMarker(location: Location) {
        if (currentLocationMarker != null) {
            currentLocationMarker?.position = LatLng(location.latitude, location.longitude)
            currentLocationMarker?.let { animateMarker(it, location) }
        } else {
            currentLocationMarker = mapInstance?.addMarker(
                    MarkerOptions()
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(requireContext(), R.drawable.ic_map_car)))
                            .anchor(0.5f, 0.5f)
                            .position(LatLng(location.latitude, location.longitude))
            )
            currentLocationMarker?.let { animateMarker(it, location) }
        }
    }

    private fun addStopMarkersOnMap(stops: List<UserStop>) {
        stops.forEachIndexed { index, userStop ->
            if (!userStop.isStopDone) {
                val icon = if (index == 0) {
                    context?.let { context -> getBitmapFromVectorDrawable(context, R.drawable.ic_marker) }
                } else {
                    context?.let { context -> getBitmapFromVectorDrawable(context, R.drawable.ic_marker_red) }
                }
                mapInstance?.addMarker(
                        MarkerOptions()
                                .position(LatLng(userStop.location.lat, userStop.location.lng))
                                .title(userStop.name)
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                ).also {
                    if (it != null) {
                        allMarkers.add(it)
                    }
                }
            }
        }
    }

    private fun removeDoneStopMarkers(stops: ArrayList<UserStop>?) {
        stops?.forEach { stop ->
            if (stop.isStopDone) {
                val marker = allMarkers.find { it.title == stop.name }
                marker?.remove()
            }
        }
    }

    private fun getLatLngBounds(decodedPath: List<LatLng>): LatLngBounds {
        val boundsBuilder = LatLngBounds.builder()
        decodedPath.forEach {
            boundsBuilder.include(it)
        }
        return boundsBuilder.build()
    }

    fun showDestinations(places: ArrayList<UserStop>) {
        SmartLocation.with(context)
                .location()
                .continuous()
                .start { location ->
                    if (isMapReady && location != null) {
                        places.add(0, UserStop("My location", com.google.maps.model.LatLng(location.latitude, location.longitude), true))
                        initViewSwitcher(places)
                        addStopMarkersOnMap(places)
                        updateMarker(location)
                        updateEta(location)
                        val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(getLatLngBounds(places.map { LatLng(it.location.lat, it.location.lng) }), 20.px)
                        mapInstance?.stopAnimation()
                        mapInstance?.animateCamera(cameraUpdateFactory, 1000, null)
                    }
                }

    }

    fun showRoute() {
        if (isMapReady) {
            allDisposables.add(
                    UserStopRepository.getAllUserStops()
                            .subscribe {
                                initViewSwitcher(ArrayList(it.filter { stop -> !stop.isStopDone }))
                                mapInstance?.stopAnimation()
                                showDirectionsOnMap(ArrayList(it.filter { stop -> !stop.isStopDone }))
                            }
            )
        }
    }

    private fun initViewSwitcher(places: ArrayList<UserStop>) {
        ctlViewSwitcherHome.show()
        linListViewHome.setOnClickListener {
            if (::listener.isInitialized) {
                listener.onListViewSelected(places)
            }
        }
        txtNextStopHome.text = places.getOrNull(0)?.name ?: ""
    }

    fun setListener(listener: HomeListener) {
        this.listener = listener
    }

    fun setOnMapReadyAction(action: () -> kotlin.Unit) {
        onMapReadyAction = action
    }

    override fun onCloseDirections() {
        activity?.onBackPressed()
    }

    private fun getGeoContext(): GeoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()

    override fun onDestroy() {
        super.onDestroy()
        /*SmartLocation.with(context)
                .location()
                .stop()*/
        isMapReady = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        allDisposables.clear()
    }

}

interface HomeListener {
    fun onListViewSelected(places: List<UserStop>)
    fun closeDirections()
}