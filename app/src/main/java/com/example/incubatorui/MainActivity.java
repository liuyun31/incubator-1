package com.example.incubatorui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.incubatorui.GetJPEG.MyRnnable;
import com.example.incubatorui.sendMessage.MyOnClickBegin;
import com.example.incubatorui.sendMessage.Response;
import com.example.incubatorui.sendMessage.SensorDataRequest;
import com.example.incubatorui.utils.ImageUtils.VerifyStoragePermissions;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

public class MainActivity extends AppCompatActivity {

    public static final int ActivityClient = 0;//返回连接
    public static final int ActivityImage = 1;//跳转查看图片
    public static final int ActivityFull  = 2;//全屏控制
    LoadingDialog loadingDialog;
    Context context;
    Activity activity;
    //传感器数据
    public TextView txtCo2Data;
    public TextView txtTemp1Data;
    public TextView txtTemp2Data;
    public TextView txtHumiData;
    public TextView txtPressure;

    //当前坐标点位
    TextView txtXCoordinateMain;
    TextView txtYCoordinateMain;
    TextView txtZCoordinateMain;
    //当前坐标点位数据
    String XCoordinateMain;
    String YCoordinateMain;
    String ZCoordinateMain;
    //序列号
    TextView txtID;
    //仪器状态
    TextView txtInstrument;
    //打开摄像头
    Button btnCamera;
    boolean camera = false;

    ApplicationUtil applicationUtil;
    ThreadSocketIndicate threadSocketIndicate;
    ThreadUpDataSensor threadUpDataSensor;
    ImageView imageView;
    ImageView imageViewOff;
    ImageView imageViewOn;
    public boolean RD = true;
    int switchActivity=0;
    boolean upDataSensor = false;

