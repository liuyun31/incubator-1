package com.example.incubatorui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doodle.DoodleActivity;
import com.example.doodle.DoodleParams;
import com.example.doodle.DoodleView;
import com.example.doodle.dialog.DialogController;
import com.example.incubatorui.imageCrop.view.CropImageView;
import com.example.incubatorui.utils.ImageUtils.DeleteFile;
import com.example.incubatorui.utils.ImageUtils.GetTime;
import com.example.incubatorui.utils.ImageUtils.Mkdirfolder;
import com.example.incubatorui.utils.ImageUtils.SaveSignImageBox;
import com.example.incubatorui.utils.ImageUtils.VerifyStoragePermissions;

import java.io.File;

public class PrintscreenActivity extends AppCompatActivity {
    private Bitmap bitmap;
    ApplicationUtil applicationUtil;
    private final int returnFullModeActivity = 0;
    private final int returnLookImageActivity = 1;
    private int returnActivity=0;

    CropImageView cropImageView;      //自定义的裁剪框
    Bitmap cropBitMap;                //需要裁剪的bitmap
    Button croppingButton;            //裁剪按钮
    ImageView doodleImageView;
    // 涂鸦
    public static final int REQ_CODE_DOODLE = 101;
    ViewGroup advancedContainer;



    //获取组件宽度
    int cropwidth ;
    //获取组件高度
    int cropheight;

    private String DNAME;        //保存文件夹的名字，


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏

        Intent t =getIntent();
        returnActivity = t.getIntExtra("returnActivity",0);

        cropImageView = findViewById(R.id.CropImageView);//自定义裁剪框
        croppingButton = findViewById(R.id.cropping); //裁剪按钮
        // 涂鸦自定义容器
        advancedContainer = findViewById(R.id.container_advanced_doodle);
        //涂鸦之后的图片显示
        doodleImageView = findViewById(R.id.doodleImageView);


        applicationUtil = (ApplicationUtil)this.getApplication();
        DNAME = applicationUtil.getDNAME();

        bitmap = applicationUtil.getAllBitmap();
        applicationUtil.setAllBitmap(null);
        if(returnActivity == returnFullModeActivity){
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int newHeight = (int) ((int) dm.heightPixels / 1.2);
            int newWidth  = newHeight*4/3;
            cropBitMap = zoomImg(bitmap,newWidth,newHeight);
        }else {
            cropBitMap = bitmap;
        }



        //获取bitmap宽度
        cropwidth = cropBitMap.getWidth();
        cropheight = cropBitMap.getHeight();

