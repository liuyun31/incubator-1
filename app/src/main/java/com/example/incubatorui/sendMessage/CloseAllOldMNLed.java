package com.example.incubatorui.sendMessage;

import android.os.Looper;
import android.widget.Toast;

import com.example.incubatorui.ApplicationUtil;

/**
 * 关闭之前区域的所有灯
 */
public class CloseAllOldMNLed extends Thread{
    ApplicationUtil applicationUtil;
    int m;
    int n;
    public CloseAllOldMNLed(ApplicationUtil applicationUtil,int m,int n){
        this.applicationUtil = applicationUtil;
        this.m = m;
        this.n = n;

    }
    @Override
    public void run(){
        //System.out.println("启动关闭线程 m,n=====================================     " +m + n );
        try{
            if(m < 0 || n < 0){
                return;
            }
            if (!applicationUtil.getSocketIs()){
                showToast("服务器未连接");
            }
            if(!applicationUtil.getTcpSendIs()){
                showToast("不允许发送指令，关闭区域LED失败");
            }
            //关闭红色LED
            applicationUtil.sendMessageF("CMSRL"+m+"C"+n+"=0\r\n");//关闭
            Response rled1 = new Response(applicationUtil,150);
            rled1.start();
            while (true){
                if (rled1.getIsGo() == 2){
                    showToast("指令响应失败，关闭区域LED失败");
                    return;
                }else {
                    if(rled1.getIsGo() == 1){
                        System.out.println("指令响应成功" + m + n);
                        applicationUtil.setRgbIndex(m-1,n-1,0,0);
                        break;
                    }
                }
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //关闭绿色led
            applicationUtil.sendMessageF("CMSGL"+m+"C"+n+"=0\r\n");//关闭
            Response rled2 = new Response(applicationUtil,150);
            rled2.start();
            while (true){
                if (rled2.getIsGo() == 2){
                    showToast("指令响应失败，关闭区域LED失败");
                    return;
                }else {
                    if(rled2.getIsGo() == 1){
                        System.out.println("指令响应成功");
                        applicationUtil.setRgbIndex(m-1,n-1,1,0);
                        break;
                    }
                }
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //关闭蓝色led
            applicationUtil.sendMessageF("CMSBL"+m+"C"+n+"=0\r\n");//关闭
            Response rled3 = new Response(applicationUtil,150);
            rled3.start();
            while (true){
                if (rled3.getIsGo() == 2){
                    showToast("指令响应失败，关闭区域LED失败");
                    return;
                }else {
                    if(rled3.getIsGo() == 1){
                        System.out.println("指令响应成功");
                        applicationUtil.setRgbIndex(m-1,n-1,2,0);
                        break;
                    }
                }
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //关闭白色led
            if (n>=6){
                n=5;
            }
            applicationUtil.sendMessageF("CMSWL"+m+"C"+n+"=0\r\n");//关闭
            Response rled4 = new Response(applicationUtil,150);
            rled4.start();
            while (true){
                if (rled4.getIsGo() == 2){
                    showToast("指令响应失败，关闭区域LED失败");
                    return;
                }else {
                    if(rled4.getIsGo() == 1){
                        applicationUtil.setLED((m-1)*5+(n),0);
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
            //关闭紫外1
            if(m >= 4){//第4行没有紫外灯
                m =3;
            }
            applicationUtil.sendMessageF("CMSZL"+m+"C"+n+"=0\r\n");//关闭
            Response rled5 = new Response(applicationUtil,150);
            rled5.start();
            while (true){
                if (rled5.getIsGo() == 2){
                    showToast("指令响应失败，关闭区域LED失败");
                    return;
                }else {
                    if(rled5.getIsGo() == 1){
                        System.out.println("指令响应成功");
                        applicationUtil.setUV1((m-1)*5+n,0);
                        break;
                    }
                }
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //关闭紫外2
            applicationUtil.sendMessageF("CMSKL"+m+"C"+n+"=0\r\n");//关闭
            Response rled6 = new Response(applicationUtil,150);
            rled6.start();
            while (true){
                if (rled6.getIsGo() == 2){
                    showToast("指令响应失败，关闭区域LED失败");
                    return;
                }else {
                    if(rled6.getIsGo() == 1){
                        System.out.println("指令响应成功");
                        applicationUtil.setUV2((m-1)*5+n,0);
                        break;
                    }
                }
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 提示框
     * @param msg
     */
    public void showToast(String msg){
        Looper.prepare();
        Toast.makeText(applicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}
