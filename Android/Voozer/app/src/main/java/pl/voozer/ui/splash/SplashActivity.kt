package pl.voozer.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import pl.voozer.R
import pl.voozer.service.network.Connection
import pl.voozer.service.notification.NotificationService
import pl.voozer.ui.base.BaseActivity
import pl.voozer.ui.login.LoginActivity
import pl.voozer.utils.SPLASH_DELAY

class SplashActivity : BaseActivity<SplashController, SplashView>(), SplashView {

    override fun initController() {
        controller = SplashController()
        controller.view = this
        controller.api = Connection.Builder().provideOkHttpClient().provideRetrofit().createApi()
        controller.sharedPreferences = this.getSharedPreferences("123", Context.MODE_PRIVATE)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNotificationChannel()
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_DELAY)
    }

    private fun initNotificationChannel() {
        NotificationService.createChannel(this)
    }
}