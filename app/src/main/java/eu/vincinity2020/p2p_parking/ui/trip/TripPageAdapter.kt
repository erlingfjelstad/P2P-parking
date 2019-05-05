package eu.vincinity2020.p2p_parking.ui.trip

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class TripPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    companion object {
        const val NUM_PAGES = 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingTripsFragment()
            1 -> RecentTripsFragment()
            else -> UpcomingTripsFragment()
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> {
                "Upcoming trips"
            }
            1 -> {
                "Recent trips"
            }
            else -> ""
        }
    }
}