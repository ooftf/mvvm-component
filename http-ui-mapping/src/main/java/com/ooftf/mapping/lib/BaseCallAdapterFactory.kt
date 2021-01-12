package com.ooftf.mapping.lib
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/7/28
 */
class BaseCallAdapterFactory : CallAdapter.Factory() {
    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() = BaseCallAdapterFactory()
    }

    override fun get(
            returnType: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (BaseCallback::class.java != getRawType(returnType)) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalStateException(
                    "Deferred return type must be parameterized as BaseLiveData<Foo> or BaseLiveData<out Foo>"
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
    ) : CallAdapter<T, BaseCallback<T>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): BaseCallback<T> {
            val result = BaseCallback<T>()
            GlobalScope.launch (Dispatchers.IO){
                try {
                    val response = call.execute()
                    withContext(Dispatchers.Main){
                        result.onResponse(call,  response)
                    }
                } catch (t: Throwable) {
                    withContext(Dispatchers.Main){
                        result.onFailure(call, t)
                    }
                }
            }
            return result
        }
    }
}