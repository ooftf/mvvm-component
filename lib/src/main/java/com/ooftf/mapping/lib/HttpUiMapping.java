package com.ooftf.mapping.lib;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.Window;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/11/14
 */
public class HttpUiMapping {
    private static provider provider;

    public static void init(provider provider, boolean debug) {
        HttpUiMapping.provider = provider;
        LogUtil.debug = debug;
    }

    public static HttpUiMapping.provider getProvider() {
        return provider;
    }

    public interface provider {
        void onTokenInvalid(BaseResponse baseResponse);

        MyDialogInterface createLoadingDialog(Activity activity);

        void toast(String string);
    }

    public interface MyDialogInterface extends DialogInterface {
        void setOnCancelListener(DialogInterface.OnCancelListener listener);

        Window getWindow();

        void show();

    }

    public static void main(String[] args) {
        Integer integer = null;
        System.out.println("" + (integer == 0));
    }

}
