package pl.voozer.ui.login

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.voozer.service.model.Auth
import pl.voozer.service.model.Login
import pl.voozer.service.network.Api
import pl.voozer.ui.base.BaseController

class LoginController : BaseController<LoginView>() {

    lateinit var sharedPreferences: SharedPreferences

    private fun saveToken(token: Auth){
        val editor = sharedPreferences.edit()
        editor.putString("token", "Bearer ${token.token}")
        editor.apply()
    }


    @SuppressLint("CheckResult")
    fun login(body: Login) {
        api.login(body)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { token ->
                    run {
                        saveToken(token)
                        view.login()
                    }
                },
                { error: Throwable ->
                    Log.d("debugLogin", "Cannot login")
                }
            )
    }

    @SuppressLint("CheckResult")
    fun loadUser() {
        api.getUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { user -> view.updateUser(user) },
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }
}