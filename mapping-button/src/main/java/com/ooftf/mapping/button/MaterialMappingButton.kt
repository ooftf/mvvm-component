package com.ooftf.mapping.button

import android.content.Context
import android.util.AttributeSet
import com.ooftf.basic.utils.DensityUtil
import com.scwang.smartrefresh.header.internal.MaterialProgressDrawable

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/1/26
 */
class MaterialMappingButton : MappingButton<MaterialProgressDrawable> {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun createProgressDrawable(): MaterialProgressDrawable {
        return MaterialProgressDrawable(this)
    }

    override fun setLoadingColor(color: Int) {
        progressDrawable.setColorSchemeColors(color)
    }

    override fun getProgressDrawablePadding(): Int {
        return DensityUtil.dip2pxInt(4f)
    }
}