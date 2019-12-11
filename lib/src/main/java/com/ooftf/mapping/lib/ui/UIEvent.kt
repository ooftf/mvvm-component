package com.ooftf.mapping.lib.ui

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/10/15
 */
object UIEvent {
    /**
     * SmartLayout 加载更多结束
     */
    const val SMART_LAYOUT_LOADMORE_FINISH = 0

    /**
     * SmartLayout 加载更多结束,并且成功
     */
    const val SMART_LAYOUT_LOADMORE_FINISH_SUCCESS = 1

    /**
     * SmartLayout 加载更多结束，并且没有更多
     */
    const val SMART_LAYOUT_LOADMORE_FINISH_AND_NO_MORE = 2




    object Single{
        const val NOMAL = 0
        const val LOADING = 1
        const val SUCCESS = 2
        const val FAIL = 3
    }
}