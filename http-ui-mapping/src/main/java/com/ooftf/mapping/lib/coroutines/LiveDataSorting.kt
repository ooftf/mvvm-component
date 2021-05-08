package com.ooftf.mapping.lib.coroutines

import com.ooftf.mapping.lib.IResponse
import com.ooftf.mapping.lib.ui.BaseLiveData
import com.ooftf.mapping.lib.ui.Cancelable

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/5/8
 */
class LiveDataSorting<T : IResponse>(
    val baseLiveData: BaseLiveData
) :
    BaseResponseSorting<T>(), Cancelable {
    init {
        doOnHttpError {
            baseLiveData.showMessage("网络数据异常，请稍后再试")
        }
        doOnResponseCodeError {
            baseLiveData.showMessage(it.getMessage())
        }
    }

    fun bindSmart(): LiveDataSorting<T> {
        doOnStart {
            baseLiveData.startRefresh()
        }
        doOnHttpError {
            baseLiveData.finishRefresh()
        }
        doOnResponse {
            baseLiveData.finishRefresh()
        }
        return this
    }


    fun bindSingle(tag: Any): LiveDataSorting<T> {
        doOnStart {
            baseLiveData.singleLoading(tag)
        }
        doOnAnyFail {
            baseLiveData.singleFail(tag)
        }
        doOnResponseSuccess {
            baseLiveData.singleSuccess(tag)
        }
        return this
    }

    fun bindMultiple(tag: Any): LiveDataSorting<T> {
        doOnStart {
            baseLiveData.addMultiple(tag)
        }
        doOnAnyFail {
            baseLiveData.lessMultiple(tag)
        }
        doOnResponseSuccess {
            baseLiveData.lessMultiple(tag)
        }
        return this
    }


    fun bindDialog(): LiveDataSorting<T> {
        doOnStart {
            baseLiveData.showDialog(this)
        }
        doOnHttpError {
            baseLiveData.dismissDialog(this)
        }
        doOnResponse {
            baseLiveData.dismissDialog(this)
        }
        return this
    }

    fun bindStateLayout(): LiveDataSorting<T> {
        doOnStart {
            baseLiveData.switchToLoading()
        }
        doOnAnyFail {
            baseLiveData.switchToError()
        }
        doOnResponseSuccess {
            baseLiveData.switchToSuccess()
        }
        return this
    }

    override fun cancel() {

    }

    override fun doOnResponse(doOnResponse: (response: T) -> Unit): LiveDataSorting<T> {
        return super.doOnResponse(doOnResponse) as LiveDataSorting<T>
    }

    override fun doOnResponseSuccess(doOnResponseSuccess: (response: T) -> Unit): LiveDataSorting<T> {
        return super.doOnResponseSuccess(doOnResponseSuccess) as LiveDataSorting<T>
    }

    override fun doOnResponseSuccessHeader(doOnResponseSuccess: (response: T) -> Unit): LiveDataSorting<T> {
        return super.doOnResponseSuccessHeader(doOnResponseSuccess) as LiveDataSorting<T>
    }

    override fun doOnAnyFail(doOnAnyFail: () -> Unit): LiveDataSorting<T> {
        return super.doOnAnyFail(doOnAnyFail) as LiveDataSorting<T>
    }

    override fun doOnHttpError(doOnHttpError: (response: Throwable) -> Unit): LiveDataSorting<T> {
        return super.doOnHttpError(doOnHttpError) as LiveDataSorting<T>
    }

    override fun doOnStart(doOnStart: () -> Unit): LiveDataSorting<T> {
        return super.doOnStart(doOnStart) as LiveDataSorting<T>
    }

    override fun doOnResponseCodeError(doOnResponseCodeError: (response: T) -> Unit): LiveDataSorting<T> {
        return super.doOnResponseCodeError(doOnResponseCodeError) as LiveDataSorting<T>
    }

}