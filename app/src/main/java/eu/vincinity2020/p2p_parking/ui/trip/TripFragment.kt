package eu.vincinity2020.p2p_parking.ui.trip

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar

class TripFragment : BaseFragment() {

    @BindView(R.id.viewpager_trip)
    lateinit var viewPager: ViewPager

    @BindView(R.id.tab_layout_trip)
    lateinit var tabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trip, container, false)
    }

    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withHamburger()
            .withTitle(R.string.bottom_navigation_trips)
            .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        val adapter = TripPageAdapter(requireFragmentManager())

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}