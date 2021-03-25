package com.example.incubatorui.GetJPEG;

import android.graphics.Bitmap;

public class MyRnnable implements Runnable{
    Bitmap bitmap=null;
    Boolean video;

    /**
     * 将当前对象存储起来
     * @param b
     */
    public MyRnnable(Bitmap b, Boolean video){
        this.bitmap = b;
        this.video = video;
    }
    public MyRnnable(Bitmap b){
        this.bitmap = b;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public Boolean getVideo() {
        return video;
    }

    @Override
    public void run() {

    }
}
