package com.sora.myapplist;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView = null;
    private List<AppInfo> system_appInfoList = null;
    private List<AppInfo> history_appInfoList = null;
    private List<AppInfo> refresh_appInfoList = null;
    private List<AppInfo> sdcard_appInfoList = null;
    private ProgressBar progressBar = null;
    private Toolbar toolbar = null;
    private App_Handler app_handler = null;
//    private Boolean ishistoryed = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        init();
        //创建handler对象
        app_handler = new App_Handler();
        //调用线程 Thread_getHistory_AppInfoList
        //读取HistoryAppList
        Thread_getHistory_AppInfoList getHistory_AppInfoList = new Thread_getHistory_AppInfoList();
        //线程开始
        new Thread(getHistory_AppInfoList).start();
        //TODO 确保在该线程结束之后下个线程才开始执行 实际效果不确定
        try {
            new Thread(getHistory_AppInfoList).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //调用线程 Thread_makeSystemAppInfoList
        //读取当前安装App 构造SystemAppInfoList
        Thread_makeSystemAppInfoList makeSystemAppInfoList = new Thread_makeSystemAppInfoList();
        //线程开始
        new Thread(makeSystemAppInfoList).start();
        //TODO AppList排序按钮的监听
        //监听Toolbar按钮点击事件
        toolbar.setOnMenuItemClickListener(new toolbar_OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return super.onMenuItemClick(item);
            }
        });
