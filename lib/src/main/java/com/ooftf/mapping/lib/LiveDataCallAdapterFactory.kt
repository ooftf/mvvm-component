package com.ooftf.mapping.lib

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/7/28
 */
class LiveDataCallAdapterFactory : CallAdapter.Factory() {
    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() = LiveDataCallAdapterFactory()
    }

    override fun get(
            returnType: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (LiveDataOperator::class.java != getRawType(returnType)) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalStateException(
                    "Deferred return type must be parameterized as LiveDataOperator<Foo> or LiveDataOperator<out Foo>"
            )
        }
        val responseType = getParameterUpperBound(0, returnType)

        val rawDeferredType = getRawType(responseType)
        return if (IResponse::class.java.isAssignableFrom(rawDeferredType)) {
            BodyCallAdapter<IResponse>(responseType)
        } else {
            throw IllegalStateException(
                    "Response 必须继承自 IResponse"
            )
        }

    }

    private class BodyCallAdapter<T : IResponse>(
            private val responseType: Type
    ) : CallAdapter<T, LiveDataOperator<T>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): LiveDataOperator<T> {
            val result = LiveDataOperator<T>()
            result.liveDataCallback.setCall(call)
            call.enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    GlobalScope.launch(Dispatchers.Main) {
                        result.liveDataCallback.onFailure(call, t)
                    }
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    GlobalScope.launch(Dispatchers.Main) {
                        result.liveDataCallback.onResponse(call, response)
                    }
                }
            })

            return result
        }
    }
}