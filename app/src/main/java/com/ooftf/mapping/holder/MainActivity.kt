package com.ooftf.mapping.holder

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ooftf.arch.frame.mvvm.activity.BaseMvvmActivity
import com.ooftf.mapping.holder.databinding.ActivityMainBinding

/**
 * @author 
 * @email 
 * @date 2021-01-26
 */
@Route(path = "//Main")
class MainActivity : BaseMvvmActivity<ActivityMainBinding, MainViewModel>() {

}