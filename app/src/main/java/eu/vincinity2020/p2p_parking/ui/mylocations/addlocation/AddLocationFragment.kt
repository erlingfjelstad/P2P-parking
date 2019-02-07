package eu.vincinity2020.p2p_parking.ui.mylocations.addlocation

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.data.entities.Trip
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.fragment_add_location.*
import kotlinx.android.synthetic.main.fragment_recent_trips.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddLocationFragment : BaseFragment(), AddLocationMvpView {


    @Inject
    lateinit var presenter: AddLocationPresenter

    lateinit var  marker : Marker


    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withBackButton()
            .withTitle(R.string.add_new_location)
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list: ArrayList<Trip> = ArrayList()
        for (i in 1..5) {
            val trip1 = Trip(i.toLong(), Date(), Date(), "ParkingSensor # " + i)
            list.add(trip1)

        }
        initMap()

        btnSave.setOnClickListener {
            if (etDescription.text.toString() != "")
            {
               val lat =  marker.position.latitude.toString()
               val lng =  marker.position.longitude.toString()
                if (lat == "0.0" && lng == "0.0")
                {
                   Toast.makeText(context, "Please select a valid location", Toast.LENGTH_SHORT).show()
                }
                else {
                    val desc = etDescription.text.toString()
                    presenter.saveLocation(lat, lng, desc, App.get(context!!).getUser()!!.email, App.get(context!!).getUser()!!.password)
                    progress.visibility = View.VISIBLE
                    map.visibility = View.GONE
                }
            }
            else {
                Toast.makeText(context, "Enter description", Toast.LENGTH_SHORT ).show()
            }
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


        map.overlays.add(MapEventsOverlay(object : MapEventsReceiver {

            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                Log.e("MapView", "normal click")
                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                Log.e("MapView", "long click")
                val parkingIcon = requireActivity().getDrawable(R.drawable.ic_local_parking_black_24dp)

                marker.position = p
                marker.title = "My location"
                marker.icon = parkingIcon
                marker.id = "MARKER"
                map.overlays.add(marker)

                layoutDescription.visibility = View.VISIBLE
                return false
            }
        }))

        marker = Marker(map)
//        addParkingSpotsToMap()
    }
    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .addLocationComponentBuilder()
                .addLocationModule(AddLocationModule())
                .build()
                .inject(this)

        presenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onUnknownError(errorMessage: String) {
        (activity as BaseActivity).showToast(resources.getString(R.string.unable_to_connect_to_server))
        progress.visibility = View.GONE
        map.visibility = View.VISIBLE

    }

    override fun onTimeout() {
        (activity as BaseActivity).showToast(resources.getString(R.string.unable_to_connect_to_server))
        progress.visibility = View.GONE
        map.visibility = View.VISIBLE

    }

    override fun onNetworkError() {
        (activity as BaseActivity).showToast(resources.getString(R.string.unable_to_connect_to_server))
        progress.visibility = View.GONE
        map.visibility = View.VISIBLE

    }

    override fun onLoadFinish() {
        progress.visibility = View.GONE
        map.visibility = View.VISIBLE
    }

    override fun updateLocations(user: User) {
        progress.visibility = View.GONE
        map.visibility = View.VISIBLE
    }

}