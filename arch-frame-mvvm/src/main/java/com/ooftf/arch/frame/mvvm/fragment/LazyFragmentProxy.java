package com.ooftf.arch.frame.mvvm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/1/13 0013
 */
public class LazyFragmentProxy<T extends Fragment & LazyFragmentProxy.LazyFragmentOwner> {
    T fragment;
    private WeakReference<View> rootViewReference;

    public LazyFragmentProxy(T fragment) {
        this.fragment = fragment;
    }

    public boolean isLoaded = false;

    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment.lazyEnabled()) {
            View view = null;
            if (rootViewReference != null) {
                view = rootViewReference.get();
            }
            if (view == null) {
                view = fragment.getView();
                rootViewReference = new WeakReference<>(view);
            }
            if (view == null) {
                isLoaded = false;
                view = fragment.getContentView(inflater, container);
                rootViewReference = new WeakReference<>(view);
            }
            loadJudgment(view);
            return view;
        } else {
            View view;
            if (fragment.getView() == null) {
                view = fragment.getContentView(inflater, container);
            } else {
                view = fragment.getView();
            }
            loadJudgment(view);
            return fragment.getView();
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (fragment.lazyEnabled()) {
            loadJudgment(fragment.getView());
        } else {
            fragment.onLoad(view);
        }
    }

    private void loadJudgment(View view) {
        if (view != null && fragment.getUserVisibleHint() && !isLoaded && !fragment.isHidden() && fragment.isShowing()) {
            fragment.onLoad(view);
            isLoaded = true;
        }
    }

    public void setUserVisibleHint(boolean visibleHint) {
        if (fragment.lazyEnabled()) {
            loadJudgment(fragment.getView());
        }

    }

    public void onHiddenChanged() {
        if (fragment.lazyEnabled()) {
            loadJudgment(fragment.getView());
        }
    }

    public void onStart() {
        if (fragment.lazyEnabled()) {
            loadJudgment(fragment.getView());
        }
    }

    public void onDetach() {
        rootViewReference = null;
    }


    public interface LazyFragmentOwner {
        /**
         * fragment 的布局文件
         *
         * @return
         */
        View getContentView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container);

        /**
         * 初始化界面
         */

        void onLoad(@NotNull View rootView);

        boolean lazyEnabled();


        boolean isShowing();
    }
}
