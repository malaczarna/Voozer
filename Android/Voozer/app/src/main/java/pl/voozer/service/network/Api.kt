package pl.voozer.service.network

import io.reactivex.Observable
import pl.voozer.service.model.*
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
}