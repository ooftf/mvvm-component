package com.ooftf.mapping.lib

import androidx.annotation.CallSuper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * 此部分只做逻辑分发，不做界面处理，除了token失效
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/7/22 0022
 */
open class BaseCallback<T : BaseResponse> : Callback<T> {
    @CallSuper
    override fun onResponse(call: Call<T>, response: Response<T>) {
        doOnResponseContainer.forEach {
            it.invoke(call, response)
        }
        if (response.code() != 200) {
            doOnAnyFailContainer.forEach { it.invoke(call) }
            onHttpCodeError(call, response)
            return
        }
        val body = response.body()
        if (body == null) {
            doOnAnyFailContainer.forEach { it.invoke(call) }
            onResponseFailureBodyNull(call, response)
            return
        }

        if (body.error == 0) {
            doOnResponseSuccessContainer.forEach {
                it.invoke(call, body)
            }
        } else if (body.error == ERROR_TOKEN || body.error == ERROR_ACCOUNT || body.error == ERROR_ACCOUNT_NO) {
            doOnAnyFailContainer.forEach { it.invoke(call) }
            doOnResponseCodeErrorContainer.forEach { it.invoke(call, body) }
            onResponseLoginStatusError(body)
        } else {
            doOnAnyFailContainer.forEach { it.invoke(call) }
            doOnResponseCodeErrorContainer.forEach { it.invoke(call, body) }
        }
    }


    protected open fun onHttpCodeError(call: Call<T>, response: Response<T>) {}

    protected open fun onResponseFailureBodyNull(call: Call<T>, response: Response<T>) {

    }

    protected open fun onResponseLoginStatusError(body: T) {
        HttpUiMapping.getProvider().onTokenInvalid(body)
    }

    @CallSuper
    override fun onFailure(call: Call<T>, t: Throwable) {
        doOnAnyFailContainer.forEach { it.invoke(call) }
    }

    private val doOnResponseContainer = ArrayList<(call: Call<T>, response: Response<T>) -> Unit>()
    open fun doOnResponse(doOnResponse: (call: Call<T>, response: Response<T>) -> Unit): BaseCallback<T> {
        doOnResponseContainer.add(doOnResponse)
        return this
    }

    private val doOnResponseSuccessContainer = ArrayList<(call: Call<T>, response: T) -> Unit>()
    open fun doOnResponseSuccess(doOnResponseSuccess: (call: Call<T>, response: T) -> Unit): BaseCallback<T> {
        doOnResponseSuccessContainer.add(doOnResponseSuccess)
        return this
    }

    private val doOnAnyFailContainer = ArrayList<(call: Call<T>) -> Unit>()
    open fun doOnAnyFail(doOnAnyFail: (call: Call<T>) -> Unit): BaseCallback<T> {
        doOnAnyFailContainer.add(doOnAnyFail)
        return this
    }

    private val doOnResponseCodeErrorContainer = ArrayList<(call: Call<T>, response: T) -> Unit>()
    open fun doOnResponseCodeError(doOnResponseCodeError: (call: Call<T>, response: T) -> Unit): BaseCallback<T> {
        doOnResponseCodeErrorContainer.add(doOnResponseCodeError)
        return this
    }


    companion object {
        const val ERROR_TOKEN = 100420
        const val ERROR_ACCOUNT = 100463
        const val ERROR_ACCOUNT_NO = 100405
    }
}
