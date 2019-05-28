package pl.voozer.ui.base

import pl.voozer.service.network.Api

abstract class BaseController<V: BaseView> {

    lateinit var view: V
    lateinit var api: Api
}