package com.ooftf.mapping.lib.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/9/16
 */
interface ISmartLayoutData {
    fun refresh()
    fun nextPage()
    fun getLoadMoreState(): LiveData<Int>
    fun getRefreshState(): LiveData<Int>
    fun getLifecycleOwner(): LifecycleOwner?
}