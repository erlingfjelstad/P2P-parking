package eu.vincinity2020.p2p_parking.ui.dashboard.home


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.app.ActivityCompat
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
import com.labo.kaji.fragmentanimations.MoveAnimation

import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.AppConstants.Companion.NAV_FRAGMENT_ANIMATION_DURATION
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import eu.vincinity2020.p2p_parking.utils.addFragment
import eu.vincinity2020.p2p_parking.utils.getBitmapFromVectorDrawable
import eu.vincinity2020.p2p_parking.utils.px
import io.nlopez.smartlocation.SmartLocation
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.time.Instant
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var mapInstance: GoogleMap? = null


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
            moveCameraToCurrentLocation(googleMap)
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
            }
        }
    }

    private fun moveCameraToCurrentLocation(map: GoogleMap) {
        SmartLocation.with(context)
                .location()
                .oneFix()
                .start { location ->
                    map.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 17f)
                    )
                }
    }

    fun showDirectionsOnMap(stops: ArrayList<UserStop>) {
        mapInstance?.clear()
        doAsync {
            val totalStops = stops.size

            val directionResult = if (totalStops >= 3) {
                val waypoints = stops.subList(1, stops.size - 2).map { DirectionsApiRequest.Waypoint(it.location) }
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
            val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(getLatLngBounds(stops.map { LatLng(it.location.lat,it.location.lng) }), 20.px)
            uiThread {
                mapInstance?.addPolyline(PolylineOptions().color(Color.BLUE).addAll(decodedPath))
                mapInstance?.animateCamera(cameraUpdateFactory, 1000, null)
                addStopMarkersOnMap(stops)
            }
        }
    }

    private fun addStopMarkersOnMap(stops: ArrayList<UserStop>) {
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
        decodedPath.subList(0, 2).forEach {
            boundsBuilder.include(it)
        }
        return boundsBuilder.build()
    }


    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            MoveAnimation.create(MoveAnimation.LEFT, enter, NAV_FRAGMENT_ANIMATION_DURATION)
        } else {
            MoveAnimation.create(MoveAnimation.LEFT, enter, NAV_FRAGMENT_ANIMATION_DURATION)
        }
    }

    private fun getGeoContext(): GeoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()
}
