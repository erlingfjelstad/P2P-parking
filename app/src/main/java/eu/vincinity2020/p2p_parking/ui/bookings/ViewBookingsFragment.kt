package eu.vincinity2020.p2p_parking.ui.bookings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.data.entities.Trip
import eu.vincinity2020.p2p_parking.data.entities.User
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.fragment_my_bookings.*
import kotlinx.android.synthetic.main.fragment_recent_trips.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ViewBookingsFragment : BaseFragment(), ViewBookingsMvpView {

    @Inject
    lateinit var presenter: ViewBookingsPresenter

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
        val list: ArrayList<Trip> = ArrayList()
        for (i in 1..5) {
            val trip1 = Trip(i.toLong(), Date(), Date(), "ParkingSpot # " + i)
            list.add(trip1)

        }
        rvMyBookings.adapter = MyBookingsAdapter(view.context, list)
    }

    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .viewBookingsComponentBuilder()
                .viewBookingsModule(ViewBookingModule())
                .build()
                .inject(this)

        presenter.attach(this)
        presenter.getAllBookingList(34)
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

    override fun onLoadFinish() {
    }

    override fun updateBookingList(user: User) {
    }


}