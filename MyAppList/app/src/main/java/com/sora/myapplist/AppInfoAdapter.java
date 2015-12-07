package com.sora.myapplist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sora on 2015/12/6.
 */
public class AppInfoAdapter extends BaseAdapter {

    private List<AppInfo> appInfoList = null;
    LayoutInflater layoutInflater = null;

    public AppInfoAdapter(Context context,List<AppInfo> appInfoList) {
        layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        this.appInfoList = appInfoList;
    }

    @Override
    public int getCount() {
        return appInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return appInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View view = null;
        if (convertView == null || convertView.getTag() == null){
            view = layoutInflater.inflate(R.layout.item_listview,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = (AppInfo) getItem(position);
        viewHolder.appName.setText(appInfo.getAppName());
        viewHolder.edition.setText(appInfo.getEdition());
        viewHolder.appSize.setText(appInfo.getAppSize());
        viewHolder.packageName.setText(appInfo.getPackageName());
        viewHolder.installTime.setText(appInfo.getInstallTime());
        return view;
    }

    class ViewHolder{
        TextView appName;
        TextView edition;
        TextView appSize;
        TextView packageName;
        TextView installTime;

        public ViewHolder(View view) {
            this.appName = (TextView) view.findViewById(R.id.id_appName);
            this.edition = (TextView) view.findViewById(R.id.id_edition);
            this.appSize = (TextView) view.findViewById(R.id.id_appSize);
            this.packageName = (TextView) view.findViewById(R.id.id_packageName);
            this.installTime = (TextView) view.findViewById(R.id.id_installTime);

        }
    }
}
