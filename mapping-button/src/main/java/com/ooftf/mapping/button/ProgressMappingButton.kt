package com.ooftf.mapping.button

import android.content.Context
import android.util.AttributeSet
import com.ooftf.basic.utils.DensityUtil
import com.scwang.smartrefresh.layout.internal.ProgressDrawable

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/1/26
 */
class ProgressMappingButton : MappingButton<ProgressDrawable> {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun createProgressDrawable(): ProgressDrawable {
        return ProgressDrawable()
    }

    override fun setLoadingColor(color: Int) {
        progressDrawable.setColor(color)
    }

    override fun getProgressDrawablePadding(): Int {
        return DensityUtil.dip2pxInt(14f)
    }
}