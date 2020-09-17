package com.ooftf.mapping.lib.ui

import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import com.ooftf.mapping.lib.R
import com.ooftf.widget.statelayout.StateLayoutSwitcher

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/9/17
 */
object StateLayoutDataBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["data"], requireAll = false)
    fun setStateLayoutUiMapping(view: StateLayoutSwitcher, data: IStateLayoutData?) {
        if (data == null) {
            return
        }
        view.setOnRetryListener {
            data.refresh()
        }
        view.setEmptyAction {
            data.emptyAction()
        }
        var observerState = view.getTag(R.id.observer_state)
        if (observerState == null) {
            observerState = Observer<Int> { integer: Int ->
                StateLayoutSwitcher.setValue(view, integer)
            }
            view.setTag(R.id.observer_state, observerState)
            data.getLifecycleOwner()?.let {
                data.getStateLayout().observe(it, observerState)
            }
        }
    }
}