package com.ooftf.mapping.lib.coroutines

import com.ooftf.basic.utils.toast
import com.ooftf.mapping.lib.HttpUiMapping
import com.ooftf.mapping.lib.IResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/5/8
 */
open class BaseResponseSorting<T : IResponse>() {
    private val doOnAnyFailContainer = ArrayList<() -> Unit>()
    private val doOnResponseSuccessContainer = ArrayList<(response: T) -> Unit>()
    private val doOnResponseCodeErrorContainer = ArrayList<(response: T) -> Unit>()
    private val doOnResponseContainer = ArrayList<(response: T) -> Unit>()
    private val doOnHttpErrorContainer = ArrayList<(response: Throwable) -> Unit>()
    private val doOnStartContainer = ArrayList<() -> Unit>()

    suspend fun run(netAction: suspend () -> T): T? {
        try {
            preRun()
            val response = netAction()
            handleResponse(response)
            return response
        } catch (e: Throwable) {
            onFail(e)
        }
        return null

    }

    protected suspend fun preRun() {
        withContext(Dispatchers.Main) {
            doOnStartContainer.forEach { it.invoke() }
        }
    }

    protected suspend fun onFail(e: Throwable) {
        withContext(Dispatchers.Main) {
            doOnAnyFailContainer.forEach { it.invoke() }
            doOnHttpErrorContainer.forEach { it.invoke(e) }
        }
    }

    protected suspend fun handleResponse(response: T) {
        withContext(Dispatchers.Main) {
            doOnResponseContainer.forEach {
                it.invoke(response)
            }
            when {
                response.isSuccess() -> {
                    try {
                        doOnResponseSuccessContainer.forEach {
                            it.invoke(response)
                        }
                    } catch (t: Throwable) {
                        toast("数据处理异常！")
                        t.printStackTrace()
                    }
                }
                response.isTokenError() -> {
                    doOnAnyFailContainer.forEach { it.invoke() }
                    doOnResponseCodeErrorContainer.forEach { it.invoke(response) }
                    onResponseLoginStatusError(response)
                }
                else -> {
                    doOnAnyFailContainer.forEach { it.invoke() }
                    doOnResponseCodeErrorContainer.forEach { it.invoke(response) }
                }
            }
        }

    }

    protected open fun onResponseLoginStatusError(body: T) {
        HttpUiMapping.provider.onTokenInvalid(body)
    }


    open fun doOnResponse(doOnResponse: (response: T) -> Unit): BaseResponseSorting<T> {
        doOnResponseContainer.add(doOnResponse)
        return this
    }

    open fun doOnResponseSuccess(doOnResponseSuccess: (response: T) -> Unit): BaseResponseSorting<T> {
        doOnResponseSuccessContainer.add(doOnResponseSuccess)
        return this
    }

    open fun doOnResponseSuccessHeader(doOnResponseSuccess: (response: T) -> Unit): BaseResponseSorting<T> {
        doOnResponseSuccessContainer.add(0, doOnResponseSuccess)
        return this
    }

    open fun doOnAnyFail(doOnAnyFail: () -> Unit): BaseResponseSorting<T> {
        doOnAnyFailContainer.add(doOnAnyFail)
        return this
    }

    open fun doOnHttpError(doOnHttpError: (response: Throwable) -> Unit): BaseResponseSorting<T> {
        doOnHttpErrorContainer.add(doOnHttpError)
        return this
    }

    open fun doOnStart(doOnStart: () -> Unit): BaseResponseSorting<T> {
        doOnStartContainer.add(doOnStart)
        return this
    }

    open fun doOnResponseCodeError(doOnResponseCodeError: (response: T) -> Unit): BaseResponseSorting<T> {
        doOnResponseCodeErrorContainer.add(doOnResponseCodeError)
        return this
    }
}