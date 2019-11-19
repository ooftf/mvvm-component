package com.ooftf.mapping.lib.ui

import retrofit2.Call


/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/10/25
 */
interface CallOwner {
    fun getCall(): Call<*>?
}