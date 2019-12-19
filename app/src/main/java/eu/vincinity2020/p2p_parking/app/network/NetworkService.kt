package eu.vincinity2020.p2p_parking.app.network

import com.google.gson.JsonObject
import eu.vincinity2020.p2p_parking.data.entities.GenericResponse
import eu.vincinity2020.p2p_parking.data.entities.LoginResponse
import eu.vincinity2020.p2p_parking.data.entities.RegisterRequest
import eu.vincinity2020.p2p_parking.data.entities.SaveFcmTokenRequest
import eu.vincinity2020.p2p_parking.data.entities.locations.PlaceRequest
import eu.vincinity2020.p2p_parking.data.entities.locations.PlacesResponse
import eu.vincinity2020.p2p_parking.data.entities.parking.ParkingSpotResponse
import eu.vincinity2020.p2p_parking.data.entities.user.EditUserRequest
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface NetworkService {

    //    , @Query("email") email: String, @Query("password") password: String
    @GET("/user/login")
    fun loginUser(@Header("Authorization") basicAuth: String): Single<LoginResponse?>


    @Headers("ContainType: application/json")
    @PUT("/user/update/deviceId")
    fun saveFcmToken(@Header("Authorization") basicAuth: String, @Body request: SaveFcmTokenRequest): Single<JsonObject>           //Save FCM token of device on server


    @Headers("ContainType: application/json")
    @POST("/user/register")
    fun registerUser(@Body registerJson: RegisterRequest): Single<GenericResponse>

    @Headers("ContainType: application/json")
    @PUT("/user/save")
    fun editUser(@Body editUserRequest: EditUserRequest): Single<GenericResponse>


    @Headers("ContainType: application/json")
    @GET("/user/{id}")
    fun getUser(@Path("id") userId: String): Observable<JsonObject>                  //Returns country listing


    @GET("/user/logout")
    fun logout(): Observable<JsonObject>

    @GET("/sensor/list")
    fun getParkingSites(@Header("Authorization") basicAuth: String): Observable<JsonObject>              //Without using user's location

    @GET("sensor/nearBy")
    fun getParkingSites(@Header("Authorization") basicAuth: String, @Query("lat") latitude: String,
                        @Query("lon") longitude: String, @Query("limit") limit: String): Observable<JsonObject>


    @GET("/booking/list")
    fun getParkingBookings(@Header("Authorization") basicAuth: String, @Query("limit") limit: String,
                           @Query("userId") userId: String): Observable<JsonObject>


    @Headers("ContainType: application/json")
    @POST("/booking/bookSpace")
    fun bookParkingSpace(@Header("Authorization") basicAuth: String, @Body bookDetails: JsonObject): Observable<JsonObject>


    @GET("/country/list")
    fun getCountries(): Observable<JsonObject>                  //Returns country listing


    @Headers("ContainType: application/json")
    @POST("/location/save")
    fun saveMyLocation(@Header("Authorization") basicAuth: String, @Body locationObject: JsonObject): Observable<JsonObject>

    @Headers("ContainType: application/json")
    @POST("/location/save")
    fun saveMyLocation(@Header("Authorization") basicAuth: String, @Body locationObject: PlaceRequest): Observable<GenericResponse>


    @Headers("ContainType: application/json")
    @GET("location/list")
    fun getSavedLocations(@Header("Authorization") basicAuth: String,
                          @Query("userId") userId: String, @Query("limit") limit: String = "20"): Observable<PlacesResponse>                  //Returns all vehicles of this user

    @Headers("ContainType: application/json")
    @DELETE("/location/delete/{id}")
    fun deleteLocation(@Header("Cookie") token: String,
                       @Path("id") locationId: String): Observable<GenericResponse>

    @GET("/vehicleType/list")
    fun getVehicleTypeList(): Observable<JsonObject>                  //Returns Vehicle Type listing


    @Headers("ContainType: application/json")
    @PUT("/vehicle/saveAsDefaultVehicle")
    fun updateDefaultVehicle(@Header("Authorization") basicAuth: String, @Body vehicleDetails: JsonObject): Observable<JsonObject>


    @Headers("ContainType: application/json")
    @GET("/vehicle/ownerVehicles/{ownerId}")
    fun getVehicles(@Header("Authorization") basicAuth: String, @Path("ownerId") userId: String): Observable<JsonObject>                  //Returns all vehicles of this user


    @Headers("ContainType: application/json")
    @POST("/vehicle/addNewVehicle")
    fun saveNewVehicle(@Header("Authorization") basicAuth: String, @Body vehicleObject: JsonObject): Observable<JsonObject>

    @Headers("ContainType: application/json")
    @GET("/booking/getNearByParkingSensor")
    fun getParkingSpots(@Query("lat") lat: Double,
                        @Query("lng") lng:Double,
                        @Query("limit") limit:Int = 25): Observable<ParkingSpotResponse>
}