package eu.vincinity2020.p2p_parking.app.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import eu.vincinity2020.p2p_parking.data.dto.UserDTO
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface NetworkService {

//    , @Query("email") email: String, @Query("password") password: String
    @GET("/user/login")
    fun loginUser(@Header("Authorization") basicAuth: String): Observable<JsonObject>



    @Headers("ContainType: application/json")
    @PUT("/user/update/deviceId")
    fun saveFcmToken(@Body fcmToken: JsonObject) : Observable<JsonObject>           //Save FCM token of device on server


    @Headers("ContainType: application/json")
    @POST("/user/register")
    fun registerUser(@Body registerJson: JsonObject): Observable<JsonObject>



    @Headers("ContainType: application/json")
    @GET("/user/{id}")
    fun getUser(@Path("id") userId: String): Observable<JsonObject>                  //Returns country listing


    @GET("/user/logout")
    fun logout(): Observable<JsonObject>

    @GET("/sensor/list")
    fun getParkingSites(): Observable<JsonObject>              //Without using user's location

    @GET("sensor/nearBy")
    fun getParkingSites(@Query("lat") latitude: String, @Query("lon") longitude: String,
                        @Query("limit") limit: String): Observable<JsonObject>


    @GET("/country/list")
    fun getCountries(): Observable<JsonObject>                  //Returns country listing


    @Headers("ContainType: application/json")
    @POST("/location/save")
    fun saveMyLocation(@Body locationObject: JsonObject): Observable<JsonObject>




    @GET("/vehicleType/list")
    fun getVehicleTypeList(): Observable<JsonObject>                  //Returns Vehicle Type listing

}