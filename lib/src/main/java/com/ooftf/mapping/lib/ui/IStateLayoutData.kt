package com.ooftf.mapping.lib.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/9/16
 */
interface IStateLayoutData {
    fun refresh()
    fun emptyAction()
    fun getStateLayout(): LiveData<Int>
    fun getLifecycleOwner(): LifecycleOwner?
}