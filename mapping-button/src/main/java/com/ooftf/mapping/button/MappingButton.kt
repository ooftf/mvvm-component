package com.ooftf.mapping.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.ooftf.basic.utils.DensityUtil

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/1/26
 */
abstract class MappingButton<T> : AppCompatButton where T : Drawable, T : Animatable {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val progressDrawable: T by lazy {
        createProgressDrawable().apply {
            val progressPadding = getProgressDrawablePadding()
            bounds = Rect(0, 0, (width.coerceAtMost(height) - progressPadding * 2), (width.coerceAtMost(height) - progressPadding * 2))
        }
    }

    abstract fun getProgressDrawablePadding(): Int
    abstract fun createProgressDrawable(): T

    var loadding = false
    fun toLoading() {
        if (loadding) {
            return
        }
        isEnabled = false
        loadding = true
        postInvalidate()
        progressDrawable.start()
    }

    fun toSuccess() {
        toNormal()
    }

    private fun toNormal() {
        isEnabled = true
        loadding = false
        progressDrawable.stop()
    }

    override fun onDraw(canvas: Canvas) {
        if (loadding) {
            canvas.translate((width / 2 - progressDrawable.bounds.width() / 2).toFloat(), (height / 2 - progressDrawable.bounds.height() / 2).toFloat())
            progressDrawable.draw(canvas)
            postInvalidate()
        } else {
            super.onDraw(canvas)
        }
    }

    abstract fun setLoadingColor(color: Int)

    fun toFail() {
        toNormal()
    }

}
