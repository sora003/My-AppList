package com.sora.myapplist;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sora on 2015/12/10.
 */
public class AppListService {

    Context context;

    public AppListService(Context context) {
        this.context = context;
    }

    //合并HistoryAppList和SystemAppInfoList,生成RefreshAppInfoList
    public List<AppInfo> makeRefreshAppInfoList(List<AppInfo> history_appInfoList,List<AppInfo> system_appInfoList) {
        List<AppInfo> refresh_appInfoList = new ArrayList<AppInfo>();
        //用history_appInfoList覆盖refresh_appInfoList
        replace(history_appInfoList,refresh_appInfoList);
        int id = refresh_appInfoList.size();
        for (AppInfo system_appInfo :
                system_appInfoList) {
            boolean isExisted = false;
            for (AppInfo refresh_appInfo :
                    refresh_appInfoList) {
                //比较系统app和历史app的包名是否一致
                //TODO 算法可优化
                if (system_appInfo.getPackageName().equals(refresh_appInfo.getPackageName())){
                    isExisted = true;
                }
            }
            if (!isExisted){
                refresh_appInfoList.add(system_appInfo);
                id++;
                refresh_appInfoList.get(id-1).setAppID(Integer.toString(id));
            }
        }
        return refresh_appInfoList;
    }

    //读取HistoryAppList
    private void getHistory_AppInfoList() throws IOException, ClassNotFoundException {
        //新建FileService对象
        FileService service = new FileService(context);
        //判断读取列表是否为空指针
        //若是则返回
        if (service.listFromData() == null){
            return;
        }
        //若不是将读取列表赋值给history_appInfoList
        else {
            replace(service.listFromData(),history_appInfoList);
        }
    }

    ////用List:A覆盖List:B
    private void replace(List<AppInfo> A,List<AppInfo> B){
        for (AppInfo var :
                A) {
            B.add(var);
        }
    }
}
