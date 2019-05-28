package pl.voozer.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment<C: BaseController<V>, V: BaseView>: Fragment(), BaseView {

    lateinit var controller: C

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initController()
        return inflater.inflate(getLayoutResId(), container, false)
    }

    protected abstract fun getLayoutResId(): Int

    protected abstract fun initController()
}