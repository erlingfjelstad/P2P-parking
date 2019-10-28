package eu.vincinity2020.p2p_parking.ui.dashboard.home


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.app.ActivityCompat
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
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import eu.vincinity2020.p2p_parking.ui.dashboard.home.fragmnet.DirectionStatusFragment
import eu.vincinity2020.p2p_parking.ui.dashboard.home.fragmnet.DirectionStatusListener
import eu.vincinity2020.p2p_parking.utils.*
import io.nlopez.smartlocation.SmartLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(), OnMapReadyCallback, DirectionStatusListener {

    private var mapInstance: GoogleMap? = null
    private val allDisposables = CompositeDisposable()
    private var mapReadyObservable = PublishSubject.create<Boolean>()
    private var shouldShowCurrentLocation = true
    private var currentLocation: Location? = null
    private var locationObservable = PublishSubject.create<Location?>()

    private lateinit var listener: HomeListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlePermissions()
        mapReadyObservable.onNext(false)
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
            moveCameraToCurrentLocation(googleMap)
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.uiSettings.isMyLocationButtonEnabled = true
            }
        }
        mapReadyObservable.onNext(true)
    }


    private fun moveCameraToCurrentLocation(map: GoogleMap) {
        SmartLocation.with(context)
                .location()
                .oneFix()
                .start { location ->
                    currentLocation = location
                    locationObservable.onNext(location)
                    if (shouldShowCurrentLocation) {
                        map.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 17f)
                        )
                    }
                }
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

    fun showDirectionsOnMap(stops: ArrayList<UserStop>) {
        mapInstance?.clear()
        doAsync {
            val location = SmartLocation.with(context)
                    .location()
                    .lastLocation

            if (location != null) {
                val totalStops = stops.size

                val directionResult = if (totalStops >= 3) {
                    val waypoints = stops.map { DirectionsApiRequest.Waypoint(it.location) }
                    DirectionsApi.newRequest(getGeoContext())
                            .mode(TravelMode.DRIVING)
                            .origin(com.google.maps.model.LatLng(location.latitude, location.longitude))
                            .waypoints(*(waypoints.toTypedArray()))
                            .destination(stops[stops.size - 1].location)
                            .units(Unit.METRIC)
                            .await()
                } else {
                    DirectionsApi.newRequest(getGeoContext())
                            .mode(TravelMode.DRIVING)
                            .origin(com.google.maps.model.LatLng(location.latitude, location.longitude))
                            .destination(stops[stops.size - 1].location)
                            .units(Unit.METRIC)
                            .await()
                }


                if (directionResult?.routes?.isEmpty() == true) {
                    return@doAsync
                }
                val cameraUpdateFactory = if (stops.size > 2) {
                    val path = stops.subList(0, 2).map { LatLng(it.location.lat, it.location.lng) }.toMutableList()
                    path.add(0, LatLng(location.latitude, location.longitude))
                    CameraUpdateFactory.newLatLngBounds(getLatLngBounds(path), 40.px)

                } else {
                    val path = stops.map { LatLng(it.location.lat, it.location.lng) }.toMutableList()
                    path.add(0, LatLng(location.latitude, location.longitude))
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

                    startMyLocationIconUpdate()
                }
            }
        }
    }

    private fun startMyLocationIconUpdate() {
        SmartLocation.with(context)
                .location()
                .continuous()
                .start { location ->
                    val marker = mapInstance?.addMarker(
                            MarkerOptions()
                                    .flat(true)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(requireContext(), R.drawable.ic_map_car)))
                                    .anchor(0.5f, 0.5f)
                                    .position(LatLng(location.latitude, location.longitude))
                    )
                    marker?.let { animateMarker(it, location) }
                }
    }

    private fun addStopMarkersOnMap(stops: List<UserStop>) {
        stops.forEachIndexed { index, userStop ->
            val icon = if (index == 0) {
                context?.let { context -> getBitmapFromVectorDrawable(context, R.drawable.ic_marker_red) }
            } else {
                context?.let { context -> getBitmapFromVectorDrawable(context, R.drawable.ic_marker) }
            }
            mapInstance?.addMarker(
                    MarkerOptions()
                            .position(LatLng(userStop.location.lat, userStop.location.lng))
                            .title(userStop.name)
                            .icon(BitmapDescriptorFactory.fromBitmap(icon))
            )
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
        refreshStateObservables()
        shouldShowCurrentLocation = false
        allDisposables.add(
                mapReadyObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isMapReady ->
                            if (isMapReady) {
                                initViewSwitcher(places)
                                addStopMarkersOnMap(places)
                                val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(getLatLngBounds(places.map { LatLng(it.location.lat, it.location.lng) }), 20.px)
                                mapInstance?.stopAnimation()
                                mapInstance?.animateCamera(cameraUpdateFactory, 1000, null)
                            }
                        }
        )
    }

    private fun refreshStateObservables() {
        mapReadyObservable = PublishSubject.create()
        locationObservable = PublishSubject.create()
    }

    fun showRoute(places: ArrayList<UserStop>) {
        refreshStateObservables()
        shouldShowCurrentLocation = false
        allDisposables.add(
                mapReadyObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isMapReady ->
                            if (isMapReady) {
                                allDisposables.add(
                                        locationObservable.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe { currentLocation ->
                                                    if (currentLocation != null) {
                                                        initViewSwitcher(places)
                                                        places.add(0, UserStop(getString(R.string.current_location), com.google.maps.model.LatLng(currentLocation.latitude, currentLocation.longitude)))
                                                        mapInstance?.stopAnimation()
                                                        showDirectionsOnMap(places)
                                                    }
                                                }
                                )
                            }
                        }
        )
    }

    private fun initViewSwitcher(places: ArrayList<UserStop>) {
        ctlViewSwitcherHome.show()
        linListViewHome.setOnClickListener {
            if (::listener.isInitialized) {
                listener.onListViewSelected(places)
            }
        }
        txtNextStopHome.text = places[0].name
    }

    fun setListener(listener: HomeListener) {
        this.listener = listener
    }

    override fun onCloseDirections() {

    }

    private fun getGeoContext(): GeoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()

    override fun onDestroy() {
        super.onDestroy()
        SmartLocation.with(context)
                .location()
                .stop()
        shouldShowCurrentLocation = true
    }

}

interface HomeListener {
    fun onListViewSelected(places: List<UserStop>)
    fun closeDirections()
}