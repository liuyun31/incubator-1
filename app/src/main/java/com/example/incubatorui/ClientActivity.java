package com.example.incubatorui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.incubatorui.utils.ImageUtils.VerifyStoragePermissions;

public class ClientActivity extends AppCompatActivity {

    private TextView textIp;//ip地址输入框
    private TextView textPort;//端口输入端
    private Boolean socketIs=false;//判断socket连接是否成功
    public TextView prompt;//提示
    ApplicationUtil appUtil;//获取socket连接
    public ImageView icon;
    String ip ;
    String port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_client);
        VerifyStoragePermissions.verifyStoragePermissions(this);
        //绑定控件对象
        textIp = findViewById(R.id.IPEdit);
        textPort = findViewById(R.id.portEdit);
        prompt = findViewById(R.id.prompt);
        icon = findViewById(R.id.imgIcon);
        //设置图片圆角角度
        RoundedCorners roundedCorners= new RoundedCorners(500);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options=RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(this)
                .applyDefaultRequestOptions(
                        new RequestOptions()
                                .format(DecodeFormat.PREFER_ARGB_8888))//改变图片输出编码，原来是rgb565
                     .load(R.drawable.logo).apply(options).into(icon);
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("DataIPAndPort",
                    Activity.MODE_PRIVATE);
            String IPp = sharedPreferences.getString("textIp", "");
            String ProtT = sharedPreferences.getString("textPort", "");
            if(IPp !=null && !IPp.equals("")){
                textIp.setText(IPp);
            }
            if(ProtT != null && !ProtT.equals("")){
                textPort.setText(ProtT);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void link(View view) {

        //获取socket连接对象
        appUtil =  (ApplicationUtil) this.getApplication();
        //获取用户输入的值
        ip = textIp.getText().toString();
        port = textPort.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("DataIPAndPort",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("textIp", textIp.getText().toString());
        editor.putString("textPort", textPort.getText().toString());
        editor.commit();
        try {
            //获取socket连接
            appUtil.init(ip, port);
            Thread threadTime = new Thread(){
                @Override
                public void run() {
                    try {
                        //延时等待连接状态
                        sleep(1000);
                        //获取连接状态
                        socketIs = appUtil.getSocketIs();
                        System.out.println("状态：" + socketIs);
                        //判断连接状态
                        if(socketIs){
                            //跳转Activity
                            Intent it=new Intent();
                            it.setClass(getApplicationContext(),MainActivity.class);//启动MainActivity
                            startActivity(it);
                            finish();//关闭当前活动
                        }else {
                            //用户提示
                            prompt.setText("连接失败！");
                            sleep(700);
                            prompt.setText("");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            threadTime.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Skip(View view) {
        //跳转Activity
        Intent it=new Intent();
        it.setClass(getApplicationContext(),MainActivity.class);//启动MainActivity
        startActivity(it);
        finish();//关闭当前活动
    }
}
