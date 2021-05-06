package com.ooftf.mapping.lib.ui

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.ooftf.mapping.lib.HttpUiMapping
import com.ooftf.mapping.lib.LogUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/6/13 0013
 */
class BaseLiveDataObserve(private var liveData: BaseLiveData, private var owner: LifecycleOwner, activity: Activity) {
    constructor(liveData: BaseLiveData, activity: AppCompatActivity) : this(liveData, activity, activity)
    constructor(liveData: BaseLiveData, fragment: Fragment) : this(liveData, fragment, fragment.activity!!)

    private var loadingDialog: HttpUiMapping.MyDialogInterface? = null

    fun createDialog(activity: Activity): HttpUiMapping.MyDialogInterface {
        var dialog = HttpUiMapping.provider.createLoadingDialog(activity)
        dialog.setOnCancelListener {
            (dialog.getWindow().decorView.tag)?.let {
                (it as? List<Cancelable>)?.forEach { item ->
                    item.cancel()
                }
            }
        }
        return dialog
    }

    private val smarts = ArrayList<SmartRefreshLayout>()

    init {

        liveData.invalidateBinding.observe(owner) {
            (owner as? Fragment)?.let {
                it.view?.let { fragmentView ->
                    DataBindingUtil.findBinding<ViewDataBinding>(fragmentView)?.notifyChange()
                }
            }

            (owner as? Activity)?.let {
                DataBindingUtil
                        .findBinding<ViewDataBinding>(it.findViewById<ViewGroup>(android.R.id.content)
                                .getChildAt(0))
                        ?.invalidateAll()
            }
        }

        liveData.finishLiveData.observe(owner, { integer ->
            activity.setResult(integer)
            activity.finish()
        })
        liveData.showLoading.observe(owner, { calls ->
            if (calls.size > 0) {
                if (loadingDialog == null) {
                    loadingDialog = createDialog(activity)
                }
                loadingDialog?.let {
                    it.getWindow().decorView.tag = calls
                    it.show()
                }
            } else {
                loadingDialog?.dismiss()
                loadingDialog = null
            }

        })
        liveData.startActivityLiveData.observe(owner, { postcard -> postcard.navigation(activity) })
        liveData.messageLiveData.observe(owner, { HttpUiMapping.provider.toast(it) })


        liveData.finishWithData.observe(owner, {
            val intent = Intent()
            intent.putExtra(DEFAULT_RESULT_DATA, it.data)
            activity.setResult(it.code, intent)
            if (it.isFinish) {
                activity.finish()
            }
        })
        liveData.finishActivity.observe(owner, {
            val intent = Intent()
            intent.replaceExtras(it.data)
            activity.setResult(it.code, intent)
            if (it.isFinish) {
                activity.finish()
            }
        })
        setSmartObserve()
    }

    fun bindSmartRefreshLayout(smartRefreshLayout: SmartRefreshLayout): BaseLiveDataObserve {
        smarts.add(smartRefreshLayout)
        return this
    }

    private fun setSmartObserve() {
        liveData.smartRefresh.observe(owner, Observer { integer ->
            if (integer == 0) {
                smarts.forEach {
                    if (it.state == RefreshState.Refreshing) {
                        it.finishRefresh(0, true, null)
                        LogUtil.e("finishRefresh  ok")
                    } else {
                        LogUtil.e("finishRefresh  no")
                    }
                }
            }
        })

        liveData.smartLoadMore.observe(owner, Observer { integer ->
            if (integer == UIEvent.SMART_LAYOUT_LOADMORE_FINISH) {
                smarts.forEach {
                    it.resetNoMoreData()
                    it.finishLoadMore()
                }
            } else if (integer == UIEvent.SMART_LAYOUT_LOADMORE_FINISH_AND_NO_MORE) {
                smarts.forEach {
                    it.finishLoadMoreWithNoMoreData()
                }
            }
        })
    }

    companion object {
        const val DEFAULT_RESULT_DATA = "data"
    }

}
