package com.sora.myapplist;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Sora on 2015/12/10.
 */

public class FileService {

    private Context context;

    //得到传入的上下文对象的引用
    public FileService(Context context) {
        this.context = context;
    }

    //传递文件名，数据，实现在SD卡根目录下创建文件并将数据保存在该文件中的功能
    public void saveToSDCard(List<AppInfo> fileList) throws IOException {
        String filename = "My AppList.txt";
        File file = new File(Environment.getExternalStorageDirectory(),filename);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(fileList);
        objectOutputStream.close();
    }

    public List<AppInfo> listFromFile(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (List<AppInfo>) objectInputStream.readObject();
    }

    //传递文件名，数据，实现在data目录下创建文件并将数据保存在该文件中的功能
    public void saveToData(List<AppInfo> fileList) throws IOException {
        String filename = "History AppList.txt";
        FileOutputStream fileOutputStream = context.openFileOutput(filename,Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(fileList);
        objectOutputStream.close();
    }

    //读取存放在data目录下的数据文件 导出history_appInfoList
    public List<AppInfo> listFromData() throws IOException, ClassNotFoundException {
        //文件名
        String filename = "History AppList.txt";
        //文件上层目录路径
        String filepath = context.getFilesDir().getAbsolutePath();
        //文件路径
        File file = new File(filepath+"/"+filename);
        //判断文件是否存在 不存在返回空指针
        if (!file.exists()){
            return null;
        }
        //若存在读取文件序列 返回list
        else {
            FileInputStream fileInputStream = context.openFileInput(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (List<AppInfo>) objectInputStream.readObject();
        }
    }
}
