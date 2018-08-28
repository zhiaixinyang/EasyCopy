package com.mdove.easycopy.update.manager;

import com.mdove.easycopy.config.MainConfigSP;
import com.mdove.easycopy.utils.DateUtils;

/**
 * Created by MDove on 2018/8/28.
 */

public class UpdateStatusManager {
    public static boolean isShowUpdateDialog() {
        long curTime = System.currentTimeMillis();
        if (MainConfigSP.getAppOrderTodayTime()==0){
            MainConfigSP.setAppOrderTodayTime(curTime);
            return true;
        }
        boolean isSame= DateUtils.isSameDay(curTime,MainConfigSP.getAppOrderTodayTime());
        if (!isSame){
            MainConfigSP.setAppOrderTodayTime(curTime);
            return true;
        }
        return false;
    }
}
