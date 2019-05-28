package pl.voozer.ui.login

import pl.voozer.service.model.Auth
import pl.voozer.ui.base.BaseView

interface LoginView: BaseView {
    fun login()
}