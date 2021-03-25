package com.example.incubatorui.sendMessage;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.example.incubatorui.ApplicationUtil;
import com.example.incubatorui.R;
import com.example.incubatorui.SiteSelection;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

public class MyOnClickZero implements View.OnClickListener {
    ApplicationUtil applicationUtil;
    private Activity activity;
    private Context context;
    LoadingDialog loadingDialog;
    int i=0;
    public MyOnClickZero(ApplicationUtil a,Activity activity,Context context){
        this.applicationUtil = a;
        this.activity = activity;
        this.context =  context;
    }
    @Override
    public void onClick(View v) {
        if(!applicationUtil.getTcpSendIs()){
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

        switch (v.getId()){
            case R.id.btn_XZero:applicationUtil.sendMessageF("CMXC\r\n");
            i=1;
                break;
            case R.id.btn_YZero:applicationUtil.sendMessageF("CMYC\r\n");
            i=2;
                break;
            case R.id.btn_ZZero:applicationUtil.sendMessageF("CMZC\r\n");
            i=3;
                break;
            case R.id.btn_filterZero:applicationUtil.sendMessageF("CMLC\r\n");
            i=4;
                break;
            default:break;
        }
        Thread t = new Thread(){
            @Override
            public void run(){
                Response r1 = new Response(applicationUtil,3000);
                r1.start();
                while (true){
                    if (r1.getIsGo() == 2){
                        System.out.println("指令响应失败");
                        //更新UI
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.loadFailed();
                            }
                        });
                        return;
                    }else {
                        if(r1.getIsGo() == 1){
                            System.out.println("指令响应成功");
                            switch (i){
                                case 1:applicationUtil.setX_int_total(0);
                                    break;
                                case 2:applicationUtil.setY_int_total(0);
                                    break;
                                case 3:applicationUtil.setZ_int_total(0);
                                    break;
                                default:break;
                            }
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
