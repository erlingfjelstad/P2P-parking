package eu.vincinity2020.p2p_parking.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.common.BaseFragment

class UpcomingTripsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        App.get(requireContext())
                .appComponent()
                .tripComponentBuilder()
                .tripModule(TripModule())
                .build()
                .inject(this)
    }
}