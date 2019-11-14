package com.ooftf.mapping.lib

import android.os.Handler
import android.os.Looper

/**
 * AndroidUtil 主要负责硬件层级
 * App主要负责软件层级
 */
object ThreadUtil {
    val TAG = "ThreadUtil"
    private val mainHandler = Handler(Looper.getMainLooper())

    val isMainThread: Boolean
        get() = Thread.currentThread() === Looper.getMainLooper().thread

    fun runOnUiThread(runnable: Runnable) {
        if (isMainThread) {
            runnable.run()
        } else {
            mainHandler.post(runnable)
        }
    }
}
