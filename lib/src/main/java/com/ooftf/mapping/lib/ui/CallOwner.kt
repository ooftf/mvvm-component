package com.chiatai.premix.lib.base.net.ui

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