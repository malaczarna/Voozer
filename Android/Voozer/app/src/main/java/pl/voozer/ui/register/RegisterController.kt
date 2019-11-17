package pl.voozer.ui.register

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.voozer.scquiz.ui.register.RegisterView
import pl.voozer.service.model.Register
import pl.voozer.service.network.Api
import pl.voozer.ui.base.BaseController

class RegisterController : BaseController<RegisterView>() {

    @SuppressLint("CheckResult")
    fun register(body: Register) {
        api.register(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    run {
                        view.register()
                    }
                },
                { error: Throwable ->
                    Log.d("registerError", error.localizedMessage)
                }
            )
    }
}