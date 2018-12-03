package eu.vincinity2020.p2p_parking.ui.search

import eu.vincinity2020.p2p_parking.app.network.OpenStreetMapService
import eu.vincinity2020.p2p_parking.app.common.MvpView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchPresenterImpl(private val openStreetMapService: OpenStreetMapService) : SearchPresenter {

    var searchView: SearchView? = null
    val compositeDisposable = CompositeDisposable()

    override fun attach(view: MvpView) {
        searchView = view as SearchView
    }

    override fun detach() {
        searchView = null
    }

    override fun queryAddress(query: String) {
        compositeDisposable.add(
                openStreetMapService.search(query)
                        .debounce(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { _ ->
                            searchView?.onSearchStarted()
                        }
                        .observeOn(Schedulers.io())
                        .map {
                            return@map SearchResult.fromDTO(it)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter { t: List<SearchResult> ->
                            if (t.isEmpty()) {
                                searchView?.onSearchFinished()
                                searchView?.renderEmptyState()
                                return@filter false
                            } else return@filter true
                        }
                        .subscribe { result ->
                            searchView?.onSearchResultRecieved(result)
                            searchView?.onSearchFinished()
                        }
        )
    }
}