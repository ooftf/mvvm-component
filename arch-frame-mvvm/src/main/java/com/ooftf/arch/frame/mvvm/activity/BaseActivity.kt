package com.ooftf.arch.frame.mvvm.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.gyf.immersionbar.ImmersionBar
import com.ooftf.arch.frame.mvvm.R
import com.ooftf.arch.frame.mvvm.utils.BackPressedHandler
import com.ooftf.arch.frame.mvvm.utils.PostcardSerializable
import com.ooftf.basic.utils.getCurrentFragment
import com.trello.lifecycle4.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by master on 2017/10/10 0010.
 */
open class BaseActivity : AppCompatActivity() {
    private val defaultRequestCode = 837
    var successIntent: Bundle? = null
    var failIntent: Bundle? = null
    private var mToast: Toast? = null

    val provider: com.trello.rxlifecycle4.LifecycleProvider<Lifecycle.Event> by lazy {
        AndroidLifecycle.createLifecycleProvider(this)
    }

    fun <T> bindDestroy(): LifecycleTransformer<T> {
        return provider.bindUntilEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun <T> bindAuto(): LifecycleTransformer<T> {
        return provider.bindToLifecycle()
    }

    fun <T> Observable<T>.bindDestroy(): Observable<T> {
        return bindUntilEvent(this@BaseActivity, Lifecycle.Event.ON_DESTROY)
    }


    val providerCompat: LifecycleProvider<Lifecycle.Event> by lazy {
        com.trello.lifecycle2.android.lifecycle.AndroidLifecycle.createLifecycleProvider(this)
    }

    fun <T> bindDestroyCompat(): com.trello.rxlifecycle3.LifecycleTransformer<T> {
        return providerCompat.bindUntilEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun <T> bindAutoCompat(): com.trello.rxlifecycle3.LifecycleTransformer<T> {
        return providerCompat.bindToLifecycle()
    }

    fun <T> io.reactivex.Observable<T>.bindDestroyCompat(): io.reactivex.Observable<T> {
        return this.compose(this@BaseActivity.bindDestroyCompat())
    }

    fun getBaseActivity(): BaseActivity {
        return this
    }

    init {
        if (isImmersionEnable()) {
            lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
                fun create() {
                    ImmersionBar.with(this@BaseActivity).statusBarDarkFont(isDarkFont())
                            .navigationBarColorInt(Color.WHITE)
                            .keyboardEnable(true, getKeyBordMode())
                            .init()
                    var list: MutableList<View> = ArrayList()
                    getToolbarId().forEach {
                        findViewById<View>(it)?.let { it ->
                            list.add(it)
                        }
                    }
                    getToolbar().forEach {
                        list.add(it)
                    }
                    ImmersionBar.setTitleBar(this@BaseActivity, *list.toTypedArray())
                }

            })
        }
    }

    open fun getKeyBordMode() = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    fun finishSuccess() {
        val successIntent = intent.getBundleExtra(INTENT_DATA_SUCCESS_INTENT)
        if (successIntent != null) {
            PostcardSerializable.toPostcard(successIntent).navigation(this)
        }
        setResult(Activity.RESULT_OK)
        finish()

    }

    fun finishFail() {
        val failIntent = intent.getBundleExtra(INTENT_DATA_FAIL_INTENT)
        if (failIntent != null) {
            PostcardSerializable.toPostcard(failIntent).navigation(this)
        }
        finish()
    }

    open fun getToolbarId(): IntArray {
        return intArrayOf(R.id.toolbar)
    }

    open fun getToolbar(): Array<View> {
        return emptyArray()
    }


    open fun isDarkFont(): Boolean {
        return true
    }

    open fun isImmersionEnable(): Boolean {
        return true
    }


    fun startActivity(cla: Class<*>) {
        startActivity(Intent(this, cla))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isScreenForcePortrait()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        super.onCreate(savedInstanceState)
        /*try {
            this.onCreate(savedInstanceState)
        } catch (e: Throwable) {
            CrashReport.postCatchedException(e)
            if (BuildConfig.DEBUG) {
                toast(e.toString())
            }
            setContentView(R.layout.base_layout_error_default) // TODO
        }
*/
    }


    /**
     * 是否强制竖屏
     */
    fun isScreenForcePortrait() = true


    fun showDialogMessage(
            message: CharSequence,
            listener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which -> dialog?.dismiss() }
    ) {
        showDialogMessage(message, "确定", listener)
    }

    fun showDialogMessage(message: CharSequence) {
        showDialogMessage(
                message,
                "确定",
                DialogInterface.OnClickListener { dialog, which -> dialog?.dismiss() })
    }

    fun showDialogMessage(
            message: CharSequence,
            positiveText: CharSequence = "确定",
            positiveListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which -> dialog?.dismiss() }
    ) {
        AlertDialog
                .Builder(this)
                .setMessage(message)
                .setPositiveButton(positiveText, positiveListener)
                .show()
    }

    private var mActivityResultCallback: ForResultCallback? = null
    fun startActivityForResult(intent: Intent?, callback: ForResultCallback) {
        mActivityResultCallback = callback
        super.startActivityForResult(intent, defaultRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == defaultRequestCode && mActivityResultCallback != null) {
            mActivityResultCallback?.callback(resultCode, data)
            mActivityResultCallback = null
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    interface ForResultCallback {
        fun callback(resultCode: Int, data: Intent?)
    }

    @SuppressLint("CheckResult")
    fun delayFinish(mil: Long) {
        Observable.timer(mil, TimeUnit.MILLISECONDS)
                .bindDestroy()
                .subscribe {
                    finish()
                }
    }

    fun toast(content: String, duration: Int = Toast.LENGTH_SHORT) {
        mToast?.cancel()
        mToast = Toast.makeText(this.application, content, duration)
        mToast?.show()
    }


    fun toast(content: String) {
        toast(content, Toast.LENGTH_SHORT)
    }

    companion object {
        const val INTENT_DATA_SUCCESS_INTENT = "INTENT_DATA_SUCCESS_INTENT"
        const val INTENT_DATA_FAIL_INTENT = "INTENT_DATA_FAIL_INTENT"
    }

    override fun onBackPressed() {
        (getCurrentFragment() as? BackPressedHandler)?.let {
            if (it.onBackPressed()) {
                return@onBackPressed
            }
        }
        super.onBackPressed()
    }
}

