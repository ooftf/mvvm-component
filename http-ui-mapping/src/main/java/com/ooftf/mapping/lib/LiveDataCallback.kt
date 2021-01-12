package com.ooftf.mapping.lib

import androidx.lifecycle.MutableLiveData
import com.ooftf.mapping.lib.ui.BaseLiveData
import com.ooftf.mapping.lib.ui.Cancelable
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
class LiveDataCallback<T : IResponse> : BaseCallback<T>, Cancelable {


    private var baseLiveData: BaseLiveData? = null
    private var successData: MutableLiveData<T>? = null
    private var bindSmart = false
    private var bindDialog = false
    private var bindStateLayout = false
    private val singleTags: MutableSet<Any> by lazy {
        HashSet<Any>()
    }
    private val multipleTags: MutableSet<Any> by lazy {
        HashSet<Any>()
    }

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

    init {

        doOnResponseCodeError { _, body ->
            baseLiveData?.showMessage(body.getMessage())
        }
        doOnAnyFail {
            baseLiveData?.let { baseLiveData ->
                if (bindStateLayout) {
                    baseLiveData.switchToError()
                }
                singleTags.forEach {
                    baseLiveData.singleFail(it)
                }
                if (bindSmart) {
                    baseLiveData.finishLoadMore()
                    baseLiveData.finishRefresh()
                }
                if (bindDialog) {
                    baseLiveData.dismissDialog(this)
                }
                multipleTags.forEach {
                    baseLiveData.lessMultiple(it)
                }

            }

        }
        doOnResponseSuccess { call, body ->
            baseLiveData?.let { baseLiveData ->
                if (bindStateLayout) {
                    baseLiveData.switchToSuccess()
                }
                singleTags.forEach {
                    baseLiveData.singleSuccess(it)
                }
                if (bindSmart) {
                    baseLiveData.finishLoadMoreSuccess()
                    baseLiveData.finishRefresh()
                }
                if (bindDialog) {
                    baseLiveData.dismissDialog(this)
                }
                multipleTags.forEach {
                    baseLiveData.lessMultiple(it)
                }
            }
            successData?.postValue(body)
        }


    }

    override fun cancel() {
        mCall?.cancel()
    }

    fun setLiveData(value: BaseLiveData): LiveDataCallback<T> {
        baseLiveData = value
        return this
    }

    fun setCall(call: Call<*>?) {
        this.mCall = call
    }

    fun bindSmart(): LiveDataCallback<T> {
        baseLiveData?.startRefresh()
        bindSmart = true
        return this
    }

    fun bindDialog(call: Cancelable): LiveDataCallback<T> {
        baseLiveData?.showDialog(call)
        bindDialog = true
        return this
    }

    fun bindSingle(tag: Any): LiveDataCallback<T> {
        baseLiveData?.singleLoading(tag)
        singleTags.add(tag)
        return this
    }

    fun bindMultiple(tag: Any): LiveDataCallback<T> {
        baseLiveData?.addMultiple(tag)
        multipleTags.add(tag)
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
        super.onFailure(call, t)
        t.printStackTrace()
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


    override fun onHttpCodeError(call: Call<T>, response: Response<T>) {
        super.onHttpCodeError(call, response)
        baseLiveData?.showMessage("出错了，请重试")
    }

    override fun onResponseFailureBodyNull(call: Call<T>, response: Response<T>) {
        super.onResponseFailureBodyNull(call, response)
        baseLiveData?.showMessage("出错了，请重试")
    }


    override fun doOnResponse(doOnResponse: (call: Call<T>, response: Response<T>) -> Unit): LiveDataCallback<T> {
        return super.doOnResponse(doOnResponse) as LiveDataCallback
    }

    override fun doOnResponseSuccess(doOnResponseSuccess: (call: Call<T>, body: T) -> Unit): LiveDataCallback<T> {
        return super.doOnResponseSuccess(doOnResponseSuccess) as LiveDataCallback
    }

    override fun doOnResponseSuccessHeader(doOnResponseSuccess: (call: Call<T>, response: T) -> Unit): LiveDataCallback<T> {
        return super.doOnResponseSuccessHeader(doOnResponseSuccess) as LiveDataCallback
    }

    override fun doOnAnyFail(doOnAnyFail: (call: Call<T>) -> Unit): LiveDataCallback<T> {
        return super.doOnAnyFail(doOnAnyFail) as LiveDataCallback<T>
    }

    override fun doOnResponseCodeError(doOnResponseCodeError: (call: Call<T>, body: T) -> Unit): LiveDataCallback<T> {
        return super.doOnResponseCodeError(doOnResponseCodeError) as LiveDataCallback<T>
    }


}