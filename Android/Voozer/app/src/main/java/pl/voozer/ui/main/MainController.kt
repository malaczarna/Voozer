package pl.voozer.ui.main

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.voozer.service.model.Destination
import pl.voozer.service.model.Position
import pl.voozer.service.network.Api
import pl.voozer.ui.base.BaseController

class MainController : BaseController<MainView>() {

    @SuppressLint("CheckResult")
    fun loadPosition() {
        api.getPosition()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { position -> view.updatePosition(position = position) },
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun sendPosition(position: Position) {
        api.setPosition(position = position)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
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
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { user -> view.updateUser(user) },
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun changeProfile() {
        api.setProfile()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {},
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun setDestination(destination: Destination) {
        api.setDestination(destination = destination)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {},
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun loadDrivers() {
        api.getDrivers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { drivers -> view.updateDrivers(drivers) },
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun finishDestination() {
        api.stopDestination()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {},
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun loadDirection(api: Api, origin: String, destination: String, key: String) {
        api.getDirections(origin = origin, destination = destination, key = key)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { direction -> view.setRoute(direction)},
                { error: Throwable ->
                    Log.d("Error", error.localizedMessage)
                }
            )
    }
}