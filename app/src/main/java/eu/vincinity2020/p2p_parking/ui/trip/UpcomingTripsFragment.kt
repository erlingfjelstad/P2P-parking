package eu.vincinity2020.p2p_parking.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar

class UpcomingTripsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withHamburger()
            .withTitle(R.string.bottom_navigation_trips)
            .build()
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