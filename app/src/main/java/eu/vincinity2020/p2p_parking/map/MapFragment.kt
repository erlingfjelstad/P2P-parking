package eu.vincinity2020.p2p_parking.map

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.book.BookParkingSpotActivity
import eu.vincinity2020.p2p_parking.common.BaseFragment
import eu.vincinity2020.p2p_parking.compoundviews.ParkingSpotCardView
import eu.vincinity2020.p2p_parking.entities.ParkingSpot
import eu.vincinity2020.p2p_parking.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.view_parking_spot_card.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment : BaseFragment(), eu.vincinity2020.p2p_parking.map.MapView,
        Marker.OnMarkerClickListener {

    private val parkingSpots = ArrayList<ParkingSpot>(3)
    @BindView(R.id.map)
    lateinit var map: MapView

    @BindView(R.id.parking_spot_card_view)
    lateinit var parkingSpotCardView: ParkingSpotCardView

    private var selectedParkingSpotUid: String? = null

    private var selectedParkingSpotName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = requireContext()
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        initSearchView()
        initMap()
    }

    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .mapComponentBuilder()
                .mapModule(MapModule())
                .build()
                .inject(this)
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }


    private fun initSearchView() {
        search_view_map.clearFocus()
        search_view_map.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initMap() {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(false)
        map.setMultiTouchControls(true)
        val mapController = map.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(69.649190, 18.955182)
        mapController.setCenter(startPoint)

        addParkingSpotsToMap()
    }

    private fun addParkingSpotsToMap() {
        val parkingSpot1 = ParkingSpot(1L, "Parkeringsplass #1", 69.648631, 18.955679)
        val parkingSpot2 = ParkingSpot(2L, "Parkeringsplass #2", 69.649120, 18.953544)
        val parkingSpot3 = ParkingSpot(3L, "Parkeringsplass #3", 69.648251, 18.959048)

        parkingSpots.add(parkingSpot1)
        parkingSpots.add(parkingSpot2)
        parkingSpots.add(parkingSpot3)
        val parkingIcon = requireActivity().getDrawable(R.drawable.ic_local_parking_black_24dp)

        parkingSpots.forEach { parkingSpot: ParkingSpot ->
            val marker = Marker(map)
            marker.position = GeoPoint(parkingSpot.latitude, parkingSpot.longitude)
            marker.title = parkingSpot.name
            marker.id = parkingSpot.id.toString()
            marker.icon = parkingIcon
            marker.setOnMarkerClickListener(this)

            map.overlays.add(marker)
        }
    }

    override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
        parkingSpotCardView.visibility = View.VISIBLE
        selectedParkingSpotUid = marker?.id
        selectedParkingSpotName = marker?.title

        text_view_parking_spot_name.text = selectedParkingSpotName
        return true
    }

    @OnClick(R.id.button_register_parking_spot)
    fun onRegisterParkingSpotClicked() {
        val parkingSpot = parkingSpots.find { parkingSpot ->
            return@find parkingSpot.id == selectedParkingSpotUid?.toLong()
        }
        val intent = BookParkingSpotActivity.getLaunchIntent(requireContext(), parkingSpot, null)
        startActivity(intent)
    }

    @OnClick(R.id.image_button_close)
    fun onCloseParkingSpotClicked() {
        parkingSpotCardView.visibility = View.GONE
    }


    override fun onUnknownError(errorMessage: String) {

    }

    override fun onTimeout() {
    }

    override fun onNetworkError() {
    }
}