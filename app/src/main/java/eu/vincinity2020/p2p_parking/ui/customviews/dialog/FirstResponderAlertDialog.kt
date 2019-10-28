package eu.vincinity2020.p2p_parking.ui.customviews.dialog

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.data.entities.eventbus.FirstResponderAlert
import eu.vincinity2020.p2p_parking.utils.isConnected
import eu.vincinity2020.p2p_parking.utils.toCompoundDuration
import io.nlopez.smartlocation.SmartLocation
import kotlinx.android.synthetic.main.layout_dialog_first_responder.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit


class FirstResponderAlertDialog(context: Context,
                                val alertInfo: FirstResponderAlert,
                                val onRespond: (() -> Unit)? = null) : AlertDialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dialog_first_responder)
        initViews()
    }

    private fun initViews() {
        txtPlaceNameResponder.text = alertInfo.locationName
        ctlRespond.setOnClickListener {
            if (onRespond != null) {
                onRespond.invoke()
            } else {
                context.toast("Response not available for mock alert")
            }
            dismiss()
        }
        showEta()
    }

    private fun showEta() {
        doAsync {
            SmartLocation.with(context)
                    .location()
                    .oneFix()
                    .start { location ->
                        if (context.isConnected()) {
                            val directionsResult = DirectionsApi.newRequest(getGeoContext())
                                    .mode(TravelMode.DRIVING)
                                    .origin(LatLng(location.latitude, location.longitude))
                                    .destination(LatLng(alertInfo.lat, alertInfo.long))
                                    .units(com.google.maps.model.Unit.METRIC)
                                    .await()

                            if (directionsResult.routes.isNotEmpty()) {
                                //val eta = directionsResult.routes?.get(0)?.legs?.getOrNull(0)?.duration?.humanReadable
                                var totalSeconds:Long = 0
                                directionsResult?.routes?.get(0)?.legs?.forEach {
                                    totalSeconds += it.duration.inSeconds
                                }
                                context.runOnUiThread {
                                    txtEtaResponder.text = totalSeconds.toCompoundDuration()
                                }
                            }
                        }
                    }
        }
    }

    private fun getGeoContext(): GeoApiContext = GeoApiContext.Builder()
            .apiKey(context.getString(R.string.google_maps_key))
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()
}