        cropImageView.setImageBitmap(getScaleUpBitmap(cropBitMap));      //将  //接收到的bitmap显示到自定义控件 cropImageView*/

    }

    // 等比缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public Bitmap getScaleUpBitmap(Bitmap bitmap) {
        float scaleWidth = 1, scaleHeight = 1;
        if (bitmap.getWidth() < cropwidth) {
            //强转为float类型，
            scaleWidth = (float)cropwidth / (float)bitmap.getWidth();
        }
        if (bitmap.getHeight() < cropheight) {
            scaleHeight = (float)cropheight / (float)bitmap.getHeight();
        }
       if (scaleWidth < scaleHeight)                   //判断是根据宽还是高来放大bitmap
            scaleHeight = scaleWidth;
        else
            scaleWidth = scaleHeight;
        Matrix matrix = new Matrix();
        //根据屏幕大小选择bitmap放大比例。
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight()
                , matrix, true);
        return bitmap;
    }


    /**
     * 裁剪
     * @param view
     */
    public void cropping(View view) {
        cropImageView.setVisibility(View.VISIBLE);
        advancedContainer.setVisibility(View.GONE);
        cropImageView.setImageBitmap(getScaleUpBitmap(cropBitMap));
        //获取裁剪的图片,
        cropBitMap = cropImageView.getCroppedImage();

        //调用getScaleUpBitmap(Bitmap bitmap) 得到按比例放大的 //将的bitmap显示到自定义控件 cropImageView
        cropImageView.setImageBitmap(getScaleUpBitmap(cropBitMap));
        //将图片保存到临时文件
        String path = saveImgProgram(DNAME,"TemporaryFile","test.jpg",cropBitMap);
    }

    /**
     * 返回
     * @param view
     */
    public void back(View view) {
        DialogController.showMsgDialog(this, "保存图片", null, "取消",
                "保存", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetTime getTime = new GetTime();
                        String jpgName = getTime.getTimeYYYYMMDD() + ".jpg";
                        saveImgProgram(DNAME,"picture" ,jpgName,cropBitMap);
                        finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    /**
     * 保存按钮
     * @param view
     */
    public void saveImg(View view) {
        GetTime getTime = new GetTime();
        String jpgName = getTime.getTimeYYYYMMDD() + ".jpg";
        saveImgProgram(DNAME,"picture" ,jpgName,cropBitMap);
        finish();
    }

//    /**
//     * 保存图片的程序，返回保存的路径
    public String saveImgProgram(String fileName,String signImage ,String imageName, Bitmap bitmap){
        String pathMy = Environment.getExternalStorageDirectory()+ File.separator;
        File test = Mkdirfolder.mkdirFolder(pathMy,fileName);
        SaveSignImageBox saveSignImageBox = new SaveSignImageBox();
        saveSignImageBox.saveSignImageBox(test.getPath(),signImage,imageName,bitmap);

        return test.getPath()+File.separator+signImage+File.separator;
    }



    @Override
    public void finish() {
        switch (returnActivity){
            case returnFullModeActivity:
                Thread thread  = (new Thread(){
                    @Override
                    public void run() {
                        Intent it=new Intent(getApplicationContext(), FullModeActivity.class);//启动FullModeActivity
                        startActivity(it);
                    }
                });
                thread.start();
                break;
            case returnLookImageActivity:
                Thread thread1  = (new Thread(){
                    @Override
                    public void run() {
                        Intent it=new Intent(getApplicationContext(), LookImage.class);//启动FullModeActivity
                        startActivity(it);
                    }
                });
                thread1.start();
                break;
            default:break;
        }
        super.finish();
    }

    /**
     * 画笔
     * @param view
     */
    public void brush(View view) {
        advancedContainer.setVisibility(View.VISIBLE);
        cropImageView.setVisibility(View.GONE);

//        // 涂鸦参数
        DoodleParams params = new DoodleParams();
        params.mIsFullScreen = true;

        //将图片保存到临时文件
        String path = saveImgProgram(DNAME,"TemporaryFile","test.jpg",cropBitMap);


        //读取图片路径
        params.mImagePath = path +"test.jpg";
      //  mSavePath ，保存图片路径
//        String pathMy = Environment.getExternalStorageDirectory()+ File.separator;
//        File test = Mkdirfolder.mkdirFolder(pathMy,DNAME);
       params.mSavePath = path;
        //保存路径是否为目录，如果为目录，则在该目录生成由时间戳组成的图片名称
        params.mSavePathIsDir = true;

        // 初始画笔大小
        params.mPaintUnitSize = DoodleView.DEFAULT_SIZE;
        // 画笔颜色
        params.mPaintColor = Color.RED;
        // 是否支持缩放item
        params.mSupportScaleItem = true;
        // 启动涂鸦页面
        DoodleActivity.startActivityForResult(this, params, REQ_CODE_DOODLE);

        //如果是返回，临时文件删除，将cropBitMap显示到doodleImageView控件
        doodleImageView .setImageBitmap(getScaleUpBitmap(cropBitMap));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_DOODLE) {
            if (data == null) {
                return;
            }
            if (resultCode == DoodleActivity.RESULT_OK) {
                String path = data.getStringExtra(DoodleActivity.KEY_IMAGE_PATH);
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                //显示到img控件

                cropBitMap = BitmapFactory.decodeFile(path);         //将涂鸦之后的图片赋给裁剪框
                //如果是完成，临时文件删除，将cropBitMap显示到doodleImageView控件
                doodleImageView .setImageBitmap(getScaleUpBitmap(cropBitMap));

                //将临时文件删除
                String deletepath = saveImgProgram(DNAME,"TemporaryFile","test.jpg",cropBitMap);
                VerifyStoragePermissions.verifyStoragePermissions(this);      //没有权限就申请
                File f = new File(deletepath);
                //删除文件
                DeleteFile.delete(f);
                DeleteFile.delete(f);             //删除文件夹
                DeleteFile.delete(f);             //检查

            } else if (resultCode == DoodleActivity.RESULT_ERROR) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }
    }




}
