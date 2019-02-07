package eu.vincinity2020.p2p_parking.ui.mylocations.locationlist

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.OnClick
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.BaseFragment
import eu.vincinity2020.p2p_parking.data.entities.MyLocation
import eu.vincinity2020.p2p_parking.ui.mylocations.addlocation.AddLocationFragment
import eu.vincinity2020.p2p_parking.ui.navigation.NavigationActivity
import eu.vincinity2020.p2p_parking.utils.AndroidUtils
import eu.vincinity2020.p2p_parking.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.fragment_my_location_list.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject
import kotlin.collections.ArrayList

class MyLocationListFragment : BaseFragment(), MyLocationListMvpView {


    @Inject
    lateinit var presenter: MyLocationListPresenter

    val list: ArrayList<MyLocation> = ArrayList()


    override fun builder(): FragmentToolbar = FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withHamburger()
            .withTitle(R.string.my_location)
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_location_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMap()


        btnAddLocation.setOnClickListener {
            (activity as NavigationActivity).selectedFragmentTag = NavigationActivity.ADD_LOCATION_FRAGMENT
            AndroidUtils.attachFragment((activity as NavigationActivity).supportFragmentManager, AddLocationFragment(),
                    R.id.fragment_container, (activity as NavigationActivity).selectedFragmentTag, true)

        }
        rvMyLocations.layoutManager = LinearLayoutManager(view.context)
        rvMyLocations.adapter = MyLocationsAdapter(view.context, list)
    }

    override fun onResume() {
        super.onResume()
        App.get(requireContext())
                .appComponent()
                .myLocationListComponentBuilder()
                .myLocationListModule(MyLocationListModule())
                .build()
                .inject(this)

        presenter.attach(this)
        presenter.getAllLocations(App.get(context!!).getUser()!!)
        progress.visibility = View.VISIBLE
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }


    private fun initMap() {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(false)
        map.setMultiTouchControls(true)
        val mapController = map.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(69.649190, 18.955182)
        mapController.setCenter(startPoint)

//        addParkingSpotsToMap()
    }

    private fun addParkingSpotsToMap(myLocations: ArrayList<MyLocation>) {
        val parkingIcon = requireActivity().getDrawable(R.drawable.ic_local_parking_black_24dp)

        myLocations.forEach { location: MyLocation ->
            val marker = Marker(map)
            marker.position = GeoPoint(location.lat, location.lon)
            marker.title = location.description
            marker.id = location.id.toString()
            marker.icon = parkingIcon
//            marker.setOnMarkerClickListener(this)

            map.overlays.add(marker)
        }
    }
    @OnClick(R.id.tvMapView)
    fun showMap()
    {
        tvMapView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark))
        tvListView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_light))
        map.visibility = View.VISIBLE
        rvMyLocations.visibility = View.GONE
    }


    @OnClick(R.id.tvListView)
    fun showList()
    {
        tvMapView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_light))
        tvListView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_blue_dark))
        map.visibility = View.GONE
        rvMyLocations.visibility = View.VISIBLE
    }

    override fun onUnknownError(errorMessage: String) {
        progress.visibility = View.GONE
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onTimeout() {
        progress.visibility = View.GONE
        Toast.makeText(context, resources.getString(R.string.unable_to_connect_to_server), Toast.LENGTH_SHORT).show()
    }

    override fun onNetworkError() {
        progress.visibility = View.GONE
        Toast.makeText(context, resources.getString(R.string.unable_to_connect_to_server), Toast.LENGTH_SHORT).show()
    }

    override fun onLoadFinish() {
        progress.visibility = View.GONE
        rvMyLocations.visibility = View.VISIBLE
    }


    override fun updateLocations(myLocations: ArrayList<MyLocation>) {
        list.clear()
        list.addAll(myLocations)
        rvMyLocations.adapter!!.notifyDataSetChanged()
        addParkingSpotsToMap(myLocations)
    }

//    @OnClick(R.id.btnAddLocation)
//    fun onRegisterUserButtonClicked() {
//        Navigation.findNavController(btnAddLocation).navigate(R.id.action_myLocationListFragment_to_addLocationFragment)
//    }


}