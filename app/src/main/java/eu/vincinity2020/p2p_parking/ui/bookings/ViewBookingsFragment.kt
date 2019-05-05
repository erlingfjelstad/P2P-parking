package eu.vincinity2020.p2p_parking.ui.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.OnClick
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.data.entities.Bookings
import eu.vincinity2020.p2p_parking.data.entities.Trip
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.fragment_my_bookings.*
import kotlinx.android.synthetic.main.fragment_recent_trips.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ViewBookingsFragment : BaseFragment(), ViewBookingsMvpView {

    @Inject
    lateinit var presenter: ViewBookingsPresenter
    private val bookings = ArrayList<Bookings>()

    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withHamburger()
            .withTitle(R.string.my_bookings)
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_bookings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMyBookings.layoutManager = LinearLayoutManager(view.context)

        initMap()

        rvMyBookings.adapter = MyBookingsAdapter(view.context, bookings)
    }

    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .viewBookingsComponentBuilder()
                .viewBookingsModule(ViewBookingModule())
                .build()
                .inject(this)
        map.onResume()

        presenter.attach(this)
        presenter.getAllBookingList(App.get(requireContext()).getUser()!!)
        progress.visibility =View.VISIBLE
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



    @OnClick(R.id.tvMapView)
    fun showMap() {
        tvMapView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark))
        tvListView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_light))
        map.visibility = View.VISIBLE
        rvMyBookings.visibility = View.GONE
    }


    @OnClick(R.id.tvListView)
    fun showList() {
        tvMapView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_light))
        tvListView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark))
        map.visibility = View.GONE
        rvMyBookings.visibility = View.VISIBLE
    }
    override fun onPause() {
        super.onPause()
        map.onPause()
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

    override fun updateBookingList(bookings: ArrayList<Bookings>) {
        this.bookings.addAll(bookings)
        rvMyBookings.adapter?.notifyDataSetChanged()
    }


}