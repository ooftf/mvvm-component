package com.ooftf.mapping.lib

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/7/22 0022
 */
interface IResponse {
    /*var error: Int = 0
    var msg: String = ""*/

    fun isTokenError(): Boolean
    fun isSucess(): Boolean
    fun getMessage(): String
}