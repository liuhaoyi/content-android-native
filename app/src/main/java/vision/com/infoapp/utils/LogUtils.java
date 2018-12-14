package vision.com.infoapp.utils;

import android.util.Log;

/**
 * Created by liuhy on 2018/12/11.
 */

public class LogUtils {
    public static boolean isAD = true;

    public static void e(String tag,String message){
        if(isAD){
            Log.e(tag,message);
        }else{
            System.out.println(tag + "---" + message);
        }
    }
}
