package eu.vincinity2020.p2p_parking.ui.choose

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.ui.book.BookParkingSpotActivity
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import eu.vincinity2020.p2p_parking.data.entities.ParkingSensor
import eu.vincinity2020.p2p_parking.ui.map.ParkingSpotAdapter
import kotlinx.android.synthetic.main.activity_choose_parking_spot.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class ChooseParkingSpotActivity : BaseActivity(), ParkingSpotAdapter.OnParkingSpotClickedListener, Marker.OnMarkerClickListener {
    override fun onCloseButtonClicked() {

    }

    companion object {
        private const val ARG_GEO_POINT = "arg:GeoPoint"
        private const val ARG_BUNDLE = "arg:Bundle"

        fun getLaunchIntent(context: Context, geoPoint: GeoPoint): Intent {
            val intent = Intent(context, ChooseParkingSpotActivity::class.java)
            val bundle = Bundle(1)
            bundle.putParcelable(ARG_GEO_POINT, geoPoint)
            intent.putExtra(ARG_BUNDLE, bundle)

            return intent
        }
    }

    private var selectedParkingSpotUid: String? = null

    private var selectedParkingSpotName: String? = null

    private lateinit var geoPoint: GeoPoint

    lateinit var parkingSpotAdapter: ParkingSpotAdapter

    private val parkingSensors: ArrayList<ParkingSensor> = ArrayList(3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        setContentView(R.layout.activity_choose_parking_spot)

        val bundle = intent.getBundleExtra(ARG_BUNDLE)
        geoPoint = bundle.getParcelable(ARG_GEO_POINT)


        setUpRecyclerView()
        initMap()
    }

    private fun setUpRecyclerView() {


        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recyclerview_promoted_spots)
        val linearLayoutManager = LinearLayoutManager(recyclerview_promoted_spots.context, LinearLayoutManager.HORIZONTAL, false)


        recyclerview_promoted_spots.layoutManager = linearLayoutManager
        parkingSpotAdapter = ParkingSpotAdapter(parkingSensors, this, this)
        recyclerview_promoted_spots.adapter = parkingSpotAdapter
        recyclerview_promoted_spots.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                if (position >= 0 && dx != 0) {
                    val itemAtPosition = parkingSpotAdapter.getItemAtPosition(position)
                    val controller = map.controller
                    val geoPoint = GeoPoint(itemAtPosition.lat, itemAtPosition.lon)
                    controller.animateTo(geoPoint, 17.0, 1000)
                }
            }
        })

    }

    private fun initMap() {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(false)
        map.setMultiTouchControls(true)
        addParkingSpotsToMap()
        addDesitinationIconToMap(geoPoint)
    }

    private fun addDesitinationIconToMap(geoPoint: GeoPoint) {
        val placeIcon = getDrawable(R.drawable.ic_place_black_24dp)
        placeIcon.setTint(Color.RED)
        val marker = Marker(map)
        marker.position = geoPoint
        marker.icon = placeIcon

        map.overlays.add(marker)
        map.controller.setCenter(marker.position)
        map.controller.setZoom(15.0)
    }

    private fun addParkingSpotsToMap() {
        val parkingIcon = getDrawable(R.drawable.ic_local_parking_black_24dp)
        parkingSensors.forEach { parkingSensor: ParkingSensor ->
            val marker = Marker(map)
            marker.position = GeoPoint(parkingSensor.lat, parkingSensor.lon)
            marker.title = parkingSensor.status
            marker.icon = parkingIcon
            marker.setOnMarkerClickListener(this)

            map.overlays.add(marker)
        }


    }

    override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
//        recyclerview_promoted_spots.adapter?.
//        selectedParkingSpotUid = marker?.snippet
//        selectedParkingSpotName = marker?.title
//
//        text_view_parking_spot_name.text = selectedParkingSpotName
        return true
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onParkingSpotClicked(parkingSensor: ParkingSensor) {
        val launchIntent = BookParkingSpotActivity.getLaunchIntent(this, parkingSensor, geoPoint)
        startActivity(launchIntent)
    }
}