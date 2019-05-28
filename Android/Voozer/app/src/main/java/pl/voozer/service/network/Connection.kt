package pl.voozer.service.network

import android.content.Context
import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.LoggingInterceptor
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.voozer.BuildConfig
import pl.voozer.utils.BASE_URL
import pl.voozer.utils.SharedPreferencesHelper
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat

class Connection {

    data class Builder(
        var okHttpClient: OkHttpClient? = null,
        var retrofit: Retrofit? = null) {

            fun provideOkHttpClient(context: Context) = apply {
                val logging = LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(com.ihsanbal.logging.Level.BODY)
                    .log(Log.VERBOSE)
                    .request("Request")
                    .addHeader("authorization", SharedPreferencesHelper.getToken(context))
                    .response("Response")
                    .build()
                val client = OkHttpClient.Builder()
                client.addInterceptor(logging)
                client.addInterceptor(StethoInterceptor())
                this.okHttpClient = client.build()
            }

            fun provideRetrofit() = apply {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(
                        GsonBuilder().setDateFormat(DateFormat.FULL).create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(okHttpClient!!)
                    .build()
            }

            fun createApi() = retrofit!!.create<Api>(Api::class.java)
        }
}