package com.example.incubatorui.GetJPEG;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewImage extends AppCompatActivity {
    private static final int CHANGE = 1;
    ImageView imageView;
    Bitmap returnBitmap = null;
    MyHandle myHandle  = new MyHandle();
    public ViewImage(Bitmap bitmap1, ImageView i)
    {
        returnBitmap = bitmap1;
        imageView = i;
        System.out.println("自定义类");
        //启动线程，通过自定义子线程类，将当前bitmap对象保存在其中
        /*runOnUiThread (new MyRnnable(bitmap1) {
            @Override
            public void run(){
                if(this.getBitmap() != null){
                    System.out.println("更新UI");
                    //更新UI
                    imageView.setImageBitmap(this.getBitmap());
                }
            }
        });*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("更新UI1");
                Message ms = new Message();
                ms.what = CHANGE;
                myHandle.sendMessage(ms);
            }
        }).start();
    }
    public Bitmap fiishView(){
        try {
            this.finish();
            return returnBitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //更新线程
    class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("更新UI2");
            super.handleMessage(msg);
            switch (msg.what) {
                case CHANGE:
                    System.out.println("更新UI3");
                    //更新内容
                    imageView.setImageBitmap(returnBitmap);
                    break;
                default:
                    break;
            }
        }
    }
}
