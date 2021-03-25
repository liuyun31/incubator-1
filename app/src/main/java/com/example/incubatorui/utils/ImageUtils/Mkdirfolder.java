package com.example.incubatorui.utils.ImageUtils;

import android.os.Environment;

import java.io.File;

/**
 * 创建目录
 */
public class Mkdirfolder {
    //文件夹目录"/sdcard/FirstFolder/SecondFolder"，多级目录必须逐一创建
    /*public static String FirstFolder="FirstFolder";//一级目录
    public static String SecondFolder="SecondFolder";//二级目录*/
    /*ALBUM_PATH取得机器的SD卡位置，File.separator为分隔符“/”*/
    /*private final static String ALBUM_PATH= Environment.getExternalStorageDirectory()+ File.separator+FirstFolder+File.separator;
    private final static String Second_PATH= ALBUM_PATH+SecondFolder+File.separator;
    */

    /**
     * 创建目录
     * @param path 创建目录的路径
     * @param folderName 创建目录名
     * @return
     */
    public static File mkdirFolder (String path,String folderName){
        //检查手机上是否有外部存储卡
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(!sdCardExist)
        {//如果不存在SD卡，进行提示
            return null;

        }else{//如果存在SD卡，判断文件夹目录是否存在
            //一级目录和二级目录必须分开创建
            File dirFirstFile=new File(path  + folderName + File.separator);//新建一级主目录
            if(!dirFirstFile.exists()){//判断文件夹目录是否存在
                dirFirstFile.mkdir();//如果不存在则创建
            }
            return dirFirstFile;
        }
    }

}