    int sensorIsInit = 0;
    //初始化按钮
    Button btnMainBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_main);
        applicationUtil = (ApplicationUtil)this.getApplication();
        context=this;
        activity = this;
        bindingView();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreen(v);
            }
        });

        //判断连接状态
        if(applicationUtil.getSocketIs()){
            //获取连接
            try {
                System.out.println("调用applicationutil");
                applicationUtil.setMainActivity(this);

                //指示灯
                imageViewOff.setVisibility(View.GONE);   //隐藏off
                imageViewOn.setVisibility(View.VISIBLE);  //显示on
                //实时更新tcp连接状态
                threadSocketIndicate = new ThreadSocketIndicate();
                threadSocketIndicate.start();
                //定时 更新传感器数据
                threadUpDataSensor = new ThreadUpDataSensor();
                upDataSensor = true;
                threadUpDataSensor.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //获取摄像头状态
            camera = applicationUtil.isCamera();
        }
    }

    /**
     * 绑定视图控件
     */
    public void bindingView(){
        //绑定控件
        //传感器数据
        txtCo2Data = findViewById(R.id.txt_Co2_Data);
        txtHumiData = findViewById(R.id.txt_humi_data);
        txtPressure = findViewById(R.id.txt_pressure_data);
        txtTemp1Data = findViewById(R.id.txt_temp1_data);
        txtTemp2Data = findViewById(R.id.txt_temp2_data);
        //坐标数据
        txtXCoordinateMain = findViewById(R.id.txt_X_Coordinate_Main);
        txtYCoordinateMain = findViewById(R.id.txt_Y_Coordinate_Main);
        txtZCoordinateMain = findViewById(R.id.txt_Z_Coordinate_Main);
        //序列号
        txtID = findViewById(R.id.txt_ID);
        //仪器状态
        txtInstrument = findViewById(R.id.txt_Instrument);
        //打开摄像头
        btnCamera = findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(new ButtonCameraListener());
        imageView = findViewById(R.id.imgJpeg);
        imageViewOff = findViewById(R.id.imageViewOff);
        imageViewOn = findViewById(R.id.imageViewOn);

        btnMainBegin = findViewById(R.id.btn_MainBegin);
        btnMainBegin.setOnClickListener(new MyOnClickBegin(applicationUtil,context,activity));
    }
    /**
     * 打开关闭摄像头监听类
     */
    class ButtonCameraListener implements View.OnClickListener {
        private int or;
        @Override
        public void onClick(View v) {
            if(!applicationUtil.getSocketIs()){
                Toast.makeText(context,"服务器未连接",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!applicationUtil.getTcpSendIs()){
                Toast.makeText(context,"不允许发送指令",Toast.LENGTH_SHORT).show();
                return;
            }
            if(camera){
                //关闭
                or = 0;
            }else {
                //打开
                or = 1;
            }
            Thread t = new Thread(){
                @Override
                public void run() {
                    if(or == 1){
                        //打开摄像头指令
                        applicationUtil.sendMessageF("CMOpenCamera\r\n");
                    }else {
                        applicationUtil.sendMessageF("CMCloseCamera\r\n");
                    }
                    Response rr = new Response(applicationUtil,150);
                    rr.start();
                    while (true){
                        if (rr.getIsGo() == 2){
                            showToast("指令响应失败");
                            return;
                        }else {
                            if(rr.getIsGo() == 1){
                                System.out.println("指令响应成功");
                                camera = !camera;
                                if(or == 1){
                                    btnCamera.setText("开");
                                    System.out.println("这里是开");
                                }else {
                                    btnCamera.setText("关");
                                    System.out.println("这里是关");
                                }
                                applicationUtil.setCamera(camera);
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
            };
            t.start();
        }
    }
    /**
     * 定时 更新传感器数据
     */
    class ThreadUpDataSensor extends Thread{
        ApplicationUtil sensor = applicationUtil;
        @Override
        public void run() {

            try {

                while (upDataSensor){
                    if(!applicationUtil.getSocketIs()){
                        showToast("服务器未连接");
                    }
                    if(!applicationUtil.getTcpSendIs()){
                        showToast("不允许发送指令");
                    }
                    if(sensorIsInit == 0){
                        sleep(5000);
                        sensorIsInit = 1;
                    }else {
                        sleep(20000);
                    }

                    SensorDataRequest s = new SensorDataRequest(sensor);
                    s.start();
                    while (true){
                        if(s.isOK()){
                            break;
                        }
                        sleep(100);
                    }
                    if(s.getNum() >= 5){
                        //更新UI
                        //更新UI
                        upDataSensorUI();
                        //更新ui
                        upDataCoordinateMain();
                        System.out.println("传感器数据请求成功");
                    }else {
                        showToast("数据请求失败");
                        //Toast.makeText(activity,"数据请求失败",Toast.LENGTH_SHORT).show();
                        //数据请求失败
                        System.out.println("数据请求失败，请检查，失败编号:" + s.getNum());
                    }


                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新传感器UI方法
     */
    public void upDataSensorUI(){
        ApplicationUtil a1 =this.applicationUtil;
        runOnUiThread(new ThreadUI(a1){
            @Override
            public void run() {
                if(applicationUtil.getCO2() !=null && !applicationUtil.getCO2().equals("")){
                    txtCo2Data.setText(applicationUtil.getCO2());
                }
                if(applicationUtil.getH() != null && !applicationUtil.getH().equals("")){
                    txtHumiData.setText(applicationUtil.getH());
                }
                if(applicationUtil.getT1() != null && !applicationUtil.getT1().equals("")){
                    txtTemp1Data.setText(applicationUtil.getT1());
                }
                if(applicationUtil.getT2() != null && !applicationUtil.getT2().equals("")){
                    txtTemp2Data.setText(applicationUtil.getT2());
                }
                if(applicationUtil.getP() != null && !applicationUtil.getP().equals("")){
                    txtPressure.setText(applicationUtil.getP());
                }
            }
        });
    }
    //传applicationUtil对象给runOnUiThread的线程
    class ThreadUI extends Thread{
        ApplicationUtil applicationUtil;
        public ThreadUI(ApplicationUtil a){
            this.applicationUtil = a;
        }
        public ApplicationUtil getApplicationUtil() {
            return applicationUtil;
        }
    }
    /**
     * 实时获取Socket状态，并更新指示灯UI的线程
     */
    class ThreadSocketIndicate extends Thread{
        ApplicationUtil a = applicationUtil;
        @Override
        public void run() {
            while (true){
                try {
                    sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(a.getSocketIs()){

                    if(imageViewOn.getVisibility()==View.GONE){
                        //更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //指示灯
                                imageViewOff.setVisibility(View.GONE);   //隐藏off
                                imageViewOn.setVisibility(View.VISIBLE);  //显示on
                            }
                        });
                    }
                }else{
                    if(applicationUtil.getSocket() == null){
                        return;
                    }
                    applicationUtil.setSocket(null);
                    if(imageViewOff.getVisibility() == View.GONE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViewOn.setVisibility(View.GONE);  //隐藏on
                                imageViewOff.setVisibility(View.VISIBLE);   //显示off
                            }
                        });
                    }
                }

            }
        }
    }

    /**
     * 返回连接
     * @param view
     */
    public void back(View view) {
        applicationUtil.setZeroIs(0);
        applicationUtil.setSocket(null);
        switchActivity = ActivityClient;
        finish();
    }

    /**
     *查看图片
     * @param view
     */
    public void viewImage(View view) {
        //选择跳转的页面
        switchActivity = ActivityImage;
        finish();
    }

    /**
     * 全屏控制
     * @param view
     */
    public void fullScreen(View view) {

        //选择跳转的页面
        switchActivity = ActivityFull;
        finish();
    }

    /**
     * 图片更新，由ApplicationUtil调用
     * @param bitmap
     */
    public void updateImage(Bitmap bitmap){
        runOnUiThread(new MyRnnable(bitmap){
            @Override
            public void run() {
                imageView.setImageBitmap(getBitmap());
            }
        });
    }

    public void stopUpdateImage(){
        applicationUtil.stopUpdateMainImg();
    }

    /**
     * 坐标UI更新方法
     * 更新序列号
     * 更新仪器状态
     */
    public void upDataCoordinateMain(){
        ApplicationUtil a1 =this.applicationUtil;
        runOnUiThread(new ThreadUI(a1){
            @Override
            public void run() {
                XCoordinateMain = "X:  ";
                YCoordinateMain = "Y:  ";
                ZCoordinateMain = "Z:  ";
                String aX = applicationUtil.getXTotal();
                String aY = applicationUtil.getYTotal();
                String aZ = applicationUtil.getZTotal();
                int dwx = aX.indexOf(",");
                int dwy = aY.indexOf(",");
                int dwz = aZ.indexOf(",");
                String ID = applicationUtil.getID();
                String S = applicationUtil.getS();
                XCoordinateMain = XCoordinateMain + aX.substring(0,dwx) + "圈" + aX.substring(dwx+1) + "步";
                YCoordinateMain = YCoordinateMain + aY.substring(0,dwy) + "圈" + aY.substring(dwy+1) + "步";
                ZCoordinateMain = ZCoordinateMain + aZ.substring(0,dwz) + "圈" + aZ.substring(dwz+1) + "步";
                txtXCoordinateMain.setText(XCoordinateMain);
                txtYCoordinateMain.setText(YCoordinateMain);
                txtZCoordinateMain.setText(ZCoordinateMain);
                txtID.setText("机器序列号："+ID);
                txtInstrument.setText(S);
            }
        });
    }
    @Override
    public void finish() {

        switch (switchActivity){
            case ActivityClient:
                applicationUtil.setZeroIs(0);

                                Thread cThread=new Thread(){//创建子线程
                                    @Override
                                    public void run() {
                                        try{
                                            Intent it=new Intent(getApplicationContext(), ClientActivity.class);//启动MainActivity
                                            startActivity(it);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                cThread.start();//启动线程
                break;
            case ActivityImage:
                                VerifyStoragePermissions.verifyStoragePermissions(this);
                                Thread myThread=new Thread(){//创建子线程
                                    @Override
                                    public void run() {
                                        try{
                                            // sleep(3000);//使程序休眠五秒
                                            Intent it=new Intent(getApplicationContext(),LookImage.class);//启动MainActivity
                                            startActivity(it);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                myThread.start();//启动线程
                break;
            case ActivityFull:////////////////////////////////////////////==
                                if(applicationUtil.getZeroIs() != 0){
                                    switchActivity = ActivityClient;
                                    Toast.makeText(this,"请进行初始化",Toast.LENGTH_SHORT).show();
                                    return;
                                }else {
                                    Thread myThread1=new Thread(){//创建子线程
                                        @Override
                                        public void run() {
                                            try{
                                                // sleep(10000);//使程序休眠五秒
                                                Intent it=new Intent(getApplicationContext(), FullModeActivity.class);//启动MainActivity
                                                startActivity(it);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    myThread1.start();//启动线程
                                }
                break;
            default:break;
        }
        //停止更新图片线程
        stopUpdateImage();
        //停止传感器数据请求
        upDataSensor = false;
        super.finish();
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
