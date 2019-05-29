package pl.voozer.ui.login

import pl.voozer.service.model.Auth
import pl.voozer.service.model.User
import pl.voozer.ui.base.BaseView

interface LoginView: BaseView {
    fun login()
    fun updateUser(user: User)
}