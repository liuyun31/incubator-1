package com.example.incubatorui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.incubatorui.BigImageUilts.MyOnClickListener;
import com.example.incubatorui.utils.ImageUtils.GetsPathImage;
import com.example.incubatorui.utils.ImageUtils.VerifyStoragePermissions;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LookImage extends AppCompatActivity implements View.OnClickListener {
    LinearLayout llGroup = null;
    private Button bntBack;
    private Button bntDate;
    //所有图片数量
    int i;//当前图片位置
    ;//图片地址集
    boolean returnMain = true;//是否返回主页面（MainActivity）

    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_look_image);
        //绑定监听
        llGroup = findViewById(R.id.ll_group);
        bntBack = findViewById(R.id.btnBack);
        bntDate = findViewById(R.id.btnDate);
        bntBack.setOnClickListener(this);
        bntDate.setOnClickListener(this);
        ApplicationUtil applicationUtil = (ApplicationUtil)this.getApplication();
        String APPPath = applicationUtil.getDNAME();
        //获取权限
        VerifyStoragePermissions.verifyStoragePermissions(this);
        //图片文件夹地址
        String path = Environment.getExternalStorageDirectory() + APPPath + "/picture" ;
        GetsPathImage getsPathImage = new GetsPathImage();
        List<String> paths = getsPathImage.getFilesAllName(path,null);
        if(paths != null){
            addGroupImage(paths.size(),paths);
        }
    }

    @Override
    public void onClick(View view) {
        System.out.println("点击事件");
        switch (view.getId()) {
            case R.id.btnBack:
                returnMain = true;
                finish();
                break;
            case R.id.btnDate:
                System.out.println("按下日期");
                showDatePickerDialog(this,  2, calendar);;
                break;
            default:
                break;
        }
    }

    /**
     * 日期选择
     * @param activity
     * @param themeResId
     * @param calendar
     */
    public  void showDatePickerDialog(Activity activity, int themeResId, Calendar calendar) {

        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                System.out.println("您选择了：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");

                String y = String.valueOf(year);
                String m = String.valueOf(monthOfYear+1);
                String d = String.valueOf(dayOfMonth);
                //补零
                if(monthOfYear + 1 <10){
                    m = "0" + (monthOfYear+1);
                }
                if(dayOfMonth < 10){
                    d = "0" + dayOfMonth;
                }
                String time = y + m + d;
                //图片文件夹地址
                String path = Environment.getExternalStorageDirectory() + "/incubator/picture/";
                GetsPathImage getsPathImage = new GetsPathImage();
                List<String> paths = getsPathImage.getFilesAllName(path,time);
                if(paths != null){
                    System.out.println("图片数量：" + paths.size());
                    addGroupImage(paths.size(),paths);
                }
            }

        }
        // 设置初始日期
        , calendar.get(Calendar.YEAR)
        , calendar.get(Calendar.MONTH)
        , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    @Override
    public void finish() {
        if(returnMain){
            Thread thread  = (new Thread(){
                @Override
                public void run() {
                    Intent it=new Intent(getApplicationContext(), MainActivity.class);//启动MainActivity
                    startActivity(it);
                }
            });
            thread.start();
        }
        super.finish();
    }

    /**
     * 添加图片数
     * @param size 所有图片数量
     * @param paths 图片地址集
     */
    public void addGroupImage(int size, List<String> paths){
        if(paths == null){
            Log.d("TAG", "内容为空");
            return;
        }else{
            Log.d("TAG", "内容长度为:"+String.valueOf(size));
        }
        //计算每行放3张图片需要多少行
        int row = size/3 + 1;
        i = size-1;
        //清空布局内容
        llGroup.removeAllViews();  //clear linearlayout
        for(int j=0;j<row;j++){
            //创建每行的LinearLayout，设为水平布局，高度为220
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, 220));
            //每行生成3个图片
            for ( int n =0; i >= 0 && n < 3; i--,n++) {
                //在每行中添加3张图片
                ImageView imageView = new ImageView(this);
                //imageView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));  //设置图片宽高
                imageView.setLayoutParams(new ActionBar.LayoutParams(360, 260));  //设置图片宽高
                //imageView.setImageBitmap(BitmapFactory.decodeFile(paths.get(i))); //图片资源
                //添加图片点击事件,将当前图片地址传给自定义的响应事件
                imageView.setOnClickListener(new MyOnClickListener(paths.get(i)) {
                    //点击图片触发，获取当前对象（MyOnClickListener）所存的地址值
                    String path1 = this.getPath();
                    @Override
                    public void onClick(View v) {
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    Intent intent = new Intent();
                                    //将图片地址传过去显示
                                    intent.putExtra("path",path1);
                                    //System.out.println("放大图片的地址为："  + path1);
                                    intent.setClass(getApplicationContext(),BigImage.class);
                                    startActivity(intent);
                                    //关闭当前页面且不回MainActivity
                                    returnMain = false;
                                    finish();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        //执行该线程
                        thread.start();
                    }
                });
                //添加图片边距
                imageView.setPadding(4,4,4,4);

                Glide.with(this)
                        .applyDefaultRequestOptions(
                                new RequestOptions()
                                        .format(DecodeFormat.PREFER_ARGB_8888))
                                            .load(new File(paths.get(i))).thumbnail(0.5f).centerCrop().into(imageView);
                linearLayout1.addView(imageView); //动态添加图片
            }
            llGroup.addView(linearLayout1);
        }
    }
}