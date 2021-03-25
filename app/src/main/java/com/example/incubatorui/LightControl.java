package com.example.incubatorui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.incubatorui.lightControl.LightControlOnClickListener;
import com.example.incubatorui.lightControl.MyOnClickRGB;
import com.example.incubatorui.sendMessage.Response;

public class LightControl extends AppCompatActivity {
    Context context;
    public ApplicationUtil applicationUtil;
    LinearLayout linearLayout;
    LinearLayout RGBSelectedEContainer;
    LinearLayout UV1LmapContainer;
    TextView txtLightR;
    TextView txtLightG;
    TextView txtLightB;
    MyOnClickRGB myOnClickRGB;
    ThreadUV1Lmap mThreadUV1Lmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_control);
        context = this;
        applicationUtil = (ApplicationUtil)this.getApplication();
        applicationUtil.setRD(true);
        RGBSelectedEContainer = findViewById(R.id.RGB_selectable_container);
        RGBSelectedEContainer.setVisibility(View.GONE);
        UV1LmapContainer = findViewById(R.id.UV1_container);
        UV1LmapContainer.setVisibility(View.GONE);

        linearLayout = findViewById(R.id.rgbLinearLayout);
        txtLightR = findViewById(R.id.tet_light_R);
        txtLightG = findViewById(R.id.tet_light_G);
        txtLightB = findViewById(R.id.tet_light_B);

        mThreadUV1Lmap = new  ThreadUV1Lmap();          //紫外线控制线程 开启
        mThreadUV1Lmap.start();

        myOnClickRGB = new MyOnClickRGB(){
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.tet_light_R:
                        ThreadRgb threadRgb1 = new ThreadRgb(0,"R");
                        threadRgb1.start();
                        break;
                    case R.id.tet_light_G:
                        ThreadRgb threadRgb2 = new ThreadRgb(1,"G");
                        threadRgb2.start();
                        break;
                    case R.id.tet_light_B:
                        ThreadRgb threadRgb3 = new ThreadRgb(2,"B");
                        threadRgb3.start();
                        break;
                    default:break;
                }
            }
            class ThreadRgb extends Thread{
                int num;
                String message;
                public ThreadRgb(int num,String message){
                    this.num  =num;
                    this.message = message;
                }
                @Override
                public void run(){
                    if(!applicationUtil.getSocketIs()){
                        showToast("服务器未连接");
                    }
                    if(!applicationUtil.getTcpSendIs()){
                        showToast("不允许指令发送");
                    }
                    int m = getmRow();
                    int n = getnList();
                    int[][][] rgb = getRgb();
                    //CMSLmCnR=0或1\r\n
                    if(rgb[m][n][num] == 0){
                        //点亮操作
                        String ms = "CMSL" + String.valueOf(m+1) + "C" + String.valueOf(n+1) + message + "=1\r\n";
                        applicationUtil.sendMessageF(ms);
                        //等待响应
                        Response r = new Response(applicationUtil,150);
                        r.start();
                        while (true){
                            if (r.getIsGo() == 2){
                                showToast("指令响应失败");
                                return;
                            }else {
                                if(r.getIsGo() == 1){
                                    System.out.println("指令响应成功");
                                    setNumLight(m,n,num,1);
                                    Resources resource = getBaseContext().getResources() ;
                                    Drawable myDrawable = null;
                                    //判断Rgb的红色
                                    if(rgb[m][n][num]==1){
                                        //取得资源的引用
                                        switch (num){
                                            case 0:myDrawable = resource.getDrawable(R.drawable.ic_rgb_r) ;
                                                break;
                                            case 1:myDrawable = resource.getDrawable(R.drawable.ic_rgb_g) ;
                                                break;
                                            case 2:myDrawable = resource.getDrawable(R.drawable.ic_rgb_b) ;
                                                break;
                                            default:break;
                                        }
                                    }else {
                                        //取得资源的引用
                                        myDrawable = resource.getDrawable(R.drawable.ic_rgb_off) ;
                                    }switch (num){
                                        case 0:txtLightR.setBackground(myDrawable);
                                            break;
                                        case 1:txtLightG.setBackground(myDrawable);
                                            break;
                                        case 2:txtLightB.setBackground(myDrawable);
                                            break;
                                        default:break;
                                    }
                                    break;
                                }
                            }
                            try {
                                sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }else {
                        String ms = "CMSL" + String.valueOf(m) + "C" + String.valueOf(n) + message + "=0\r\n";
                        applicationUtil.sendMessageF(ms);
                        //等待响应
                        Response r = new Response(applicationUtil,150);
                        r.start();
                        while (true){
                            if (r.getIsGo() == 2){
                                showToast("指令响应失败");
                                return;
                            }else {
                                if(r.getIsGo() == 1){
                                    System.out.println("指令响应成功");
                                    setNumLight(m,n,num,0);
                                    Resources resource = getBaseContext().getResources() ;
                                    Drawable myDrawable = null;
                                    //判断Rgb的红色
                                    if(rgb[m][n][num]==1){
                                        //取得资源的引用
                                        switch (num){
                                            case 0:myDrawable = resource.getDrawable(R.drawable.ic_rgb_r) ;
                                                break;
                                            case 1:myDrawable = resource.getDrawable(R.drawable.ic_rgb_g) ;
                                                break;
                                            case 2:myDrawable = resource.getDrawable(R.drawable.ic_rgb_b) ;
                                                break;
                                            default:break;
                                        }

                                    }else {
                                        //取得资源的引用
                                        myDrawable = resource.getDrawable(R.drawable.ic_rgb_off) ;
                                    }switch (num){
                                        case 0:txtLightR.setBackground(myDrawable);
                                            break;
                                        case 1:txtLightG.setBackground(myDrawable);
                                            break;
                                        case 2:txtLightB.setBackground(myDrawable);
                                            break;
                                        default:break;
                                    }
                                    break;
                                }
                            }
                            try {
                                sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }
            }
        };
        if(applicationUtil.getRgb() != null){
            myOnClickRGB.setRgb(applicationUtil.getRgb());
        }
        //绑定事件
        RgbBinding(myOnClickRGB);
        for (int i = 0; i<4; i++){
            LinearLayout linearLayout2 = new LinearLayout(this);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0, 1);

            linearLayout2.setLayoutParams(linearLayoutParams);
            linearLayout2.setOrientation(LinearLayout.HORIZONTAL);

            for ( int j = 0; j<6; j++){
                LinearLayout linearLayout3 = new LinearLayout(this);
                LinearLayout.LayoutParams linearLayout3Params = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 1);
                linearLayout3Params.setMargins(18,18,18,18);
                //getDimension
                linearLayout3.setLayoutParams(linearLayout3Params);
                linearLayout3.setOrientation(LinearLayout.VERTICAL);
                linearLayout3.setGravity(Gravity.CENTER);
                linearLayout3.setBackground(getResources().getDrawable(R.drawable.boder));
                TextView tv = new TextView(this);
                //设置宽高以及权重
                LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1);
                //内容居中
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams(tvParams);
                tv.setTextSize(14);
                tv.setText("RGB"+( i*6 + j + 1 ));
                ImageView imageView = new ImageView(this);
                //设置宽高以及权重
                LinearLayout.LayoutParams ImageViewTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1);
                ImageViewTvParams.bottomMargin=25;
                imageView.setLayoutParams(ImageViewTvParams);
                imageView.setImageResource(R.drawable.ic_rgb); //图片资源
                // System.out.println((i*6 + j + 1 ));
                linearLayout3.addView(tv);          //添加textview
                linearLayout3.addView(imageView); //动态添加图片
                //添加图片点击事件,将当前图片地址传给自定义的响应事件
                imageView.setOnClickListener(new LightControlOnClickListener(i,j) {
                    //点击图片触发，获取当前对象（MyOnClickListener）所存的地址值
                    @Override
                    public void onClick(View v) {
                        System.out.println("按下rgb" + getmRow() + "    "+getnList());
                        //设置当前点击的行和列
                        myOnClickRGB.setMN(getmRow(),getnList());
                        //获取这个灯的rgb状态
                        int[][][] rgb = myOnClickRGB.getRgb();
                        Resources resource = getBaseContext().getResources() ;
                        Drawable myDrawable;
                        //判断Rgb的红色
                        if(rgb[getmRow()][getnList()][0]==1){
                            //取得资源的引用
                            myDrawable = resource.getDrawable(R.drawable.ic_rgb_r) ;
                        }else {
                            //取得资源的引用
                            myDrawable = resource.getDrawable(R.drawable.ic_rgb_off) ;
                        }
                        txtLightR.setBackground(myDrawable);
                        //判断Rgb的绿色
                        if(rgb[getmRow()][getnList()][1]==1){
                            //取得资源的引用
                            myDrawable = resource.getDrawable(R.drawable.ic_rgb_g) ;
                        }else {
                            //取得资源的引用
                            myDrawable = resource.getDrawable(R.drawable.ic_rgb_off) ;
                        }
                        txtLightG.setBackground(myDrawable);
                        //判断Rgb的绿色
                        if(rgb[getmRow()][getnList()][2]==1){
                            //取得资源的引用
                            myDrawable = resource.getDrawable(R.drawable.ic_rgb_b) ;
                        }else {
                            //取得资源的引用
                            myDrawable = resource.getDrawable(R.drawable.ic_rgb_off) ;
                        }
                        txtLightB.setBackground(myDrawable);
                        //将布局显示出来
                        RGBSelectedEContainer.setVisibility(View.VISIBLE);

                    }
                });
                linearLayout2.addView(linearLayout3);
            }
            linearLayout.addView(linearLayout2);
        }
    }
    public void RgbBinding(MyOnClickRGB myOnClickRGB){
        txtLightR.setOnClickListener(myOnClickRGB);
        txtLightG.setOnClickListener(myOnClickRGB);
        txtLightB.setOnClickListener(myOnClickRGB);
    }

    /**
     * 紫外灯 UV1 lamp
     * @param view
     */
    public void UV1Lamp(View view) {
        if(UV1LmapContainer.getVisibility()==View.GONE){
            RGBSelectedEContainer.setVisibility(View.GONE);
            UV1LmapContainer.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 紫外灯 UV1Lmap
     */
    class ThreadUV1Lmap extends Thread{
        @Override
        public void run() {
            int UV1R[] = applicationUtil.getUV1();
            for (int i = 1; i <= 4; i++){
                if (i==3)continue;
                LinearLayout linearLayout2 = new LinearLayout(context);                       //                   宽    match_parent   高  0   占比   1
                LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0, 1);

                linearLayout2.setLayoutParams(linearLayoutParams);
                linearLayout2.setOrientation(LinearLayout.HORIZONTAL);

                for ( int j = 1; j<=5; j++){
                    LinearLayout linearLayout3 = new LinearLayout(context);
                    LinearLayout.LayoutParams linearLayout3Params = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    linearLayout3Params.setMargins(18,18,18,18);
                    //getDimension
                    linearLayout3.setLayoutParams(linearLayout3Params);
                    linearLayout3.setOrientation(LinearLayout.VERTICAL);
                    linearLayout3.setGravity(Gravity.CENTER);
                    linearLayout3.setBackground(getResources().getDrawable(R.drawable.boder));
                    TextView tv = new TextView(context);
                    //设置宽高以及权重
                    LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1);
                    //内容居中
                    tv.setGravity(Gravity.CENTER);
                    tv.setLayoutParams(tvParams);
                    tv.setTextSize(14);
                    if (i==4){                                          //因为要通过3 ，命名又没有跳过，所以i==4，减2
                        tv.setText("Z"+( (i-2)*5 + j ));
                    }else
                        tv.setText("Z"+( (i-1)*5 + j ));

                    ImageView imageView = new ImageView(context);
                    //设置宽高以及权重
                    LinearLayout.LayoutParams ImageViewTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1);
                    ImageViewTvParams.bottomMargin=25;
                    imageView.setLayoutParams(ImageViewTvParams);
                    int index = i*5+j > 15 ? (i*5+j)-10 : i*5+j-5;
                    if(UV1R[index] == 0){
                        imageView.setImageResource(R.drawable.ic_uv_off); //图片资源
                    }else {
                        imageView.setImageResource(R.drawable.ic_uv_on); //图片资源
                    }
                    linearLayout3.addView(tv);          //添加textview
                    linearLayout3.addView(imageView); //动态添加图片
                    //添加图片点击事件,
                    imageView.setOnClickListener(new LightControlOnClickListener(i,j) {     //
                        //点击图片触发，获取当前对象（MyOnClickListener）所存的地址值
                        @Override
                        public void onClick(View v) {
                            int[] UV1 =applicationUtil.getUV1();
                            int index = getmRow()*5+getnList() > 15 ? (getmRow()*5+getnList())-10 : getmRow()*5+getnList()-5;
                            int value;
                            //没点亮的状态
                            if(UV1[index] == 0){
                                applicationUtil.sendMessageF("CMSL"+getmRow()+"C"+getnList()+"Z=1\r\n");
                                value = 1;
                            }else {
                                applicationUtil.sendMessageF("CMSL"+getmRow()+"C"+getnList()+"Z=0\r\n");
                                value = 0;
                            }
                            //响应
                            Response r1 = new Response(applicationUtil,150);
                            r1.start();
                            while (true){
                                if (r1.getIsGo() == 2){
                                    Toast.makeText(context,"指令响应失败",Toast.LENGTH_SHORT).show();
                                    return;
                                }else {
                                    if(r1.getIsGo() == 1){
                                        System.out.println("指令响应成功");
                                        //更改灯状态
                                        applicationUtil.setUV1(index,value);
                                        ImageView isi = (ImageView)v;
                                        if(value == 0){
                                            isi.setImageResource(R.drawable.ic_uv_off); //图片资源
                                        }else {
                                            isi.setImageResource(R.drawable.ic_uv_on); //图片资源
                                        }
                                        break;
                                    }
                                }
                                try {
                                    sleep(20);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    linearLayout2.addView(linearLayout3);
                }
                UV1LmapContainer.addView(linearLayout2);
            }

        }
    }
    /**
     * 返回
     */
    public void back(View view) {
        finish();
    }
    /**
     * 重写返回方法（系统返回键执行该方法）
     */
    @Override
    public void finish(){
        applicationUtil.setRgb(myOnClickRGB.getRgb());
        applicationUtil.setRD(false);
        if (RGBSelectedEContainer.getVisibility()== View.VISIBLE){     //如果RGB选择是显示，则将其隐藏
            RGBSelectedEContainer.setVisibility(View.GONE);
        }
        else if(UV1LmapContainer.getVisibility()==View.VISIBLE){
            UV1LmapContainer.setVisibility(View.GONE);
        }
        else {
            Thread thread  = (new Thread(){
                @Override
                public void run() {
                    Intent it=new Intent(getApplicationContext(), FullModeActivity.class);//启动MainActivity
                    startActivity(it);
                }
            });
            thread.start();
            super.finish();
        }
    }

    /**
     * 提示框
     * @param msg
     */
    public void showToast(String msg){
        Looper.prepare();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}
