package com.sora.myapplist;

import java.util.Comparator;

/**
 * Created by Sora on 2015/12/13.
 */
public class InstallTimeComparator implements Comparator<AppInfo> {

    @Override
    public int compare(AppInfo a1,AppInfo a2) {
        return a1.getInstallTime().compareTo(a2.getInstallTime());
    }
}
