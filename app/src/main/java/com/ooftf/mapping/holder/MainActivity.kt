package com.ooftf.mapping.holder

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.topText.background = LineDrawable()

    }

    override fun isScreenForcePortrait(): Boolean {
        return false
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.e("MainActivity", "onConfigurationChanged")

    }

    override fun onWindowAttributesChanged(params: WindowManager.LayoutParams?) {
        super.onWindowAttributesChanged(params)
        Log.e("MainActivity", "onWindowAttributesChanged")
    }
}