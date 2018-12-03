package eu.vincinity2020.p2p_parking.app.network

import eu.vincinity2020.p2p_parking.ui.search.Address
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenStreetMapService {

    @GET("search/?format=json")
    fun search(@Query("q") query: String): Observable<List<Address>>
}