package com.ooftf.mapping.lib;

import android.os.Handler;
import android.os.Looper;

/**
 * AndroidUtil 主要负责硬件层级
 * App主要负责软件层级
 */
public class ThreadUtil {
    public static final String TAG = "ThreadUtil";
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static void runOnUiThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            mainHandler.post(runnable);
        }
    }
}
