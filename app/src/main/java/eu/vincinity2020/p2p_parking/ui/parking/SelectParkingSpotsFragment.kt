package eu.vincinity2020.p2p_parking.ui.parking


import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.model.LatLng
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import eu.vincinity2020.p2p_parking.data.entities.parking.ParkingSpot
import eu.vincinity2020.p2p_parking.utils.*
import kotlinx.android.synthetic.main.fragment_select_parking_spots.*
import org.jetbrains.anko.support.v4.runOnUiThread
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SelectParkingSpotsFragment : Fragment(), SelectParkingContract.View, OnMapReadyCallback {

    @Inject
    lateinit var presenter: SelectParkingPresenter
    private lateinit var mapInstance: GoogleMap
    private var isListViewShown = false
    private var selectedMarkerLocation: LatLng? = null
    private lateinit var parkingSpots: ArrayList<ParkingSpot>
    private lateinit var listener: SelectParkingSpotListener
    private var allMarkers: ArrayList<Marker> = arrayListOf()
    private var placeQueryIndex = 0


    companion object {
        fun newInstance(listener: SelectParkingSpotListener, places: List<UserStop>): SelectParkingSpotsFragment {
            val fragment = SelectParkingSpotsFragment()
            fragment.attachListener(listener)
            val args = Bundle()
            args.putParcelableArray(INTENT_KEY_PLACES, places.toTypedArray())
            fragment.arguments = args
            return fragment
        }

        const val INTENT_KEY_PLACES = "intent_key_places"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_parking_spots, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.get(requireContext())
                .appComponent()
                .selectParkingSpotComponentBuilder()
                .selectParkingModule(SelectParkingModule())
                .build()
                .inject(this)
        pbSelectParking.progressTintList = ColorStateList.valueOf(Color.RED)
        initViews()
    }

    private fun attachListener(listener: SelectParkingSpotListener) {
        this.listener = listener
    }

    private fun initViews() {
        isListViewShown = false
        recSelectParking.gone()
        frlMapContainerSelectParking.show()
        initMap()
        btnToggleViewSelectParking.setOnClickListener {
            isListViewShown = !isListViewShown
            showParkingSpots()
        }
        btnNavigate.setOnClickListener {
            if (isListViewShown) {
                val parkingSpot = (recSelectParking.adapter as SelectParkingAdapter).getSelectedParkingSpot()
                if (parkingSpot != null) {
                    navigateTo(LatLng(parkingSpot.lat, parkingSpot.lon))
                }
            } else {
                navigateTo(selectedMarkerLocation)
            }
        }
    }

    private fun initMap() {
        val mapFragment = SupportMapFragment()
        mapFragment.getMapAsync(this)
        childFragmentManager.addFragment(R.id.frlMapContainerSelectParking, mapFragment)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mapInstance = googleMap
            val places = arguments?.getParcelableArray(INTENT_KEY_PLACES) as Array<UserStop>?
            if (places != null) {
                presenter.getParkingSpots(this, places[placeQueryIndex].location)
            }
            googleMap.setOnMarkerClickListener { marker ->
                selectedMarkerLocation = LatLng(marker.position.latitude, marker.position.longitude)
                allMarkers.forEach { it.setIcon(BitmapDescriptorFactory.fromBitmap(getSmallMarkerBitmap())) }
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getBigMarkerBitmap()))
                true
            }
        }
    }

    private fun navigateTo(position: LatLng?) {
        val places = arguments?.getParcelableArray(INTENT_KEY_PLACES) as Array<UserStop>?
        if (places != null) {
            if (placeQueryIndex <= places.lastIndex) {
                presenter.getParkingSpots(this, places[placeQueryIndex].location)
            } else {
                listener.onParkingSpotSelected(places.toList())
            }
        }
    }

    override fun handleGetParkingSuccess(parkingSpots: ArrayList<ParkingSpot>) {
        placeQueryIndex++
        if (parkingSpots.isNotEmpty()) {
            mapInstance.clear()
            this.parkingSpots = parkingSpots
            populateListView(parkingSpots)
            populateMapMarkers(parkingSpots)
        } else {
            showNoParkingSpotsAvailableDialog()
        }
    }

    private fun showNoParkingSpotsAvailableDialog() {
        val places = arguments?.getParcelableArray(INTENT_KEY_PLACES) as Array<UserStop>?
        P2PDialog.infoDialog(requireContext(), "No parking spots available for the selected location: " +
                (places?.getOrNull(placeQueryIndex - 1)?.name ?: "")) {
            if (places != null) {
                if (placeQueryIndex <= places.lastIndex) {
                    presenter.getParkingSpots(this, places[placeQueryIndex].location)
                } else {
                    listener.onParkingSpotSelected(places.toList())
                }
            }
        }.apply {
            show()
            cancelable(false)
        }
    }

    private fun showParkingSpots() {
        if (isListViewShown) {
            frlMapContainerSelectParking.gone()
            recSelectParking.show()
            populateListView(parkingSpots)
            btnToggleViewSelectParking.text = "Map View"
        } else {
            frlMapContainerSelectParking.show()
            recSelectParking.gone()
            populateMapMarkers(parkingSpots)
            btnToggleViewSelectParking.text = "List View"
        }
    }

    private fun populateMapMarkers(parkingSpots: ArrayList<ParkingSpot>) {
        mapInstance.clear()
        val locations = parkingSpots.map { com.google.android.gms.maps.model.LatLng(it.lat, it.lon) }
        addStopMarkersOnMap(locations)
        val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(getLatLngBounds(locations), 40.px)
        mapInstance.animateCamera(cameraUpdateFactory, 1000, null)
    }

    private fun getLatLngBounds(decodedPath: List<com.google.android.gms.maps.model.LatLng>): LatLngBounds {
        val boundsBuilder = LatLngBounds.builder()
        decodedPath.forEach {
            boundsBuilder.include(it)
        }
        return boundsBuilder.build()
    }

    private fun addStopMarkersOnMap(parkingSpots: List<com.google.android.gms.maps.model.LatLng>) {
        parkingSpots.forEach {
            val icon = getSmallMarkerBitmap()
            mapInstance.addMarker(
                    MarkerOptions()
                            .position(it)
                            .icon(BitmapDescriptorFactory.fromBitmap(icon))
            ).also { marker ->
                allMarkers.add(marker)
            }
        }
    }

    private fun getSmallMarkerBitmap(): Bitmap? {
        val bitmap = requireContext().let { context -> getBitmapFromVectorDrawable(context, R.drawable.ic_parking_spot) }
        return Bitmap.createScaledBitmap(bitmap, 30.px, 30.px, false)
    }

    private fun getBigMarkerBitmap(): Bitmap? {
        val bitmap = requireContext().let { context -> getBitmapFromVectorDrawable(context, R.drawable.ic_parking_spot) }
        return Bitmap.createScaledBitmap(bitmap, 50.px, 50.px, false)
    }

    private fun populateListView(parkingSpots: ArrayList<ParkingSpot>) {
        val adapter = SelectParkingAdapter(parkingSpots)
        recSelectParking.layoutManager = LinearLayoutManager(requireContext())
        recSelectParking.adapter = adapter
    }

    override fun handleGetParkingFailure(e: Throwable) {
        placeQueryIndex++
        P2PDialog.errorDialog(requireContext(), e.getErrorMessage()) {
            val places = arguments?.getParcelableArray(INTENT_KEY_PLACES) as Array<UserStop>?
            if (places != null) {
                if (placeQueryIndex <= places.lastIndex) {
                    presenter.getParkingSpots(this, places[placeQueryIndex].location)
                } else {
                    listener.onParkingSpotSelected(places.toList())
                }
            }
        }.show()
    }

    override fun showProgress() {
        runOnUiThread {
            pbSelectParking.show()
        }
    }

    override fun hideProgress() {
        runOnUiThread {
            pbSelectParking.gone()
        }
    }


}

interface SelectParkingSpotListener {
    fun onParkingSpotSelected(places: List<UserStop>)
}