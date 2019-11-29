package com.ooftf.mapping.lib.ui;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.facade.Postcard;
import com.ooftf.mapping.lib.LostMutableLiveData;
import com.ooftf.mapping.lib.ThreadUtil;
import com.ooftf.widget.statelayout.IStateLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/6/13 0013
 */
public class BaseLiveData {
    /**
     * //finish
     */
    public LostMutableLiveData<Integer> finishLiveData = new LostMutableLiveData<>();
    /**
     * //finish
     */
    public LostMutableLiveData<FinishData> finishWithData = new LostMutableLiveData<>();
    /**
     * start
     */
    public LostMutableLiveData<Postcard> startActivityLiveData = new LostMutableLiveData<>();


    /**
     * errorMessage
     */
    public LostMutableLiveData<String> messageLiveData = new LostMutableLiveData<>();


    /**
     * showLoading
     */
    public MutableLiveData<List<CallOwner>> showLoading = new MutableLiveData<>();


    public MutableLiveData<Integer> smartRefresh = new MutableLiveData<>();
    public MutableLiveData<Integer> smartLoadMore = new MutableLiveData<>();

    public MutableLiveData<Integer> stateLayout = new MutableLiveData<>();

    public Map<Class, MutableLiveData> dataListLiveData = new HashMap();

    /**
     * finish
     */
    public void finish() {
        finishLiveData.setValue(Activity.RESULT_CANCELED);
    }


    public <T> MutableLiveData<T> getLiveData(Class<T> tClass) {
        MutableLiveData<T> mutableLiveData = dataListLiveData.get(tClass);
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            dataListLiveData.put(tClass, mutableLiveData);
        }
        return mutableLiveData;
    }

    public <T> void post(T data) {
        Class<T> classs = (Class<T>) data.getClass();
        getLiveData(classs).postValue(data);
    }

    /**
     * finish
     */
    public void finish(int result) {
        finishLiveData.setValue(result);
    }

    /**
     * finish
     */
    public void finish(FinishData result) {
        finishWithData.setValue(result);
    }

    public void startActivity(Postcard postcart) {
        startActivityLiveData.postValue(postcart);
    }

    /**
     * showDialog
     */
    public void showDialog(CallOwner call) {
        ThreadUtil.INSTANCE.runOnUiThread(() -> {
            if (showLoading.getValue() == null) {
                ArrayList<CallOwner> calls = new ArrayList<>();
                calls.add(call);
                showLoading.setValue(calls);
            } else {
                List<CallOwner> calls = showLoading.getValue();
                calls.add(call);
                showLoading.setValue(calls);
            }
        });
    }


    /**
     * dismissDialog
     */
    public void dismissDialog(CallOwner call) {
        ThreadUtil.INSTANCE.runOnUiThread(() -> {
            if (showLoading.getValue() == null) {
                showLoading.setValue(new ArrayList<>());
            } else {
                List<CallOwner> calls = showLoading.getValue();
                if (!calls.remove(call)) {
                    calls.remove(null);
                }
                showLoading.setValue(calls);
            }
        });
    }

    public void showMessage(String message) {
        this.messageLiveData.postValue(message);
    }


    public void startRefresh() {
        ThreadUtil.INSTANCE.runOnUiThread(() -> {
            if (smartRefresh.getValue() == null) {
                smartRefresh.setValue(1);
            } else {
                smartRefresh.setValue(smartRefresh.getValue() + 1);
            }
        });
    }

    public void finishRefresh() {
        ThreadUtil.INSTANCE.runOnUiThread(() -> {
            if (smartRefresh.getValue() == null) {
                smartRefresh.setValue(0);
            } else {
                smartRefresh.setValue(smartRefresh.getValue() - 1);
            }
        });
    }

    public void finishLoadMore() {
        smartLoadMore.postValue(UIEvent.SMART_LAYOUT_LOADMORE_FINISH);
    }

    public void finishLoadMoreWithNoMoreData() {
        smartLoadMore.postValue(UIEvent.SMART_LAYOUT_LOADMORE_FINISH_AND_NO_MORE);
    }


    public void switchToEmpty() {
        ThreadUtil.INSTANCE.runOnUiThread(() -> {
            stateLayout.setValue(IStateLayout.STATE_EMPTY);
        });
    }

    public void switchToLoading() {
        ThreadUtil.INSTANCE.runOnUiThread(() -> {
            stateLayout.setValue(IStateLayout.STATE_LOAD);
        });
    }

    public void switchToError() {
        ThreadUtil.INSTANCE.runOnUiThread(() -> {
            stateLayout.setValue(IStateLayout.STATE_ERROR);
        });
    }

    public void switchToSuccess() {
        ThreadUtil.INSTANCE.runOnUiThread(() -> {
            stateLayout.setValue(IStateLayout.STATE_SUCCESS);
        });
    }


    public BaseLiveDataObserve attach(LifecycleOwner owner, Activity activity) {
        return new BaseLiveDataObserve(this, owner, activity);
    }


}
