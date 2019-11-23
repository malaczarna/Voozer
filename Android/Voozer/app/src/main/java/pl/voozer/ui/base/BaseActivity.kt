package pl.voozer.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import pl.voozer.R

abstract class BaseActivity<C: BaseController<V>, V: BaseView>: AppCompatActivity(), BaseView {

    lateinit var controller: C
    private lateinit var progressDialog: MaterialDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initController()
        initProgressDialog()
    }

    private fun initProgressDialog() {
        progressDialog = MaterialDialog(this)
            .customView(R.layout.popup_loading, scrollable = true)
            .noAutoDismiss()
            .cancelOnTouchOutside(false)
    }

    fun showProgressDialog() {
        progressDialog.show()
    }

    fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    protected abstract fun getLayoutResId(): Int

    protected abstract fun initController()
}