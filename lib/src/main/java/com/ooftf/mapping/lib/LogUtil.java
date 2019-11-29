package com.ooftf.mapping.lib;
import android.util.Log;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/11/29
 */
public class LogUtil {
    public static boolean debug;

    public static void e(String message){
        if(debug){
            Log.e("http-ui-mapping",message);
        }
    }
}