//        设置click事件
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

    }

    //合并List:A和RefreshAppInfoList,生成新的RefreshAppInfoList
    private void makeRefreshAppInfoList(List<AppInfo> A) {
        //获取refresh_appInfoList的size
        int id = refresh_appInfoList.size();
        //将List:B遍历 判断List:B中的元素在refresh_appInfoList中是否存在
        for (AppInfo a :
                A) {
            //判断App是否相同的标识
            boolean isExisted = false;
            for (AppInfo refresh_appInfo :
                    refresh_appInfoList) {
                //比较系统app和历史app的包名是否一致
                //TODO 算法可优化
                if (a.getPackageName().equals(refresh_appInfo.getPackageName())){
                    isExisted = true;
                }
            }
            //如果是不相同的App 将新的元素添加进入refresh_appInfoList中
            if (!isExisted){
                refresh_appInfoList.add(a);
                //对id进行调整 表示新元素添加在原序列之后
                id++;
                refresh_appInfoList.get(id-1).setAppID(Integer.toString(id));
            }
            //TODO 考虑在乱序情况下需要刷新列表
        }
    }

    //调用ListView 展示AppInfoList
    private void showAppList(List<AppInfo> appInfoList) {
        //将AppInfoList加载到适配器
        AppInfoAdapter appInfoAdapter = new AppInfoAdapter(MainActivity.this, appInfoList);
        //显示App列表
        listView.setAdapter(appInfoAdapter);
    }

    ////用List:A覆盖List:B
    private void replace(List<AppInfo> A,List<AppInfo> B){
        //必须将List:B初始化
        B = new ArrayList<AppInfo>();
        //foreach
        for (AppInfo var :
                A) {
            B.add(var);
        }
    }

    public void init() {
        //声明toolbar控件
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //创建进度条对象
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //创建ListView对象
        listView = (ListView) findViewById(R.id.app_listView);
        //创建List 用于装填App信息
        system_appInfoList = new ArrayList<AppInfo>();
        history_appInfoList = new ArrayList<AppInfo>();
        refresh_appInfoList = new ArrayList<AppInfo>();
        sdcard_appInfoList = new ArrayList<AppInfo>();
    }

    //三方应用程序过滤器
    private boolean filterApp(ApplicationInfo applicationInfo) {
        //代表被用户升级过的系统应用
        if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        }
        //代表用户应用
        else if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }

    //导入程序列表
    private void importFile() throws IOException, ClassNotFoundException {
        //判断SDCard是否存在并且可读写
        Boolean isExisted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        //新建FileService对象
        FileService service = new FileService(getApplicationContext());
       //判断SDCard能否被访问
        if (isExisted) {
            //判断读取列表是否为空指针
            //若是则返回
            if (service.listFromSDCard() == null) {
                Toast.makeText(MainActivity.this, "请确认文件格式正确且以文件名My AppList.txt存放在SDCard根目录下", Toast.LENGTH_SHORT).show();
            }
            //若非空将读取列表赋值给history_appInfoList
            else {
                //将SD卡上的数据导入sdcard_appInfoList
                replace(service.listFromSDCard(), sdcard_appInfoList);
//                for (int i=0;i<sdcard_appInfoList.size();i++){
//                    sdcard_appInfoList.get(i).toString();
//                }
                //检索sdcard_appInfoList 将其并入refresh_appInfoList
                makeRefreshAppInfoList(sdcard_appInfoList);
                showAppList(refresh_appInfoList);
                Toast.makeText(MainActivity.this, "导入成功", Toast.LENGTH_SHORT).show();
                saveHistory_appInfoList();
            }
        }
        else{
            Toast.makeText(MainActivity.this, "无法访问SDCard", Toast.LENGTH_SHORT).show();
        }

    }

    //导出程序列表
    private void exportFile() throws IOException {
        //判断SDCard是否存在并且可读写
        Boolean isExisted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        //新建FileService对象
        FileService service = new FileService(getApplicationContext());
        //更新后的appInfoList
        List<AppInfo> fileList = refresh_appInfoList;
        //判断SDCard能否被访问
        if (isExisted) {
            service.saveToSDCard(fileList);
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "My AppList.txt";
            Toast.makeText(MainActivity.this, "已导出程序列表" + filePath, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this, "无法访问SDCard", Toast.LENGTH_SHORT).show();
        }
    }

    //将AppInfoList复制到剪贴板
    private void copyFile() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(getRefresh_appInfoList());
        Toast.makeText(MainActivity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

    //获取AppInfoList的内容
    private String getRefresh_appInfoList(){
        String app_list = "";
        for (int i=0;i< refresh_appInfoList.size();i++){
            //获取App的id
            String appid = refresh_appInfoList.get(i).getAppID();
            //获取App的Label
            String appName = refresh_appInfoList.get(i).getAppName();
            //获取App的大小
            String appSize = refresh_appInfoList.get(i).getAppSize();
            //获取App对应的包名
            String packageName = refresh_appInfoList.get(i).getPackageName();
            //获取App的安装时间
            String installTime = refresh_appInfoList.get(i).getInstallTime();
            //获取版本名
            String edition = refresh_appInfoList.get(i).getEdition();
            app_list += appid + ":   " + appName + "    " + edition + "    " + appSize + "    " + packageName + "    " + installTime+"  \n";
        }
        return app_list;
    }

    //显示toolbar内容
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //inner class ->监听Toolbar按钮点击事件
    private class toolbar_OnMenuItemClickListener implements Toolbar.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                //用户点击了copy功能键
                case R.id.action_copy:
                    copyFile();
                    break;
                //用户点击了export功能键
                case R.id.action_export:
                    try {
                        exportFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                //用户点击了import功能键
                case R.id.action_import:
                    try {
                        importFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return true;
        }
    }

    //写入HistoryAppInfoList
    private void saveHistory_appInfoList(){
        //新建FileService对象
        FileService service = new FileService(getApplicationContext());
        //更新存储内容
        List<AppInfo> fileList = refresh_appInfoList;
        //传递history_appInfoList 存储数据
        try {
            service.saveToData(fileList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //线程 读取当前系统安装App 构造SystemAppInfoList
    private class Thread_makeSystemAppInfoList implements Runnable{

        @Override
        public void  run(){
            Message message = new Message();
            //存放数据
            Bundle bundle = new Bundle();
            //获取已安装的应用程序包
            List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
            //非三方程序序列号
            int app_id = 0;
            //对获得的应用程序包进行相关操作
            for (int i = 0; i < packs.size(); i++) {
                PackageInfo p = packs.get(i);
                //过滤无版本名的app
                if (p.versionName == null) {
                    continue;
                }
                //过滤非三方应用程序
                if (!filterApp(p.applicationInfo)) {
                    continue;
                }
                //确认将会添加到程序列表,id+1
                app_id++;
                //获得App创建时对应的文件夹
                String dir = p.applicationInfo.publicSourceDir;
                //获取App创建时对应文件夹的大小
                Long size_long = new File(dir).length();
                //定义时间格式
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                //App创建时对应的文件夹的最近修改时间-最近一次的更新时间
                Date date = new Date(new File(dir).lastModified());
                //获取App的Label
                String appName = (String) p.applicationInfo.loadLabel(getPackageManager());
                //获取App的大小
                String appSize = Long.toString(size_long / 1024 / 1024) + "MB";
                //获取App对应的包名
                String packageName = p.packageName;
                //获取App的安装时间
                String installTime = formatter.format(date);
                //获取版本名
                String edition = p.versionName;
                //构建AppInfo对象
                String appID = Integer.toString(app_id);
                AppInfo appInfo = new AppInfo();
                //设置App所需输出的各参数值
                appInfo.setAppName(appName);
                appInfo.setEdition(edition);
                appInfo.setPackageName(packageName);
                appInfo.setAppSize(appSize);
                appInfo.setInstallTime(installTime);
                appInfo.setAppID(appID);
                //添加进列表中
                system_appInfoList.add(appInfo);
            }
            bundle.putString("showAppList", "2");
            message.setData(bundle);
            //向Handler发送消息
            //直到历史数据显示出 才发送message
//            while (!ishistoryed){
//
//            }
            MainActivity.this.app_handler.sendMessage(message);
        }
    }

    //线程  读取HistoryAppList
    private class Thread_getHistory_AppInfoList implements Runnable{

        @Override
        public void run() {
            Message message = new Message();
            //存放数据
            Bundle bundle = new Bundle();
            //新建FileService对象
            FileService service = new FileService(getApplicationContext());
            //判断读取列表是否为空指针
            //若是则返回
            try {
                if (service.listFromData() == null){
                    return;
                }
                //若不是将读取列表赋值给history_appInfoList
                else {
                    replace(service.listFromData(),history_appInfoList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            bundle.putString("showAppList","1");
            message.setData(bundle);
            //向Handler发送消息
            MainActivity.this.app_handler.sendMessage(message);
        }
    }

    //Handler 类
    private class App_Handler extends android.os.Handler {

        public App_Handler() {
        }

        public App_Handler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新UI
            String string_showAppList = null;
            //更新bundle
            Bundle bundle = msg.getData();
            //判断是否生成AppList
            string_showAppList = bundle.getString("showAppList");
            switch (string_showAppList){
                //显示HistoryAppInfoList
                case "1":
                    replace(history_appInfoList, refresh_appInfoList);
                    showAppList(refresh_appInfoList);
                    //说明history_appInfoList已显示
//                    ishistoryed = true;
                    System.out.println("输出的是历史App");
                    break;
                //显示RefreshAppInfoList
                case "2":
                    System.out.println("输出的是系统App");
                    progressBar.setVisibility(View.GONE);
                    //合并HistoryAppList和SystemAppInfoList,生成RefreshAppInfoList
                    makeRefreshAppInfoList(system_appInfoList);
                    //显示RefreshAppInfoList
                    showAppList(refresh_appInfoList);
                    //保存到data目录下的文件中
                    saveHistory_appInfoList();
                    break;
            }
        }
    }
}
