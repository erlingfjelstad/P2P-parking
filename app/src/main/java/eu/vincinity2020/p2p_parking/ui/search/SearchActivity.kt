package eu.vincinity2020.p2p_parking.ui.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import eu.vincinity2020.p2p_parking.R
import eu.vincinity2020.p2p_parking.app.App
import eu.vincinity2020.p2p_parking.ui.choose.ChooseParkingSpotActivity
import eu.vincinity2020.p2p_parking.app.common.BaseActivity
import kotlinx.android.synthetic.main.acitivity_search.*
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

class SearchActivity : BaseActivity(), SearchView, SearchAdapter.OnSearchItemClickedListener, TextWatcher {

    @Inject
    lateinit var searchPresenter: SearchPresenter

    lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_search)

        App.get(this)
                .appComponent()
                .searchComponentBuilder()
                .searchModule(SearchModule())
                .build()
                .inject(this)

        initRecyclerView()
        initEditText()
    }

    private fun initEditText() {
        search_view_map.addTextChangedListener(this)
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(recycler_view_search.context, LinearLayoutManager.VERTICAL, false)
        recycler_view_search.layoutManager = linearLayoutManager

        adapter = SearchAdapter(this, this)
        recycler_view_search.adapter = adapter


        val searchResults = ArrayList<SearchResult>(3)
        searchResults.add(SearchResult("Bankgata 1", true, 69.648853, 18.961192))
        searchResults.add(SearchResult("Storgata 33", true, 69.646795, 18.952934))
        searchResults.add(SearchResult("Kirkegata 5", false, 69.648415, 18.957944))
        adapter.submitList(searchResults)

    }

    override fun onResume() {
        super.onResume()
        searchPresenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
        searchPresenter.detach()
    }

    override fun onClick(searchResult: SearchResult) {
        val intent = ChooseParkingSpotActivity
                .getLaunchIntent(this,
                        GeoPoint(searchResult.latitude, searchResult.longitude))
        startActivity(intent)
    }

    override fun afterTextChanged(editable: Editable?) {
        val query = editable.toString()

        if (query.length > 3) {
            searchPresenter.queryAddress(query)

        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onSearchResultRecieved(result: List<SearchResult>) {
        adapter.submitList(result)
    }

    override fun onUnknownError(errorMessage: String) {
    }

    override fun onTimeout() {
    }

    override fun onNetworkError() {
    }

    override fun onSearchStarted() {
        textview_empty_state_search.visibility = View.GONE
        progress_bar_loading.visibility = View.VISIBLE
        recycler_view_search.visibility = View.INVISIBLE
    }

    override fun onSearchFinished() {
        progress_bar_loading.visibility = View.GONE
        recycler_view_search.visibility = View.VISIBLE
        textview_empty_state_search.visibility = View.GONE
    }

    override fun renderEmptyState() {
        textview_empty_state_search.visibility = View.VISIBLE
        recycler_view_search.visibility = View.GONE
    }

}