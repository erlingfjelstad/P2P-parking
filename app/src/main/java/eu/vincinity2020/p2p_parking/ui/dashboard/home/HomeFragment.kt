package eu.vincinity2020.p2p_parking.ui.dashboard.home


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class HomeFragment : Fragment(), OnMapReadyCallback {

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
                googleMap.isMyLocationEnabled = true
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

    fun showDirectionsOnMap(stops: ArrayList<UserStop>) {
        mapInstance?.clear()
        doAsync {
            val totalStops = stops.size

            val directionResult = if (totalStops >= 3) {
                val waypoints = stops.subList(1, stops.size - 1).map { DirectionsApiRequest.Waypoint(it.location) }
                DirectionsApi.newRequest(getGeoContext())
                        .mode(TravelMode.DRIVING)
                        .origin(stops[0].location)
                        .waypoints(*(waypoints.toTypedArray()))
                        .destination(stops[stops.size - 1].location)
                        .units(Unit.METRIC)
                        .await()
            } else {
                DirectionsApi.newRequest(getGeoContext())
                        .mode(TravelMode.DRIVING)
                        .origin(stops[0].location)
                        .destination(stops[stops.size - 1].location)
                        .units(Unit.METRIC)
                        .await()
            }


            val decodedPath = PolyUtil.decode(directionResult?.routes?.get(0)?.overviewPolyline?.encodedPath)
            val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(getLatLngBounds(stops.subList(0, 2).map { LatLng(it.location.lat, it.location.lng) }), 20.px)
            uiThread {
                mapInstance?.addPolyline(PolylineOptions().color(Color.BLUE).addAll(decodedPath))
                mapInstance?.animateCamera(cameraUpdateFactory, 1000, null)
                addStopMarkersOnMap(stops)
                //show directions in progress ui
                val statusFragment = DirectionStatusFragment()
                childFragmentManager.addFragmentIfNotAlreadyAdded(R.id.frlStatusContainerHome, statusFragment)
                statusFragment.updateArrivalTime(((directionResult?.routes?.get(0)?.legs?.get(0)?.duration?.inSeconds)?.div(60))?.toInt())
                statusFragment.showDirectionsButton(stops)
                statusFragment.showTimerButton()
            }
        }
    }

    private fun addStopMarkersOnMap(stops: List<UserStop>) {
        stops.forEachIndexed { index, userStop ->
            val icon = if (index == 0) {
                context?.let { context -> getBitmapFromVectorDrawable(context, R.drawable.ic_map_car) }
            } else if (index == stops.size - 1) {
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


    /*override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            MoveAnimation.create(MoveAnimation.LEFT, enter, NAV_FRAGMENT_ANIMATION_DURATION)
        } else {
            MoveAnimation.create(MoveAnimation.LEFT, enter, NAV_FRAGMENT_ANIMATION_DURATION)
        }
    }*/

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
}