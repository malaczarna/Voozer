package pl.voozer.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.voozer.service.model.Auth
import pl.voozer.service.model.Login
import pl.voozer.service.network.Api
import pl.voozer.ui.base.BaseController
import pl.voozer.utils.SharedPreferencesHelper

class LoginController : BaseController<LoginView>() {

    @SuppressLint("CheckResult")
    fun login(context: Context, body: Login) {
        api.login(body)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { token ->
                    run {
                        SharedPreferencesHelper.saveToken(context, token)
                        view.login()
                    }
                },
                { error: Throwable ->
                    Log.d("debugLogin", error.localizedMessage)
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