package pl.voozer.ui.register

import android.os.Bundle
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
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
        MaterialDialog(this).show {
            title(R.string.register_account_type_title)
            message(R.string.register_account_type_message)
            cancelOnTouchOutside(false)
            noAutoDismiss()
            negativeButton(R.string.menu_driver) {
                this@RegisterActivity.tvAdditionalCarInfo.visibility = View.VISIBLE
                this@RegisterActivity. tilCarBrand.visibility = View.VISIBLE
                this@RegisterActivity.tilCarModel.visibility = View.VISIBLE
                this@RegisterActivity.tilCarColor.visibility = View.VISIBLE
                dismiss()
            }
            positiveButton(R.string.menu_passenger) {
                dismiss()
            }
        }
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