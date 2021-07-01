package com.ooftf.mapping.holder

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.ooftf.arch.frame.mvvm.activity.BaseMvvmActivity
import com.ooftf.basic.utils.getVisibleRectOfScreen
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
        binding.topText.setOnClickListener {
            printParent(binding.topText)

            Log.e(
                "childCount",
                ""+(window.decorView as ViewGroup).childCount
            )
        }
    }

    fun printParent(view: View) {
        val parent = view.parent
        when (parent) {
            is ViewGroup -> {
                Log.e(
                    "printParent",
                    parent.javaClass.simpleName + parent.getVisibleRectOfScreen().toShortString()
                )
                printParent(parent)
            }
        }
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