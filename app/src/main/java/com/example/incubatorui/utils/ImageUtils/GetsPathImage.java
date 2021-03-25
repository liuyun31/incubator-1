package com.example.incubatorui.utils.ImageUtils;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetsPathImage {
    ArrayList<String> images;
    ArrayList<String> returnImages = new ArrayList<>();
    public List<String> getFilesAllName(String path , String time){
        //打开文件路径
        Log.d("图片文件夹路径",path);
        File file = new File(path);
        //获取文件夹内文件集合
        File[] files = file.listFiles();
        images = new ArrayList<>();
        if(files != null){
            for(int i=0;i<files.length;i++){
                if(checkIsImageFile(files[i].getPath())){
                    if(time != null){
                        String tes  = files[i].getPath();
                        int index = tes.indexOf(".jpg");
                        if(tes.substring(index-17,index-9).equals(time)){
                            images.add(files[i].getPath());
                        }
                    }else {
                        images.add(files[i].getPath());
                    }


                }
            }
            sortImagePath();
            return returnImages;
        }else{
            Log.d("图片文件夹路径","获取不到！！！！");
            return null;
        }
    }
    /**
     * 路径排序
     */
    public void sortImagePath(){
        int i = 0;
        String[]  s = new String[images.size()];
        for (String a:images) {
            s[i++] = a;
        }
        Arrays.sort(s);
        for(int j=0;j<images.size();j++){
            returnImages.add(s[j]);
        }
    }

    /**
     * 比较两个字符串
     * @param s1
     * @param s2
     * @return
     */
    public int equalsString(String s1, String s2){
        for(int i=0;i<s1.length()-1;i++){
            if(s1.charAt(i) > s2.charAt(i)){
                return 1;
            }else {
                if(s1.charAt(i) < s2.charAt(i)){
                    return 0;
                }
            }
        }
        return 0;
    }
    /**
     * 判断文件是否为图片
     * @param fName
     * @return
     */
    public boolean checkIsImageFile(String fName){

        boolean isImageFile = false;
        //获取扩展名(toLowerCase : 转小写)
        String fileEnd = fName.substring(fName.indexOf(".")+1,fName.length()).toLowerCase();
        if(fileEnd.equals("jpg") || fileEnd.equals("png") || fileEnd.equals("jpeg") || fileEnd.equals("gif") || fileEnd.equals("bmp")){
            isImageFile = true;
        }else{
            isImageFile = false;
        }
        return isImageFile;
    }

}
