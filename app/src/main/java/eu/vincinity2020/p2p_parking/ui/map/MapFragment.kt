package eu.vincinity2020.p2p_parking.ui.map

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.DaggerAppComponent
import eu.vincinity2020.p2p_parking.ui.book.BookParkingSpotActivity
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.app.network.NetworkService
import eu.vincinity2020.p2p_parking.utils.compoundviews.ParkingSpotCardView
import eu.vincinity2020.p2p_parking.data.entities.ParkingSpot
import eu.vincinity2020.p2p_parking.ui.search.SearchActivity
import eu.vincinity2020.p2p_parking.ui.search.SearchModule
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.view_parking_spot_card.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject

class MapFragment : BaseFragment(), eu.vincinity2020.p2p_parking.ui.map.MapMvpView,
        Marker.OnMarkerClickListener {



    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withHamburger()
            .withTitle(R.string.bottom_navigation_map)
            .build()


    override fun getMarkers(marker: ArrayList<ParkingSpot>) {
        parkingSpots.clear()
        parkingSpots.addAll(marker)
        addParkingSpotsToMap()
    }


    private val parkingSpots = ArrayList<ParkingSpot>(3)

    @BindView(R.id.map)
    lateinit var map: MapView


    @Inject
    lateinit var presenter: MapPresenter


    @BindView(R.id.parking_spot_card_view)
    lateinit var parkingSpotCardView: ParkingSpotCardView

    private var selectedParkingSpotUid: String? = null

    private var selectedParkingSpotName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = requireContext()
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))


        (activity?.application as App)
                .appComponent()
                .mapComponentBuilder()
                .mapModule(MapModule())
                .build()
                .inject(this)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)


        initSearchView()
        initMap()
        presenter.attach(this)

        presenter.getNearbyParking("a")
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

//        addParkingSpotsToMap()
    }


    private fun addParkingSpotsToMap() {
        val parkingIcon = requireActivity().getDrawable(R.drawable.ic_local_parking_black_24dp)

        parkingSpots.forEach { parkingSpot: ParkingSpot ->
            val marker = Marker(map)
            marker.position = GeoPoint(parkingSpot.lat, parkingSpot.lon)
            marker.title = parkingSpot.status
            marker.id = parkingSpot.sensorId.toString()
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

//    @OnClick(R.id.button_register_parking_spot)
//    fun onRegisterParkingSpotClicked() {
//        val parkingSpot = parkingSpots.find { parkingSpot ->
//            return@find parkingSpot.sensorId == selectedParkingSpotUid?.toLong()
//        }
//        val intent = BookParkingSpotActivity.getLaunchIntent(requireContext(), parkingSpot, null)
//        startActivity(intent)
//    }

    @Optional
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


    override fun onLoadFinish() {
    }
}