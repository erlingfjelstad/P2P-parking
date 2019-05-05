package eu.vincinity2020.p2p_parking.ui.map

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.bottomsheet.BottomSheetDialog
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.AppConstants

import eu.vincinity2020.p2p_parking.app.common.BaseFragment

import eu.vincinity2020.p2p_parking.data.entities.ParkingSensor
import eu.vincinity2020.p2p_parking.ui.search.SearchActivity
import eu.vincinity2020.p2p_parking.utils.DateUtils
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.book_parking_layout.view.*
import kotlinx.android.synthetic.main.fragment_map.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.*
import javax.inject.Inject

class MapFragment : BaseFragment(), eu.vincinity2020.p2p_parking.ui.map.MapMvpView,
        Marker.OnMarkerClickListener, ParkingSpotAdapter.OnParkingSpotClickedListener {



    private val parkingSpots = ArrayList<ParkingSensor>()


    var isOnActivityCalled= false;
    @Inject
    lateinit var presenter: MapPresenter

    override fun onCloseButtonClicked() {

    }

    override fun onParkingSpotClicked(parkingSensor: ParkingSensor) {

        /**
         * showing bottom sheet dialog
         */
        var view = layoutInflater.inflate(R.layout.book_parking_layout, null);

        view.text_view_parking_spot_name.text = parkingSensor.status
        view.startTime.setIs24HourView(true)
        view.endTime.setIs24HourView(true)

        var dialog = BottomSheetDialog(context!!)
        dialog.setContentView(view)
        dialog.show()


        var endTime = Calendar.getInstance()
        var startTime = Calendar.getInstance()

        view.endTime.setOnTimeChangedListener { timePicker, hourOfDay, minute ->
            endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            endTime.set(Calendar.MINUTE, minute)
        }

        view.startTime.setOnTimeChangedListener { timePicker, hourOfDay, minute ->
            startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            startTime.set(Calendar.MINUTE, minute)
        }

        view.button_register_parking_spot.setOnClickListener {
            if (view.layoutTimings.visibility == View.VISIBLE) {
                if (endTime.get(Calendar.HOUR_OF_DAY) > startTime.get(Calendar.HOUR_OF_DAY) ||
                        (endTime.get(Calendar.HOUR_OF_DAY) == startTime.get(Calendar.HOUR_OF_DAY) && endTime.get(Calendar.MINUTE) > startTime.get(Calendar.MINUTE))) {
                    //valid time selected

                    val format = "yyyy-MM-dd'T'HH:mm:ss"
                    presenter.createNewBooking(App.get(requireContext()).getUser()!!, parkingSensor,
                            DateUtils.formatDateTime(startTime, format), DateUtils.formatDateTime(endTime, format))
                } else
                    Toast.makeText(context, "End time must be grater then start time.", Toast.LENGTH_SHORT).show()


            } else
                view.layoutTimings.visibility = View.VISIBLE
        }

    }


    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withHamburger()
            .withTitle(R.string.bottom_navigation_map)
            .build()


    override fun getMarkers(marker: ArrayList<ParkingSensor>) {
        parkingSpots.clear()
        parkingSpots.addAll(marker)
        addParkingSpotsToMap()
        rvSensors.adapter?.notifyDataSetChanged()
    }






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

        rvSensors.layoutManager = LinearLayoutManager(view.context)
        rvSensors.adapter = ParkingSpotAdapter(parkingSpots, view.context, this)

        presenter.attach(this)
        if (context != null && App.get(context!!).getUser() != null) {
            presenter.getDefaultParkingSensors(App.get(context!!).getUser()!!.email, App.get(context!!).getUser()!!.password)
        }

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

        presenter.attach(this)
        if (isOnActivityCalled) {

            if (arguments != null && arguments!!.containsKey("lat")) {
                presenter.getNearbyParkingSensors(App.get(context!!).getUser()!!.email, App.get(context!!).getUser()!!.password,
                        arguments!!.getDouble("lat"), arguments!!.getDouble("lon"))
                val startPoint = GeoPoint(arguments!!.getDouble("lat"), arguments!!.getDouble("lon"))
                map.controller.setCenter(startPoint)
                progress.visibility = View.VISIBLE

            }
        }
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }


    private fun initSearchView() {
        search_view_map.clearFocus()
        search_view_map.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivityForResult(intent, AppConstants.SEARCHACTIVITYCODE)
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

        parkingSpots.forEach { parkingSensor: ParkingSensor ->
            val marker = Marker(map)
            marker.position = GeoPoint(parkingSensor.lat, parkingSensor.lon)
            marker.title = parkingSensor.status
            marker.id = parkingSensor.sensorId.toString()
            marker.snippet = parkingSensor.oid
            marker.icon = parkingIcon
            marker.setOnMarkerClickListener(this)
            marker.relatedObject = parkingSensor
            map.overlays.add(marker)
        }
    }

    override fun onMarkerClick(marker: Marker, mapView: MapView?): Boolean {
//        parkingSpotCardView.visibility = View.VISIBLE

        val selectedParkingSpot = marker.relatedObject as ParkingSensor//ParkingSensor(marker.id.toLong(), marker.title, marker.snippet, marker.position.latitude, marker.position.longitude)

        onParkingSpotClicked(selectedParkingSpot)
        return true
    }




    @OnClick(R.id.tvMapView)
    fun showMap() {
        tvMapView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark))
        tvListView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_light))
        map.visibility = View.VISIBLE
        rvSensors.visibility = View.GONE
    }


    @OnClick(R.id.tvListView)
    fun showList() {
        tvMapView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_light))
        tvListView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark))
        map.visibility = View.GONE
        rvSensors.visibility = View.VISIBLE
    }


    override fun onUnknownError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        progress.visibility =View.GONE
    }

    override fun onTimeout() {
        Toast.makeText(requireContext(), resources.getString(R.string.unable_to_connect_to_server), Toast.LENGTH_SHORT).show()
        progress.visibility =View.GONE
    }

    override fun onNetworkError() {
        Toast.makeText(requireContext(), resources.getString(R.string.unable_to_connect_to_server), Toast.LENGTH_SHORT).show()
        progress.visibility =View.GONE
    }


    override fun onLoadFinish() {
        progress.visibility =View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.SEARCHACTIVITYCODE && data != null) {

            if (data.hasExtra("lat") && data.hasExtra("lon")) {
                isOnActivityCalled = true
                val latLongBundle = Bundle()
                latLongBundle.putDouble("lat", data.getDoubleExtra("lat", 0.0))
                latLongBundle.putDouble("lon", data.getDoubleExtra("lon", 0.0))
                arguments = latLongBundle
            }

        }
    }
}