package eu.vincinity2020.p2p_parking.ui.progress


import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode
import com.google.maps.model.Unit
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import eu.vincinity2020.p2p_parking.utils.gone
import eu.vincinity2020.p2p_parking.utils.show
import io.nlopez.smartlocation.SmartLocation
import kotlinx.android.synthetic.main.fragment_progress.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.uiThread
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class ProgressFragment : Fragment() {

    companion object {
        fun newInstance(stops: ArrayList<UserStop>, listener: ProgressListener): ProgressFragment {
            val fragment = ProgressFragment()
            stops.removeAll { it.name.equals("Current Location", true) || it.name.equals("My location", true) }
            fragment.userStops = stops
            fragment.setListener(listener)
            return fragment
        }
    }

    private lateinit var userStops: ArrayList<UserStop>
    private lateinit var listener: ProgressListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startMonitoringLocation()
        initViews()
    }

    private fun setListener(listener: ProgressListener) {
        this.listener = listener
    }

    private fun initViews() {
        linMapViewProgress.setOnClickListener {
            listener.onMapViewSelected(userStops)
        }
    }

    private fun startMonitoringLocation() {
        showProgress()
        SmartLocation.with(context)
                .location()
                .start { location ->
                    if (location != null) {
                        determineEtas(location)
                    }
                }
    }

    private fun determineEtas(location: Location) {
        doAsync {
            val waypoints = userStops.subList(0, userStops.size - 1).map { DirectionsApiRequest.Waypoint(it.location) }
            val directionResults = DirectionsApi.newRequest(getGeoContext())
                    .mode(TravelMode.DRIVING)
                    .origin(LatLng(location.latitude, location.longitude))
                    .destination(LatLng(userStops.last().location.lat, userStops.last().location.lng))
                    .waypoints(*(waypoints).toTypedArray())
                    .units(Unit.METRIC)
                    .await()

            val stopEtas = directionResults?.routes?.get(0)?.legs?.map { it.duration.humanReadable }
            val stopDistances = directionResults?.routes?.get(0)?.legs?.map { it.distance.humanReadable }
            userStops.forEachIndexed { index, userStop ->
                userStop.eta = stopEtas?.getOrNull(index) ?: "-"
                userStop.distance = stopDistances?.getOrNull(index) ?: "-"
            }

            uiThread {
                hideProgress()
                txtNextStopProgress.text = userStops.first { !it.isStopDone }.name
                initStops(userStops)
            }
        }

    }

    private fun initStops(userStops: ArrayList<UserStop>) {
        val adapter = ProgressAdapter(userStops)
        recProgress.layoutManager = LinearLayoutManager(context)
        recProgress.adapter = adapter
        txtNextStopEtaProgress.text = userStops[0].eta
    }

    private fun getGeoContext(): GeoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()

    private fun showProgress() {
        runOnUiThread {
            pbProgress?.show()
        }
    }

    private fun hideProgress() {
        runOnUiThread {
            pbProgress?.gone()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SmartLocation.with(context)
                .location()
                .stop()
    }


}

interface ProgressListener {
    fun onMapViewSelected(places: List<UserStop>)
}
