package com.ooftf.mapping.lib.ui;

import androidx.databinding.BindingAdapter;

import com.ooftf.mapping.lib.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/10/15
 */
public class SmartLayoutDataBindingAdapter {
    @BindingAdapter(value = "refreshState", requireAll = false)
    public static void setRefreshState(SmartRefreshLayout smartRefreshLayout, int state) {
        if (state == 0) {
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.resetNoMoreData();
        }
    }

    @BindingAdapter(value = "loadMoreState", requireAll = false)
    public static void setLoadMoreState(SmartRefreshLayout smartRefreshLayout, int state) {
        if (state == UIEvent.SMART_LAYOUT_LOADMORE_FINISH) {
            smartRefreshLayout.finishLoadMore();
            LogUtil.e("finishLoadMore");
        } else if (state == UIEvent.SMART_LAYOUT_LOADMORE_FINISH_AND_NO_MORE) {
            smartRefreshLayout.finishLoadMoreWithNoMoreData();
            LogUtil.e("finishLoadMoreWithNoMoreData");
        }
    }

    @BindingAdapter(value = "loadMoreListener", requireAll = false)
    public static void setOnLoadMoreListener(SmartRefreshLayout smartRefreshLayout, Runnable f) {
        if (f == null) {
            return;
        }
        LogUtil.e("setOnLoadMoreListener");
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            try {
                f.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @BindingAdapter(value = "refreshListener", requireAll = false)
    public static void setOnRefreshListener(SmartRefreshLayout smartRefreshLayout, Runnable f) {
        if (f == null) {
            return;
        }
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            try {
                f.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
