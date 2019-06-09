package pl.voozer.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import pl.voozer.R
import pl.voozer.service.model.Login
import pl.voozer.service.model.Profile
import pl.voozer.service.model.User
import pl.voozer.service.network.Connection
import pl.voozer.ui.base.BaseActivity
import pl.voozer.ui.main.MainActivity
import pl.voozer.ui.register.RegisterActivity
import pl.voozer.utils.SharedPreferencesHelper

class LoginActivity : BaseActivity<LoginController, LoginView>(), LoginView {

    override fun updateUser(user: User) {
        when(user.profile) {
            Profile.PASSENGER -> {
                SharedPreferencesHelper.setMainTheme(applicationContext, true)
            }
            Profile.DRIVER -> {
                SharedPreferencesHelper.setMainTheme(applicationContext, false)
            }
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun login() {
        controller.api = Connection.Builder().provideOkHttpClient(applicationContext).provideRetrofit().createApi()
        controller.loadUser()
    }

    override fun initController() {
        controller = LoginController()
        controller.view = this
        controller.api = Connection.Builder().provideOkHttpClient().provideRetrofit().createApi()
        controller.sharedPreferences = this.getSharedPreferences("123", Context.MODE_PRIVATE)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnLogin.setOnClickListener {
            controller.login(
                Login(
                    tilEmail.editText?.text.toString(),
                    tilPassword.editText?.text.toString())
            )
        }
        btnRedirectToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}