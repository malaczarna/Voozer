package pl.voozer.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<C: BaseController<V>, V: BaseView>: AppCompatActivity(), BaseView {

    lateinit var controller: C

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initController()
    }

    protected abstract fun getLayoutResId(): Int

    protected abstract fun initController()
}