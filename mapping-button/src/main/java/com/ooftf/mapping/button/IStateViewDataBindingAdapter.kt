package com.ooftf.mapping.button

import android.graphics.Color
import androidx.databinding.BindingAdapter
import com.ooftf.mapping.lib.ui.UIEvent

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/1/26
 */
object IStateViewDataBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["mappingState", "loadingColor"], requireAll = false)
    fun status(view: MappingButton<*>, state: Int?, loadingColor: Int?) {

        loadingColor?.let {
            view.setLoadingColor(loadingColor)
        }
        when (state) {
            UIEvent.Single.LOADING -> {
                view.toLoading()
            }
            UIEvent.Single.SUCCESS -> {
                view.toSuccess()
            }
            UIEvent.Single.FAIL -> {
                view.toFail()
            }
        }

    }
    @JvmStatic
    @BindingAdapter(value = ["mappingState", "loadingColor"], requireAll = false)
    fun status(view: MappingButton<*>, state: Int?, loadingColor: String?) {
        loadingColor?.let {
            view.setLoadingColor(Color.parseColor(loadingColor))
        }
        when (state) {
            UIEvent.Single.LOADING -> {
                view.toLoading()
            }
            UIEvent.Single.SUCCESS -> {
                view.toSuccess()
            }
            UIEvent.Single.FAIL -> {
                view.toFail()
            }
        }
    }
}