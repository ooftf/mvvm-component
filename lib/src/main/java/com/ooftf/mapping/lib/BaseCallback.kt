package com.ooftf.mapping.lib

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

    override fun onResponse(call: Call<T>, response: Response<T>) {
        doOnResponseContainer.forEach {
            it.invoke(call, response)
        }
        doOnResponseContainer.clear()

        if (response.code() != 200) {
            onResponseCodeError(call, response)
            return
        }
        val body = response.body()
        if (body == null) {
            onResponseBodyNullError(call, response)
            return
        }

        if (body.error == 0) {
            onResponseSuccess(call, body)
        } else if (body.error == ERROR_TOKEN || body.error == ERROR_ACCOUNT || body.error == ERROR_ACCOUNT_NO) {
            onResponseLoginStatusError(body)
        } else {
            onResponseFailure(call, body)
        }
    }

    protected open fun onResponseCodeError(call: Call<T>, response: Response<T>) {

    }

    protected open fun onResponseBodyNullError(call: Call<T>, response: Response<T>) {

    }


    protected open fun onResponseFailure(call: Call<T>, body: T) {

    }

    protected open fun onResponseLoginStatusError(body: T) {
        HttpUiMapping.getProvider().onTokenInvalid(body)
    }

    protected open fun onResponseSuccess(call: Call<T>, body: T) {
        doOnResponseSuccessContainer.forEach {
            it.invoke(call, body)
        }
        doOnResponseSuccessContainer.clear()
    }

    override fun onFailure(call: Call<T>, t: Throwable) {

    }

    private val doOnResponseContainer = ArrayList<(call: Call<T>, response: Response<T>) -> Unit>()
    open fun doOnResponse(doOnResponse: (call: Call<T>, response: Response<T>) -> Unit): BaseCallback<T> {
        doOnResponseContainer.add(doOnResponse)
        return this
    }

    private val doOnResponseSuccessContainer = ArrayList<(call: Call<T>, response: T) -> Unit>()
    open fun doOnResponseSuccess(doOnResponse: (call: Call<T>, response: T) -> Unit): BaseCallback<T> {
        doOnResponseSuccessContainer.add(doOnResponse)
        return this
    }

    companion object {
        const val ERROR_TOKEN = 100420
        const val ERROR_ACCOUNT = 100463
        const val ERROR_ACCOUNT_NO = 100405
    }
}
