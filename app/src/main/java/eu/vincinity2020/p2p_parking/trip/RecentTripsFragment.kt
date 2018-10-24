package eu.vincinity2020.p2p_parking.trip

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.common.BaseFragment
import eu.vincinity2020.p2p_parking.entities.Trip
import kotlinx.android.synthetic.main.fragment_recent_trips.*
import java.util.*
import javax.inject.Inject

class RecentTripsFragment : BaseFragment(), TripView {

    @Inject
    lateinit var presenter: TripPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recent_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view_recent_trips.layoutManager = LinearLayoutManager(view.context)

        val trip = Trip(1, Date(), Date(), "ParkingSpot #1")
        val list = listOf(trip)

        recycler_view_recent_trips.adapter = RecentTripAdapter(view.context, list)
    }

    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .tripComponentBuilder()
                .tripModule(TripModule())
                .build()
                .inject(this)

        presenter.attach(this)
        presenter.getRecentTrips()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onUnknownError(errorMessage: String) {
    }

    override fun onTimeout() {
    }

    override fun onNetworkError() {
    }

}