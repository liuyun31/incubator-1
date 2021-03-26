package com.example.incubatorui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.incubatorui.sendMessage.MyOnClickABCD;
import com.example.incubatorui.sendMessage.MyOnClickZero;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

public class SiteSelection extends AppCompatActivity implements View.OnClickListener{
    ApplicationUtil applicationUtil;
    private Context context;
    //区域定位
    LinearLayout a1,a2,a3,a4,a5,a6;
    LinearLayout d1,d2,d3,d4,d5,d6;
    LinearLayout b1,b2,b3,b4,b5,b6;
    LinearLayout c1,c2,c3,c4,c5,c6;

    //各个按钮响应的布局
    LinearLayout siteSelect;
    LinearLayout activity_OriginControl;
    LinearLayout activity_llFixedWay;
    LinearLayout activity_absolute_coordinate_setting;
    LinearLayout activity_relative_coordinate_setting;
    LinearLayout activity_focal_setting;
    //各个按钮
    Button btnrefresh;
    Button btnOrigin;
    Button btnFixed;
    Button btnFocal;
    Button btnRelativeCoordinates;
    Button btnAbsoluteCoordinates;
    //当前坐标点位
    TextView txtXCoordinate;
    TextView txtYCoordinate;
    TextView txtZCoordinate;
    //当前坐标点位数据
    String XCoordinate;
    String YCoordinate;
    String ZCoordinate;
    //归零按钮
    Button btnXZero;
    Button btnYZero;
    Button btnZZero;
    Button btnFilterZero;
    //当前区域
    TextView txtCoordinateData;
    //解析当前位置
    int m=-1;//行
    int n=-1;//列
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_site_selection);
        applicationUtil = (ApplicationUtil)this.getApplication();
        applicationUtil.setRD(true);
        context = this;

        //绑定视图
        bindingView();
        //绑定监听
        bindingListener();
        //请求并更新坐标
        upDataCoordinate();
    }

    /**
     * 请求当前坐标
     */
    class ThreadRequestCoordinate extends Thread{
        @Override
        public void run() {
            XCoordinate = "X:  ";
            YCoordinate = "Y:  ";
            ZCoordinate = "Z:  ";
            String aX = applicationUtil.getXTotal();
            String aY = applicationUtil.getYTotal();
            String aZ = applicationUtil.getZTotal();
            int dwx = aX.indexOf(",");
            int dwy = aY.indexOf(",");
            int dwz = aZ.indexOf(",");
            XCoordinate = XCoordinate + aX.substring(0,dwx) + "圈" + aX.substring(dwx+1) + "步";
            YCoordinate = YCoordinate + aY.substring(0,dwy) + "圈" + aY.substring(dwy+1) + "步";
            ZCoordinate = ZCoordinate + aZ.substring(0,dwz) + "圈" + aZ.substring(dwz+1) + "步";
            runOnUiThread(new Thread(){
                @Override
                public void run() {
                    txtXCoordinate.setText(XCoordinate);
                    txtYCoordinate.setText(YCoordinate);
                    txtZCoordinate.setText(ZCoordinate);
                }
            });
            //解析当前位置
            m=-1;//行
            n=-1;//列
            String X = applicationUtil.getXTotal();
            String Y = applicationUtil.getYTotal();
            int Xd = X.indexOf(",");
            int Yd = Y.indexOf(",");
            int x;
            int y;
            try {
                x = Integer.parseInt(X.substring(0,Xd)) * 6400 + Integer.parseInt(X.substring(Xd+1));
                y = Integer.parseInt(Y.substring(0,Yd)) * 1536 + Integer.parseInt(Y.substring(Yd+1));
            }catch (Exception e){
                showToast("当前位置有误");
                e.printStackTrace();
                return;
            }

            /*
            M  13800    43650  73500  103350  133200
            N   42400   72267  102133 131998  161885   191770    221600
             */
            if(y >= 13800){ m=4;
                if(y >= 43650){ m=3;
                    if(y >= 73500){ m=2;
                        if(y >= 103350){ m=1;
                            if(y > 133200){m=-1;
                            }
                        }
                    }
                }
            }else{
                m=-1;
            }
            if(x >= 42400){ n=1;
                if(x>=72267){ n=2;
                    if(x>=102133){ n=3;
                        if(x>=131998){ n=4;
                            if(x>=161885){ n=5;
                                if(x>=191770){ n=6;
                                    if(x > 221600){n=-1;
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                n=-1;
            }
            runOnUiThread(new Thread(){
                @Override
                public void run() {
                    if(n <=0 || m <= 0){
                        txtCoordinateData.setText("无");
                    }else {
                        switch (m){
                            case 1:txtCoordinateData.setText("A" + n);
                                break;
                            case 2:txtCoordinateData.setText("B" + n);
                                break;
                            case 3:txtCoordinateData.setText("C" + n);
                                break;
                            case 4:txtCoordinateData.setText("D" + n);
                                break;
                        }
                    }
                }
            });
        }
    }

    /**
     * 更新UI的方法
     */
    public void upDataCoordinate(){
        //请求并更新坐标
        ThreadRequestCoordinate trc = new ThreadRequestCoordinate();
        trc.start();
    }
    /**
     * 返回
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * 重写返回方法（系统返回键执行该方法）
     */
    @Override
    public void finish(){
        applicationUtil.setRD(false);
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

    //绑定控件
    public void bindingView(){
        siteSelect = findViewById(R.id.siteSelect);
        activity_OriginControl = findViewById(R.id.activity_OriginControl);
        activity_llFixedWay = findViewById(R.id.activity_llFixedWay);
        activity_absolute_coordinate_setting = findViewById(R.id.activity_absolute_coordinate_setting);
        activity_relative_coordinate_setting = findViewById(R.id.activity_relative_coordinate_setting);
        activity_focal_setting = findViewById(R.id.activity_focal_setting);
        btnrefresh = findViewById(R.id.btn_refresh);
        btnOrigin = findViewById(R.id.btn_Origin);
        btnFixed = findViewById(R.id.btn_Fixed);
        btnFocal = findViewById(R.id.btn_focal);
        btnRelativeCoordinates = findViewById(R.id.btnRelativeCoordinates);
        btnAbsoluteCoordinates = findViewById(R.id.btnAbsoluteCoordinates);

        txtXCoordinate = findViewById(R.id.txt_XCoordinate);
        txtYCoordinate = findViewById(R.id.txt_YCoordinate);
        txtZCoordinate = findViewById(R.id.txt_ZCoordinate);

        btnXZero = findViewById(R.id.btn_XZero);
        btnYZero = findViewById(R.id.btn_YZero);
        btnZZero = findViewById(R.id.btn_ZZero);
        btnFilterZero = findViewById(R.id.btn_filterZero);

        txtCoordinateData = findViewById(R.id.txt_coordinate_data);

        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);
        a5 = findViewById(R.id.a5);
        a6 = findViewById(R.id.a6);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        b6 = findViewById(R.id.b6);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        d1 = findViewById(R.id.d1);
        d2 = findViewById(R.id.d2);
        d3 = findViewById(R.id.d3);
        d4 = findViewById(R.id.d4);
        d5 = findViewById(R.id.d5);
        d6 = findViewById(R.id.d6);
    }

    /**
     * 添加自定义监听
     */
    private void bindingListener() {
        btnrefresh.setOnClickListener(this);
        btnOrigin.setOnClickListener(this);
        btnFixed.setOnClickListener(this);
        btnFocal.setOnClickListener(this);
        btnRelativeCoordinates.setOnClickListener(this);
        btnAbsoluteCoordinates.setOnClickListener(this);

        btnXZero.setOnClickListener(new MyOnClickZero(applicationUtil,this,this));
        btnYZero.setOnClickListener(new MyOnClickZero(applicationUtil,this,this));
        btnZZero.setOnClickListener(new MyOnClickZero(applicationUtil,this,this));
        btnFilterZero.setOnClickListener(new MyOnClickZero(applicationUtil,this,this));

        a1.setOnClickListener(new MyOnClickABCD(applicationUtil,a1.getId(),this,this));
        a2.setOnClickListener(new MyOnClickABCD(applicationUtil,a2.getId(),this,this));
        a3.setOnClickListener(new MyOnClickABCD(applicationUtil,a3.getId(),this,this));
        a4.setOnClickListener(new MyOnClickABCD(applicationUtil,a4.getId(),this,this));
        a5.setOnClickListener(new MyOnClickABCD(applicationUtil,a5.getId(),this,this));
        a6.setOnClickListener(new MyOnClickABCD(applicationUtil,a6.getId(),this,this));
        b1.setOnClickListener(new MyOnClickABCD(applicationUtil,b1.getId(),this,this));
        b2.setOnClickListener(new MyOnClickABCD(applicationUtil,b2.getId(),this,this));
        b3.setOnClickListener(new MyOnClickABCD(applicationUtil,b3.getId(),this,this));
        b4.setOnClickListener(new MyOnClickABCD(applicationUtil,b4.getId(),this,this));
        b5.setOnClickListener(new MyOnClickABCD(applicationUtil,b5.getId(),this,this));
        b6.setOnClickListener(new MyOnClickABCD(applicationUtil,b6.getId(),this,this));
        c1.setOnClickListener(new MyOnClickABCD(applicationUtil,c1.getId(),this,this));
        c2.setOnClickListener(new MyOnClickABCD(applicationUtil,c2.getId(),this,this));
        c3.setOnClickListener(new MyOnClickABCD(applicationUtil,c3.getId(),this,this));
        c4.setOnClickListener(new MyOnClickABCD(applicationUtil,c4.getId(),this,this));
        c5.setOnClickListener(new MyOnClickABCD(applicationUtil,c5.getId(),this,this));
        c6.setOnClickListener(new MyOnClickABCD(applicationUtil,c6.getId(),this,this));
        d1.setOnClickListener(new MyOnClickABCD(applicationUtil,d1.getId(),this,this));
        d2.setOnClickListener(new MyOnClickABCD(applicationUtil,d2.getId(),this,this));
        d3.setOnClickListener(new MyOnClickABCD(applicationUtil,d3.getId(),this,this));
        d4.setOnClickListener(new MyOnClickABCD(applicationUtil,d4.getId(),this,this));
        d5.setOnClickListener(new MyOnClickABCD(applicationUtil,d5.getId(),this,this));
        d6.setOnClickListener(new MyOnClickABCD(applicationUtil,d6.getId(),this,this));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btn_refresh://刷新
                final RotateAnimation animation = new RotateAnimation(0.0f, 180.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration( 500 );
                btnrefresh.startAnimation( animation );
                break;
            case R.id.btn_Origin://归零设置
                System.out.println("按下原点");
                if(activity_OriginControl.getVisibility() == View.GONE)
                {
                    activity_OriginControl.setVisibility(View.VISIBLE);
                    siteSelect.setVisibility(View.GONE);
                    activity_llFixedWay.setVisibility(View.GONE);
                    activity_focal_setting.setVisibility(View.GONE);
                    activity_relative_coordinate_setting.setVisibility(View.GONE);
                    activity_absolute_coordinate_setting.setVisibility(View.GONE);
                }else {
                    activity_OriginControl.setVisibility(View.GONE);
                    siteSelect.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_Fixed://坐标定点
                System.out.println("按下定点");
                if(activity_llFixedWay.getVisibility() == View.GONE)
                {
                    activity_llFixedWay.setVisibility(View.VISIBLE);
                    siteSelect.setVisibility(View.GONE);
                    activity_OriginControl.setVisibility(View.GONE);
                    activity_focal_setting.setVisibility(View.GONE);
                    activity_relative_coordinate_setting.setVisibility(View.GONE);
                    activity_absolute_coordinate_setting.setVisibility(View.GONE);

                }else {
                    siteSelect.setVisibility(View.VISIBLE);
                    activity_llFixedWay.setVisibility(View.GONE);
                    activity_absolute_coordinate_setting.setVisibility(View.GONE);
                    activity_relative_coordinate_setting.setVisibility(View.GONE);

                }
                break;
            case R.id.btn_focal://调整焦距
                System.out.println("按下调焦");
                if(activity_focal_setting.getVisibility() == View.GONE)
                {
                    activity_focal_setting.setVisibility(View.VISIBLE);
                    siteSelect.setVisibility(View.GONE);
                    activity_llFixedWay.setVisibility(View.GONE);
                    activity_OriginControl.setVisibility(View.GONE);
                    activity_relative_coordinate_setting.setVisibility(View.GONE);
                    activity_absolute_coordinate_setting.setVisibility(View.GONE);

                }else {
                    activity_focal_setting.setVisibility(View.GONE);
                    siteSelect.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btnRelativeCoordinates://定点里的相对位置
                if(activity_relative_coordinate_setting.getVisibility() == View.GONE)
                {
                    activity_relative_coordinate_setting.setVisibility(View.VISIBLE);
                    siteSelect.setVisibility(View.GONE);
                    activity_llFixedWay.setVisibility(View.GONE);
                }
                break;
            case R.id.btnAbsoluteCoordinates://定点里的绝对位置
                if(activity_absolute_coordinate_setting.getVisibility() == View.GONE)
                {
                    activity_absolute_coordinate_setting.setVisibility(View.VISIBLE);
                    siteSelect.setVisibility(View.GONE);
                    activity_llFixedWay.setVisibility(View.GONE);
                }
                break;
            default:break;
        }
    }

    /**
     * 更新 load 等待 的 Dialog 弹框
     */
    private void  runOnUiThreadLoadingDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog = new LoadingDialog(context)  //显示等待  loadingDialog
                        .setLoadingText("响应中...")
                        .setSuccessText("指令响应成功")//显示加载成功时的文字
                        .setFailedText("指令响应失败")
                        .setLoadSpeed(LoadingDialog.Speed.SPEED_TWO)
                        .setInterceptBack(true)
                        .setShowTime(10);
                loadingDialog.show();
                return;
            }
        });
    }
    LoadingDialog loadingDialog;


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