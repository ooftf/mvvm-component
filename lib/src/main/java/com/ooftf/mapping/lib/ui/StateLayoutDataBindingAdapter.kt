package com.ooftf.mapping.lib.ui

import android.view.View
import android.widget.FrameLayout
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


    @JvmStatic
    @BindingAdapter(value = ["stateData",
        "emptyLayoutId", "loadLayoutId", "errorLayoutId", "firstLayoutId", "secondLayoutId", "thirdLayoutId",
        "emptyActionId", "errorActionId", "firstActionId", "secondActionId", "thirdActionId"], requireAll = false)
    fun setStateLayoutUiMapping(view: View,
                                data: IStateLayoutData?,
                                emptyLayoutId: Int?,
                                loadLayoutId: Int?,
                                errorLayoutId: Int?,
                                firstLayoutId: Int?,
                                secondLayoutId: Int?,
                                thirdLayoutId: Int?,
                                emptyActionId: Int?,
                                errorActionId: Int?,
                                firstActionId: Int?,
                                secondActionId: Int?,
                                thirdActionId: Int?) {
        if (data == null) {
            return
        }
        var observerState = view.getTag(R.id.observer_state)
        if (observerState == null) {
            observerState = Observer { integer: Int ->
                setStateLayoutUiMapping(view, integer, { data.refresh() }, { data.emptyAction() }, null, null, null,
                        emptyLayoutId, loadLayoutId, errorLayoutId, firstLayoutId, secondLayoutId, thirdLayoutId,
                        emptyActionId, errorActionId, firstActionId, secondActionId, thirdActionId)
            }
            view.setTag(R.id.observer_state, observerState)
            data.getLifecycleOwner()?.let {
                data.getStateLayout().observe(it, observerState)
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["stateLayout",
        "errorAction", "emptyAction", "firstAction", "secondAction", "thirdAction",
        "emptyLayoutId", "loadLayoutId", "errorLayoutId", "firstLayoutId", "secondLayoutId", "thirdLayoutId",
        "emptyActionId", "errorActionId", "firstActionId", "secondActionId", "thirdActionId"], requireAll = false)
    fun setStateLayoutUiMapping(view: View,
                                stateLayout: Int?,
                                errorAction: (() -> Unit)?,
                                emptyAction: (() -> Unit)?,
                                firstAction: (() -> Unit)?,
                                secondAction: (() -> Unit)?,
                                thirdAction: (() -> Unit)?,
                                emptyLayoutId: Int?,
                                loadLayoutId: Int?,
                                errorLayoutId: Int?,
                                firstLayoutId: Int?,
                                secondLayoutId: Int?,
                                thirdLayoutId: Int?,
                                emptyActionId: Int?,
                                errorActionId: Int?,
                                firstActionId: Int?,
                                secondActionId: Int?,
                                thirdActionId: Int?) {
        if (stateLayout == null) {
            return
        }
        val slsView =
                if (view.parent is StateLayoutSwitcher) {
                    view.parent as StateLayoutSwitcher
                } else {
                    StateLayoutSwitcher(view.context).apply {
                        setSuccessLayout(view)
                        emptyLayoutId?.let { setEmptyLayoutId(it) }
                        loadLayoutId?.let { setLoadLayoutId(it) }
                        errorLayoutId?.let { setErrorLayoutId(it) }
                        firstLayoutId?.let { setFirstLayoutId(it) }
                        secondLayoutId?.let { setSecondLayoutId(it) }
                        thirdLayoutId?.let { setThirdLayoutId(it) }

                        emptyActionId?.let { setEmptyActionId(it) }
                        errorActionId?.let { setRefreshActionId(it) }
                        firstActionId?.let { setFirstActionId(it) }
                        secondActionId?.let { setSecondActionId(it) }
                        thirdActionId?.let { setThirdActionId(it) }

                        errorAction?.let { setOnRetryListener(errorAction) }
                        emptyAction?.let { setEmptyAction(errorAction) }
                        firstAction?.let { setFirstAction(errorAction) }
                        secondAction?.let { setSecondAction(errorAction) }
                        thirdAction?.let { setThirdAction(errorAction) }
                        ViewHelper.replace(view, this, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                    }
                }
        StateLayoutSwitcher.setValue(slsView, stateLayout)
    }
}