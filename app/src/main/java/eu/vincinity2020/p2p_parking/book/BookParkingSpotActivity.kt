package eu.vincinity2020.p2p_parking.book

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import butterknife.BindDrawable
import butterknife.ButterKnife
import butterknife.OnClick
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.common.BaseActivity
import eu.vincinity2020.p2p_parking.entities.ParkingSpot
import eu.vincinity2020.p2p_parking.reciept.RecieptActivity
import kotlinx.android.synthetic.main.activity_book_parking_spot.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import java.util.*
import javax.inject.Inject


class BookParkingSpotActivity :
        BaseActivity(),
        BookParkingSpotView,
        OnItemGestureListener<OverlayItem> {

    @Inject
    lateinit var bookParkingSpotPresenter: BookParkingSpotPresenter

    @BindDrawable(R.drawable.ic_place_black_24dp)
    lateinit var placeIcon: Drawable

    private lateinit var parkingSpot: ParkingSpot

    companion object {
        private const val ARG_PARKING_SPOT = "arg:ParkingSpot"
        private const val ARG_GEO_POINT = "arg:GeoPoint"
        private const val ARG_BUNDLE = "ARG_BUNDLE"

        fun getLaunchIntent(context: Context, parkingSpot: ParkingSpot?,
                            geoPoint: GeoPoint?): Intent {
            val bundle = Bundle().apply {
                putParcelable(ARG_PARKING_SPOT, parkingSpot)
                putParcelable(ARG_GEO_POINT, geoPoint)
            }

            return Intent(context, BookParkingSpotActivity::class.java).apply {
                putExtra(ARG_BUNDLE, bundle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_book_parking_spot)

        ButterKnife.bind(this)
        val bundle = intent.getBundleExtra(ARG_BUNDLE)

        parkingSpot = bundle.getParcelable(ARG_PARKING_SPOT)
        val geoPoint = bundle.getParcelable<GeoPoint>(ARG_GEO_POINT)
        initMap(parkingSpot)
        addParkingSpotToMap(parkingSpot)
        addDesitinationIconToMap(geoPoint)

        button_book_parking_spot.setOnClickListener {
            validateForm()
        }

        App.get(this)
                .appComponent()
                .bookParkingSpotComponentBuilder()
                .bookParkingSpotModule(BookParkingSpotModule())
                .build()
                .inject(this)
    }

    private fun validateForm() {
        val fromDate = from_date_text_input_layout_register_user.editText?.tag as Date
        val toDate = to_date_text_input_layout_register_user.editText?.tag as Date

        bookParkingSpotPresenter.bookParkingSpot(fromDate, toDate, parkingSpot.name)
    }

    private fun addDesitinationIconToMap(geoPoint: GeoPoint?) {
        geoPoint?.let {
            placeIcon.setTint(Color.RED)
            val marker = Marker(map)
            marker.position = it
            marker.icon = placeIcon

            return@let map.overlays.add(marker)
        }
    }

    private fun addParkingSpotToMap(parkingSpot: ParkingSpot) {
        val parkingIcon = getDrawable(R.drawable.ic_local_parking_black_24dp)


        val marker = Marker(map)
        marker.position = GeoPoint(parkingSpot.latitude, parkingSpot.longitude)
        marker.title = parkingSpot.name
        marker.icon = parkingIcon
        marker.setOnMarkerClickListener(null)
        map.overlays.add(marker)

    }

    private fun initMap(parkingSpot: ParkingSpot) {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(false)
        map.setMultiTouchControls(true)
        val mapController = map.controller

        // hack to fix mapController.setCenter not working
        val startPoint = GeoPoint(parkingSpot.latitude + 0.005, parkingSpot.longitude)
        mapController.setCenter(startPoint)
        mapController.animateTo(startPoint)
        mapController.setZoom(15.0)
    }


    override fun onResume() {
        super.onResume()
        map.onResume()
        bookParkingSpotPresenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
        bookParkingSpotPresenter.detach()
    }

    @OnClick(R.id.from_date_text_input_layout_register_user, R.id.from_date_text_view_register_user)
    fun onFromDateClicked() {
        openDatePickerDialog(from_date_text_view_register_user)
    }

    @SuppressLint("SetTextI18n")
    private fun openDatePickerDialog(textInputEditText: TextInputEditText) {

        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)


        val timePicker: TimePickerDialog
        timePicker = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener { _,
                                                     selectedHour,
                                                     selectedMinute ->
                    val minuteString: String = if (selectedMinute <= 9) {
                        "0$selectedMinute"
                    } else {
                        selectedMinute.toString()
                    }
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                    selectedCalendar.set(Calendar.MINUTE, selectedMinute)

                    textInputEditText.tag = selectedCalendar.time
                    textInputEditText.setText(selectedHour.toString() + ":" + minuteString)
                },
                hour, minute, true)//Yes 24 hour time

        timePicker.show()
    }

    @OnClick(R.id.to_date_text_input_layout_register_user, R.id.to_date_text_view_register_user)
    fun onToDateClicked() {
        openDatePickerDialog(to_date_text_view_register_user)
    }

    override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
        return false
    }

    override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
        return true
    }

    override fun onTripSuccessfullyBooked() {
        val launchIntent = RecieptActivity.getLaunchIntent(this, parkingSpot,
                from_date_text_input_layout_register_user.editText?.text.toString(),
                to_date_text_input_layout_register_user.editText?.text.toString())

        startActivity(launchIntent)
    }

    override fun onUnknownError(errorMessage: String) {

    }

    override fun onTimeout() {
    }

    override fun onNetworkError() {
    }
}