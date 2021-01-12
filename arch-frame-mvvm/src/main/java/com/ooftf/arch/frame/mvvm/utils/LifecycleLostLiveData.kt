package com.ooftf.arch.frame.mvvm.utils

import androidx.lifecycle.Observer
import com.ooftf.arch.frame.mvvm.vm.BaseViewModel
import com.ooftf.basic.armor.livedata.LostMutableLiveData

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/11/13
 */
class LifecycleLostLiveData<T> : LostMutableLiveData<T>() {
    fun observe(vm: BaseViewModel, observer: Observer<in T>) {
        super.observeForever(observer)
        vm.doOnCleared {
            removeObserver(observer)
        }
    }
}