package eu.vincinity2020.p2p_parking.ui.places.listplaces


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.google.maps.model.LatLng
import com.jakewharton.rxbinding3.view.clicks
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.data.entities.directions.UserStop
import eu.vincinity2020.p2p_parking.data.entities.locations.PlaceRequest
import eu.vincinity2020.p2p_parking.utils.P2PDialog
import eu.vincinity2020.p2p_parking.utils.gone
import eu.vincinity2020.p2p_parking.utils.show
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_places_list.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class PlacesListFragment : Fragment(), PlacesListContract.View {

    companion object {
        fun newInstance(listener: PlacesListListener): PlacesListFragment {
            val fragment = PlacesListFragment()
            fragment.attachListener(listener)
            return fragment
        }
    }

    private var listener: PlacesListListener? = null
    private val allDisposables = CompositeDisposable()
    @Inject
    lateinit var presenter: PlacesListPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        App.get(requireContext())
                .appComponent()
                .placeListComponentBuilder()
                .placesModules(PlacesListModule())
                .build()
                .inject(this)
        presenter.fetchPlaces(this)
    }

    private fun initViews() {
        allDisposables.addAll(
                imvSearchPlacesList.clicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            listener?.onSearchClicked()
                        },
                linSetDestinationPlaceList.clicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val adapter = revPlacesList.adapter as PlacesListAdapter
                            listener?.onSetDestination(adapter.getSelectedPlaceObjects().map { UserStop(it.description, LatLng(it.lat, it.long)) })
                        },
                linShowRoutePlaceList.clicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val adapter = revPlacesList.adapter as PlacesListAdapter
                            listener?.onShowRoute(adapter.getSelectedPlaceObjects().map { UserStop(it.description, LatLng(it.lat, it.long)) })
                        }
        )
    }

    override fun handleFetchPlacesSuccess(places: ArrayList<PlaceRequest>) {
        val adapter = PlacesListAdapter(places) { selectedPlaces ->
            TransitionManager.beginDelayedTransition(crdControlsPlacesList)
            if (selectedPlaces.isEmpty()) {
                crdControlsPlacesList.gone()
            } else {
                crdControlsPlacesList.show()
            }
        }
        revPlacesList.layoutManager = LinearLayoutManager(context)
        revPlacesList.adapter = adapter

    }

    override fun handleFailure(message: String) {
        P2PDialog.errorDialog(requireContext(), message).show()
    }

    fun attachListener(listener: PlacesListListener) {
        this.listener = listener
    }

    override fun onDestroy() {
        super.onDestroy()
        allDisposables.dispose()
        presenter.detach()
    }

}

interface PlacesListListener {
    fun onSearchClicked()

    fun onSetDestination(places: List<UserStop>)

    fun onShowRoute(places: List<UserStop>)
}