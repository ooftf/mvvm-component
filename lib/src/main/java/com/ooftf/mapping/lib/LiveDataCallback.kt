package com.ooftf.mapping.lib

import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import com.ooftf.mapping.lib.ui.BaseLiveData
import com.ooftf.mapping.lib.ui.CallOwner
import org.json.JSONException
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/7/22 0022
 */
open class LiveDataCallback<T : BaseResponse> : BaseCallback<T>, CallOwner {
    override fun getCall(): Call<*>? {
        return mCall
    }

    fun setCall(call: Call<*>?) {
        this.mCall = call
    }

    private var baseLiveData: BaseLiveData? = null
    private var successData: MutableLiveData<T>? = null
    private var bindSmart = false
    private var bindDialog = false
    private var bindStateLayout = false

    constructor() : super()
    constructor(baseLiveData: BaseLiveData) : super() {
        this.baseLiveData = baseLiveData
    }

    constructor(successData: MutableLiveData<T>) : super() {
        this.successData = successData
    }

    constructor(baseLiveData: BaseLiveData?, successData: MutableLiveData<T>) : super() {
        this.baseLiveData = baseLiveData
        this.successData = successData
    }

    fun bindSmart(): LiveDataCallback<T> {
        baseLiveData?.startRefresh()
        bindSmart = true
        return this
    }

    fun bindDialog(call: CallOwner): LiveDataCallback<T> {
        baseLiveData?.showDialog(call)
        bindDialog = true
        return this
    }

    fun bindDialog(): LiveDataCallback<T> {
        return bindDialog(this)
    }

    fun bindStateLayout(): LiveDataCallback<T> {
        baseLiveData?.switchToLoading()
        bindStateLayout = true
        return this
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        t.printStackTrace()
        if (bindDialog) {
            baseLiveData?.dismissDialog(this)
        }

        if (bindSmart) {
            baseLiveData?.finishLoadMore()
            baseLiveData?.finishRefresh()
        }

        if (bindStateLayout) {
            baseLiveData?.switchToError()
        }
        var message: String = when (t) {
            is TimeoutException, is SocketTimeoutException -> "请求超时，请重试"
            is JSONException -> "数据异常，请重试"
            is IOException -> {
                if (t.message == "Canceled") {
                    "网络请求已取消"
                } else {
                    "网络连接错误，请重试"
                }
            }
            else -> "网络连接错误，请重试"
        }

        baseLiveData?.showMessage(message)

    }

    var mCall: Call<*>? = null

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (bindDialog) {
            baseLiveData?.dismissDialog(this)
        }
        if (bindSmart) {
            baseLiveData?.finishLoadMore()
            baseLiveData?.finishRefresh()
        }
        super.onResponse(call, response)
    }

    override fun onResponseFailure(call: Call<T>, body: T) {
        baseLiveData?.showMessage(body.msg)
        if (bindStateLayout) {
            baseLiveData?.switchToError()
        }
    }


    override fun onResponseLoginStatusError(body: T) {
        if (bindStateLayout) {
            baseLiveData?.switchToError()
        }
        super.onResponseLoginStatusError(body)

    }

    override fun onResponseCodeError(call: Call<T>, response: Response<T>) {
        if (bindStateLayout) {
            baseLiveData?.switchToError()
        }
        baseLiveData?.showMessage("出错了，请重试")
        super.onResponseCodeError(call, response)
    }

    override fun onResponseBodyNullError(call: Call<T>, response: Response<T>) {
        if (bindStateLayout) {
            baseLiveData?.switchToError()
        }
        baseLiveData?.showMessage("出错了，请重试")
        super.onResponseBodyNullError(call, response)
    }

    @CallSuper
    override fun onResponseSuccess(call: Call<T>, body: T) {
        if (bindStateLayout) {
            baseLiveData?.switchToSuccess()
        }
        if (bindSmart) {
            baseLiveData?.finishLoadMoreSuccess()
        }
        successData?.postValue(body)
        super.onResponseSuccess(call, body)
    }

    override fun doOnResponse(doOnResponse: (call: Call<T>, response: Response<T>) -> Unit): LiveDataCallback<T> {
        return super.doOnResponse(doOnResponse) as LiveDataCallback
    }

    override fun doOnResponseSuccess(doOnResponse: (call: Call<T>, response: T) -> Unit): LiveDataCallback<T> {
        return super.doOnResponseSuccess(doOnResponse) as LiveDataCallback
    }
}