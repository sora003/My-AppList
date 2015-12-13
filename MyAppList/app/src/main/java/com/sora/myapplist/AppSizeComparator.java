package com.sora.myapplist;

import java.util.Comparator;

/**
 * Created by Sora on 2015/12/13.
 */
public class AppSizeComparator implements Comparator<AppInfo> {

    @Override
    public int compare(AppInfo a1,AppInfo a2) {
        if (a1.getExact_AppSize() < a2.getExact_AppSize()){
            return  -1;
        }
        else if (a1.getExact_AppSize() == a2.getExact_AppSize()){
            return 0;
        }
        else if (a1.getExact_AppSize() > a2.getExact_AppSize()){
            return 1;
        }
        return 0;
    }
}
