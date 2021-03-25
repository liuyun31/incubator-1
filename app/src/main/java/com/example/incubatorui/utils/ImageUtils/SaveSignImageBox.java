package com.example.incubatorui.utils.ImageUtils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;

public class SaveSignImageBox {

    //图片保存路径
   // private String signImage = "picture";
    /**
     * 保存图片
     * @param path 保存路径
     * @param fileName 保存文件名
     * @param bitmap 对象
     */
    public void saveSignImageBox(String path,String signImage, String fileName, Bitmap bitmap) {
        //图片保存路径

        try {
            File PICTURES = new File(path);

            File imageFileDirctory = new File(PICTURES + "/" + signImage);
            if (imageFileDirctory.exists()) {
                File imageFile = new File(PICTURES + "/" + signImage + "/" + fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

            } else if (imageFileDirctory.mkdir()) {//如果该文件夹不存在，则新建
                //new一个文件
                File imageFile = new File(PICTURES + "/" + signImage + "/" + fileName);
                //通过流将图片写入
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

            }
        } catch (Exception e) {
        }
    }

}
