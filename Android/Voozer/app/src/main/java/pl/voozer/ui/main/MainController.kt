package pl.voozer.ui.main

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.voozer.service.model.Position
import pl.voozer.ui.base.BaseController

class MainController : BaseController<MainView>() {

    @SuppressLint("CheckResult")
    fun loadPosition() {
        api.getPosition()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { position ->
                    view.updatePosition(position = position)
                },
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun sendPosition(position: Position) {
        api.setPosition(position = position)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun loadUser() {
        api.getUser()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {user -> view.updateUser(user)},
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun changeProfile() {
        api.setProfile()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {view.reloadUser()},
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }
}