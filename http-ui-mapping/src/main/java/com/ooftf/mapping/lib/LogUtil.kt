package com.ooftf.mapping.lib

import android.util.Log

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/11/29
 */
internal object LogUtil {
    var debug = false
    @JvmStatic
    fun e(message: String?) {
        if (debug) {
            Log.e("http-ui-mapping", message)
        }
    }
}