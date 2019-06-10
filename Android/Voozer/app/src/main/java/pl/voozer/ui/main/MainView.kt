package pl.voozer.ui.main

import pl.voozer.service.model.Position
import pl.voozer.service.model.User
import pl.voozer.ui.base.BaseView

interface MainView : BaseView {
    fun updatePosition(position: Position)
    fun updateUser(user: User)
    fun updateDrivers(drivers: List<User>)
}