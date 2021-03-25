package com.example.incubatorui.sendMessage;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.incubatorui.ApplicationUtil;
import com.example.incubatorui.MainActivity;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;



public class MyOnClickBegin  implements View.OnClickListener{
    ApplicationUtil applicationUtil;
    Context context;
    Activity activity;
    LoadingDialog loadingDialog;
    public MyOnClickBegin(ApplicationUtil a, Context context, Activity activity){
        this.applicationUtil = a;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        if(applicationUtil.getZeroIs() == 1){
            Toast.makeText(context,"已完成初始化",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!applicationUtil.getSocketIs()){
            Toast.makeText(context,"服务器未连接",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!applicationUtil.getTcpSendIs()){
            Toast.makeText(context,"不允许发送指令",Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog = new LoadingDialog(context)  //显示等待  loadingDialog
                .setLoadingText("响应中...")
                .setSuccessText("指令响应成功")//显示加载成功时的文字
                .setFailedText("指令响应失败")
                .setLoadSpeed(LoadingDialog.Speed.SPEED_TWO)
                .setInterceptBack(true)
                .setShowTime(10);

        loadingDialog.show();
        Thread t = new Thread(){
            @Override
            public void run() {
                if(applicationUtil.getZeroIs() == 0) {//进行初始化
                    //请求仪器状态
                    applicationUtil.sendMessageF("CMInstrumentStatus\r\n");
                    Response rr = new Response(applicationUtil,150);
                    rr.start();
                    while (true){
                        if (rr.getIsGo() == 2){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.loadFailed();
                                }
                            });
                            return;
                        }else {
                            if(rr.getIsGo() == 1){
                                System.out.println("指令响应成功");
                                break;
                            }
                        }
                        try {
                            sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //请求获取ID
                    applicationUtil.sendMessageF("CMInstrumentID\r\n");
                    Response r0 = new Response(applicationUtil,150);
                    r0.start();
                    while (true){
                        if (r0.getIsGo() == 2){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.loadFailed();
                                }
                            });
                            return;
                        }else {
                            if(r0.getIsGo() == 1){
                                System.out.println("指令响应成功");
                                break;
                            }
                        }
                        try {
                            sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //进行所有归零操作
                    //x轴归零
                    applicationUtil.sendMessageF("CMXC\r\n");
                    Response r1 = new Response(applicationUtil, 3000);
                    r1.start();
                    while (true) {
                        if (r1.getIsGo() == 2) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.loadFailed();
                                }
                            });
                            return;
                        } else {
                            if (r1.getIsGo() == 1) {
                                System.out.println("指令响应成功");
                                break;
                            }
                        }
                        try {
                            sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //Y轴
                    applicationUtil.sendMessageF("CMYC\r\n");
                    Response r2 = new Response(applicationUtil, 3000);
                    r2.start();
                    while (true) {
                        if (r2.getIsGo() == 2) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.loadFailed();
                                }
                            });
                            return;
                        } else {
                            if (r2.getIsGo() == 1) {
                                System.out.println("指令响应成功");
                                break;
                            }
                        }
                        try {
                            sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //Z轴
                    applicationUtil.sendMessageF("CMZC\r\n");
                    Response r3 = new Response(applicationUtil, 3000);
                    r3.start();
                    while (true) {
                        if (r3.getIsGo() == 2) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.loadFailed();
                                }
                            });
                            return;
                        } else {
                            if (r3.getIsGo() == 1) {
                                System.out.println("指令响应成功");
                                activity.runOnUiThread(new Runnable() {
                                    MainActivity mainActivity = (MainActivity)activity;
                                    @Override
                                    public void run() {
                                        applicationUtil.setX_int_total(0);
                                        applicationUtil.setY_int_total(0);
                                        applicationUtil.setZ_int_total(0);
                                        applicationUtil.setZeroIs(1);
                                        mainActivity.upDataCoordinateMain();
                                        loadingDialog.loadSuccess();
                                    }
                                });
                                break;
                            }
                        }
                        try {
                            sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //滤光片归零
                    /*applicationUtil.sendMessageF("CMLC\r\n");
                    Response r4 = new Response(applicationUtil, 3000);
                    r4.start();
                    while (true) {
                        if (r4.getIsGo() == 2) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.loadFailed();
                                }
                            });
                            return;
                        } else {
                            if (r4.getIsGo() == 1) {
                                System.out.println("指令响应成功");
                                activity.runOnUiThread(new Runnable() {
                                    MainActivity mainActivity = (MainActivity)activity;
                                    @Override
                                    public void run() {
                                        applicationUtil.setX_int_total(0);
                                        applicationUtil.setY_int_total(0);
                                        applicationUtil.setZ_int_total(0);
                                        applicationUtil.setZeroIs(1);
                                        mainActivity.upDataCoordinateMain();
                                        loadingDialog.loadSuccess();
                                    }
                                });
                                break;
                            }
                        }
                        try {
                            sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                }
            }
        };
        t.start();
    }
}
