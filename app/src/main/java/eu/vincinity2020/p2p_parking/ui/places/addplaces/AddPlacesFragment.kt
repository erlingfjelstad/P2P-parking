package eu.vincinity2020.p2p_parking.ui.places.addplaces


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.crashlytics.android.Crashlytics
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.app.common.FragmentOnBackInterface
import eu.vincinity2020.p2p_parking.data.entities.locations.PlaceRequest
import eu.vincinity2020.p2p_parking.data.entities.places.Place
import eu.vincinity2020.p2p_parking.utils.gone
import eu.vincinity2020.p2p_parking.utils.hideKeyboard
import eu.vincinity2020.p2p_parking.utils.show
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_places.*
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.toast
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AddPlacesFragment : Fragment(), FragmentOnBackInterface, AddPlacesContract.View {

    @Inject
    lateinit var presenter: AddPlacesPresenter
    private val allDisposables = CompositeDisposable()
    private lateinit var placesClient: PlacesClient
    private var selectedPlace: Place? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.get(requireContext())
                .appComponent()
                .myPlacesComponentBuilder()
                .placesModule(AddPlacesModule())
                .build()
                .inject(this)
        Places.initialize(requireContext().applicationContext, getString(R.string.google_maps_key))
        placesClient = Places.createClient(requireContext())
        presenter.getAllPlaces(this)
        initViews()
    }

    private fun initViews() {

        crdPlaceSearch.gone()

        allDisposables.addAll(
                edtSearchPlaces.textChanges()
                        .observeOn(Schedulers.io())
                        .map { it.toString().trim() }
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .subscribe { searchQuery ->
                            val autoCompleteSessionToken = AutocompleteSessionToken.newInstance()
                            val predictionsRequest = FindAutocompletePredictionsRequest.builder()
                                    .setCountry("CA")
                                    .setTypeFilter(TypeFilter.ADDRESS)
                                    .setSessionToken(autoCompleteSessionToken)
                                    .setQuery(searchQuery)
                                    .build()

                            runOnUiThread { revSearchPlaces.show() }
                            placesClient.findAutocompletePredictions(predictionsRequest).addOnSuccessListener { response ->
                                val places = ArrayList(response.autocompletePredictions.map { Place(it.getFullText(null).toString(), it.placeId) })
                                runOnUiThread { setSearchResults(places) }
                            }.addOnFailureListener {
                                Crashlytics.logException(it)
                            }
                        },
                btnAddSelectedPlace.clicks()
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            addSelectedPlace()
                        }
        )


    }

    private fun addSelectedPlace() {
        if (selectedPlace == null) {
            toast("Please select a place")
        } else {
            selectedPlace?.let { presenter.savePlace(this, it) }
        }
    }

    fun setSearchResults(places: ArrayList<Place>) {
        if (places.isNotEmpty()) {
            crdPlaceSearch.show()
        } else {
            crdPlaceSearch.gone()
        }
        val searchAdapter = AddPlacesAdapter(places, { position ->
            selectedPlace = places[position]
        }, true)
        revSearchPlaces.layoutManager = LinearLayoutManager(context)
        revSearchPlaces.adapter = searchAdapter
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun handleSavePlaceSuccess() {
        toast("Place saved successfully")
        crdPlaceSearch.gone()
        requireContext().hideKeyboard(edtSearchPlaces)
        presenter.getAllPlaces(this)
    }

    override fun handleGetAllPlacesSuccess(places: ArrayList<PlaceRequest>) {
        val readablePlaces = ArrayList(places.map { Place(it.description, it.id.toString()) })
        val placesAdapter = AddPlacesAdapter(readablePlaces, { position ->

        })
        revSavedPlaced.layoutManager = LinearLayoutManager(context)
        revSavedPlaced.adapter = placesAdapter
    }

    override fun handleGetAllPlacesFailure() {
    }

    override fun onBackPressed(): Boolean {
        return if (crdPlaceSearch.visibility == View.VISIBLE) {
            revSearchPlaces.adapter = null
            crdPlaceSearch.gone()
            true
        } else {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
        allDisposables.dispose()
    }

}
