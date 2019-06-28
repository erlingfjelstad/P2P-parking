package eu.vincinity2020.p2p_parking.ui.dashboard.home


import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import eu.vincinity2020.p2p_parking.R
import kotlinx.android.synthetic.main.fragment_home.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

class HomeFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        initMap()
    }

    private fun initMap() {
        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
        mvHome.setTileSource(TileSourceFactory.MAPNIK)
        mvHome.setBuiltInZoomControls(false)
        mvHome.setMultiTouchControls(true)
        val mapController = mvHome.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(69.649190, 18.955182)
        mapController.setCenter(startPoint)
    }

    override fun onResume() {
        super.onResume()
        mvHome.onResume()
    }

    override fun onPause() {
        super.onPause()
        mvHome.onPause()
    }

}
