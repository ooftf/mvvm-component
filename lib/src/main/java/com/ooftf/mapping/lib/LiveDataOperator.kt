package com.ooftf.mapping.lib

import com.ooftf.mapping.lib.ui.BaseLiveData

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/7/28
 */
class LiveDataOperator<T : IResponse> {
    internal val liveDataCallback = LiveDataCallback<T>()
    fun setLiveData(value: BaseLiveData): LiveDataCallback<T> {
        liveDataCallback.setLiveData(value)
        return liveDataCallback
    }

}