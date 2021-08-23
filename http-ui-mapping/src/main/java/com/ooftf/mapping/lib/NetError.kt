package com.ooftf.mapping.lib

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/8/23
 */
sealed class NetError(var message: String?)
class HttpCodeError(message: String?, var code: Int) : NetError(message)
class BusinessCodeError<T>(message: String?, var body: T) : NetError(message)
class HttpFailureError(message: String?, var case: Throwable): NetError(message)
class BodyNullError(message: String?): NetError(message)