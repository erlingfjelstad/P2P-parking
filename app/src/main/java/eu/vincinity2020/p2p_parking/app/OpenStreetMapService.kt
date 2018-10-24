package eu.vincinity2020.p2p_parking.app

import eu.vincinity2020.p2p_parking.search.Address
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenStreetMapService {

    @GET("search/?format=json")
    fun search(@Query("q") query: String): Observable<List<Address>>
}