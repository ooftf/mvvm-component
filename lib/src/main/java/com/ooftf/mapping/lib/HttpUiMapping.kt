package com.ooftf.mapping.lib

import android.app.Activity
import android.content.DialogInterface
import android.view.Window
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/11/14
 */
object HttpUiMapping {
    lateinit var provider: Provider
        private set

    fun init(provider: Provider, debug: Boolean) {
        HttpUiMapping.provider = provider
        LogUtil.debug = debug
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val integer: Int? = null
        println("" + (integer == 0))
    }

    fun <T:Any> proxyService(service: T): T {
        val t = service::class.java.interfaces.first()
        val proxyService = Proxy.newProxyInstance(
                t.classLoader,
                arrayOf(t),
                InvocationHandler { proxy, method, args ->
                    val invoke = if (args == null) {
                        method.invoke(service)
                    } else {
                        method.invoke(service, *args)
                    }
                    if (invoke is retrofit2.Call<*>) {
                        return@InvocationHandler Proxy.newProxyInstance(
                                invoke.javaClass.classLoader,
                                arrayOf(retrofit2.Call::class.java)
                        ) { _, method, args ->
                            if (method.name == "enqueue") {
                                if (args != null && args.isNotEmpty() && args[0] is LiveDataCallback<*>) {
                                    val ldc: LiveDataCallback<*> = args[0] as LiveDataCallback<*>
                                    ldc.setCall(invoke)
                                }
                            }
                            if (args.isNullOrEmpty()) {
                                method.invoke(invoke)
                            } else {
                                method.invoke(invoke, *args)
                            }
                        }
                    }
                    invoke
                })
        return proxyService as T
    }

    interface Provider {
        fun onTokenInvalid(baseResponse: IResponse)
        fun createLoadingDialog(activity: Activity): MyDialogInterface
        fun toast(string: String?)
    }

    interface MyDialogInterface : DialogInterface {
        fun setOnCancelListener(listener: DialogInterface.OnCancelListener?)
        fun getWindow(): Window
        fun show()
    }
}