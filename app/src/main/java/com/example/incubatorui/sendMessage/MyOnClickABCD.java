package com.example.incubatorui.sendMessage;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.example.doodle.dialog.DialogController;
import com.example.incubatorui.ApplicationUtil;
import com.example.incubatorui.R;
import com.example.incubatorui.SiteSelection;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

public class MyOnClickABCD implements View.OnClickListener {
    ApplicationUtil applicationUtil;
    int id;
    private Activity activity;
    private  Context context;
    LoadingDialog loadingDialog;

    public MyOnClickABCD(ApplicationUtil a,int id,Activity activity,Context context){
        this.applicationUtil = a;
        this.id = id;
        this.activity = activity;
        this.context =  context;
    }



    @Override
    public void onClick(View v) {
        System.out.println("按下！！！！！！！！");
        loadingDialog = new LoadingDialog(context)
                .setLoadingText("响应中...")
                .setSuccessText("指令响应成功")//显示加载成功时的文字
                .setFailedText("指令响应失败")
                .setLoadSpeed(LoadingDialog.Speed.SPEED_TWO)
                .setInterceptBack(true)
                .setShowTime(10);


        DialogController.showMsgDialog(activity, "确定移动", null, "取消",
                "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loadingDialog.show();
                        Thread thread = new Thread(){
                            @Override
                            public void run() {

                                //解析当前位置
                                String X = applicationUtil.getXTotal();
                                String Y = applicationUtil.getYTotal();
                                int Xd = X.indexOf(",");
                                int Yd = Y.indexOf(",");
                                int x = Integer.parseInt(X.substring(0,Xd)) * 6400 + Integer.parseInt(X.substring(Xd+1));
                                int y = Integer.parseInt(Y.substring(0,Yd)) * 1536 + Integer.parseInt(Y.substring(Yd+1));

                                //计算需要走的步数
                                int xb ;
                                int yb ;
                                //记录x正走反走 1是正走，0反之
                                int xf = 1;
                                int yf = 1;
                                //终点位置
                                int xz = 0;
                                int yz = 0;

                                if(applicationUtil.getTcpSendIs()){
                                    switch (id){
                                        case R.id.a1 :  xz =5001 ; yz = 44461;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.a2 : xz = 8142; yz = 44461;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.a3 : xz = 11284; yz = 44461;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.a4 : xz = 14426; yz = 44461;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.a5 : xz = 17567; yz = 44461;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.a6 : xz = 20709; yz = 44461;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.b1 : xz =5001 ; yz = 32978;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.b2 : xz = 8142; yz = 32978;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.b3 : xz = 11284; yz = 32978;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.b4 : xz = 14426; yz = 32978;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.b5 : xz = 17567; yz = 32978;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.b6 : xz = 20709; yz = 32978;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.c1 : xz = 5001; yz = 21496;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.c2 : xz = 8142; yz = 21496;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.c3 : xz = 11284; yz = 21496;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.c4 : xz = 11426; yz = 21496;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.c5 : xz = 17567; yz = 21496;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.c6 : xz = 20709; yz = 21496;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.d1 : xz = 5001; yz = 10013;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.d2 : xz = 8142; yz = 10013;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.d3 : xz = 11284; yz = 10013;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.d4 : xz = 14426; yz = 10013;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.d5 : xz = 17567; yz = 10013;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }break;
                                        case R.id.d6 : xz = 20709; yz = 10013;
                                            if(xz - x>0){
                                                xb = xz - x;
                                                xf = 1;
                                            }else {
                                                xb = x-xz;
                                                xf = 0;
                                            }
                                            if(yz - y>0){
                                                yb = yz - y;
                                                yf = 1;
                                            }else {
                                                yb = y - yz;
                                                yf = 0;
                                            }
                                            if(xb !=0 && yb != 0){
                                                if(xf == 1 && yf ==1){
                                                    String m = "CMXZYZ="+xb+","+yb+"\r\n";
                                                    applicationUtil.sendMessageF(m);
                                                }else {
                                                    if(xf == 1 && yf==0){
                                                        String m = "CMXZYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                    if(xf == 0 && yf==1){
                                                        String m = "CMXFYZ="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }if(xf == 0 && yf==0){
                                                        String m = "CMXFYF="+xb+","+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }else {
                                                if(xb == 0 && yb != 0){
                                                    if(yf == 1){
                                                        String m = "CMM3Z="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM3F="+yb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                                if(yb == 0 && xb != 0){
                                                    if(xf == 1){
                                                        String m = "CMM1Z="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }else {
                                                        String m = "CMM1F="+xb+"\r\n";
                                                        applicationUtil.sendMessageF(m);
                                                    }
                                                }
                                            }
                                            break;
                                        default:break;
                                    }
                                    //响应判断
                                    Response r = new Response(applicationUtil,3000);
                                    r.start();
                                    while (true){
                                        if (r.getIsGo() == 2){
                                            System.out.println("指令响应失败");
                                            //更新UI
                                            activity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    loadingDialog.loadFailed();
                                                }
                                            });
                                            break;
                                        }else {
                                            if(r.getIsGo() == 1){
                                                System.out.println("指令响应成功");
                                                applicationUtil.setX_Y_int_total(xz,yz);
                                                //更新UI坐标
                                                SiteSelection s = (SiteSelection)activity;
                                                s.upDataCoordinate();
                                                //更新UI
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loadingDialog.loadSuccess();
                                                    }
                                                });
                                                break;
                                            }
                                        }
                                        try {
                                            sleep(10);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                // 没有连接到服务器，显示操作失败
                                else {
                                    //更新UI
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadingDialog.loadFailed();
                                        }
                                    });
                                }
                            }
                        };
                        thread.start();

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("nononoono");
                    }
                });
    }
}
