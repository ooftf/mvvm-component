package com.ooftf.mapping.holder

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/6/28
 */
class LineDrawable : Drawable() {
    var mLeftPadding = 0;
    var mRightPadding = 0;
    var mPaint = Paint()

    init {
        mPaint.color = Color.parseColor("#DDDDDD")
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 1F
        setVisible(false, true)
    }

    override fun draw(canvas: Canvas) {
        if (!isVisible) {
            return
        }
        val y = bounds.bottom.toFloat() - mPaint.strokeWidth
        canvas.drawLine(
            bounds.left + mLeftPadding.toFloat(), y,
            (bounds.right - mRightPadding).toFloat(), y, mPaint
        )
    }

    fun setWidth(width: Float) {
        mPaint.strokeWidth = width
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

}