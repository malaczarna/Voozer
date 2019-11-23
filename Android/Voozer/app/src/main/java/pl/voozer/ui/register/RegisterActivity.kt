package pl.voozer.ui.register

import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_register.*
import pl.voozer.R
import pl.voozer.scquiz.ui.register.RegisterView
import pl.voozer.service.model.Profile
import pl.voozer.service.model.Register
import pl.voozer.service.model.Roles
import pl.voozer.service.network.Connection
import pl.voozer.ui.base.BaseActivity

class RegisterActivity : BaseActivity<RegisterController, RegisterView>(), RegisterView {

    override fun register() {
        hideProgressDialog()
        finish()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_register
    }

    override fun initController() {
        controller = RegisterController()
        controller.view = this
        controller.api = Connection.Builder().provideOkHttpClient(applicationContext).provideRetrofit().createApi()!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnRegister.setOnClickListener {
            showProgressDialog()
            controller.register(
                Register(
                    tilName.editText?.text.toString(),
                    tilEmail.editText?.text.toString(),
                    tilPassword.editText?.text.toString(),
                    listOf(Roles(Profile.PASSENGER), Roles(Profile.DRIVER))
                )
            )
        }
    }

}