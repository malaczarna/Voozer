package pl.voozer.service.network

import io.reactivex.Observable
import pl.voozer.service.model.*
import pl.voozer.service.model.direction.Direction
import retrofit2.http.*

interface Api {

    @POST("auth/login")
    fun login(@Body body: Login): Observable<Auth>

    @POST("users/register-command")
    fun register(@Body body: Register): Observable<RegisterResponse>

    @GET("users/position")
    fun getPosition(): Observable<Position>

    @POST("users/position")
    fun setPosition(@Body position: Position): Observable<Unit?>

    @POST("users/change-profile-command")
    fun setProfile(): Observable<Unit?>

    @GET("users/info")
    fun getUser(): Observable<User>

    @GET("users/{userId}")
    fun getSpecificUser(@Path("userId") id: String): Observable<User>

    @POST("destinations")
    fun setDestination(@Body destination: Destination): Observable<Unit?>

    @GET("destinations")
    fun getDestination(): Observable<Unit?>

    @POST("users/stop-destination-command")
    fun stopDestination(): Observable<Unit?>

    @GET("users/active/drivers")
    fun getDrivers(): Observable<List<User>>

//    @POST("users/active/drivers/get-in-radius-command")
//    fun getDrivers(@Body radius: Double): Observable<List<User>>

    @POST("fcm")
    fun setFirebaseToken(@Body token: String): Observable<Unit?>

    @POST("notifications")
    fun setNotification(@Body notificationMessage: NotificationMessage): Observable<Unit?>

    @GET("maps/api/directions/json")
    fun getDirections(@Query("origin") origin: String,
                      @Query("destination") destination: String,
                      @Query("key") key: String): Observable<Direction>
}