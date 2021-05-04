package com.example.incubatorui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.incubatorui.BigImageUilts.PhotoView;
import com.example.incubatorui.utils.ImageUtils.VerifyStoragePermissions;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * 该类是从小图片转到图片放大后的视图
 */
public class BigImageActivity extends AppCompatActivity {
    private String pathImg;
    private final int returnLookImageActivity = 0;
    private final int returnPrintScreenActivity = 1;
    private int returnActivity = 0;
    ApplicationUtil applicationUtil;
    Bitmap bitmap;//传递给编辑的对象
    int maxWidth;
    int maxHeight;
    PhotoView mPvShowBigger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_big_image);
        Intent intent = getIntent();
        pathImg=intent.getStringExtra("path");

        System.out.println(pathImg);
        mPvShowBigger = findViewById(R.id.pv_showbigger);
        mPvShowBigger.setmAttache(true);
        Glide.with(this)
                .applyDefaultRequestOptions(
                        new RequestOptions()
                                .format(DecodeFormat.PREFER_ARGB_8888))
                    .load(pathImg).into(mPvShowBigger);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //只请求图片宽高，不解析图片像素(请求图片属性但不申请内存，解析bitmap对象，该对象不占内存)
        opts.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(pathImg, opts);
        maxWidth = opts.outWidth;
        maxHeight = opts.outHeight;
        applicationUtil = (ApplicationUtil)this.getApplication();
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    System.out.println("宽高" + maxWidth + "------------" + maxHeight);
                    bitmap = Glide.with(applicationUtil)
                            .asBitmap()
                            .load(pathImg)
                            .submit(maxWidth, maxHeight).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

        applicationUtil = (ApplicationUtil)this.getApplication();
    }



    /**
     * 返回按钮
     * @param view
     */
    public void breturn(View view) {
        returnActivity = returnLookImageActivity;
        finish();
    }
    /**
     * 编辑按钮
     * @param view
     */
    public void EditImage(View view) {
        returnActivity = returnPrintScreenActivity;
        finish();
    }
    /**
     * 删除当前图片
     * @param view
     */
    public void bdelete(View view) {
        //如果没有权限就请求权限
        VerifyStoragePermissions.verifyStoragePermissions(this);
        File f = new File(pathImg);
        //删除文件
        f.delete();
        returnActivity = returnLookImageActivity;
        finish();
    }

    @Override
    public void finish() {
        switch(returnActivity){
            case returnLookImageActivity:
                Thread thread  = (new Thread(){
                    @Override
                    public void run() {
                        Intent it=new Intent(getApplicationContext(), LookImageActivity.class);//启动MainActivity
                        startActivity(it);
                    }
                });
                thread.start();
                break;
            case returnPrintScreenActivity :
                //传递给applicationUtil
                applicationUtil.setAllBitmap(bitmap);
                Thread thread1  = (new Thread(){
                    @Override
                    public void run() {
                        Intent it=new Intent(getApplicationContext(), PrintscreenActivity.class);//启动MainActivity
                        //通知下一个页面printscreenActivity返回到LookImage
                        it.putExtra("returnActivity",1);
                        startActivity(it);
                    }
                });
                thread1.start();
                break;
            default:break;
        }

        super.finish();
    }

}