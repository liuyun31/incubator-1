package com.example.incubatorui.sendMessage;

import android.os.Looper;
import android.widget.Toast;

import com.example.incubatorui.ApplicationUtil;

public class SensorDataRequest extends Thread{
    ApplicationUtil applicationUtil;
    boolean isOK = false;
    int num =-1;//完成个数
    public SensorDataRequest(ApplicationUtil a ){
        this.applicationUtil = a;
    }

    public int getNum() {
        return num;
    }

    public boolean isOK() {
        return isOK;
    }

    @Override
    public void run() {
        if(!applicationUtil.getTcpSendIs()){
            isOK = true;
        }else {
            num = 0;
            try {
                applicationUtil.sendMessageF("CMSCO2\r\n");
                Response r1 = new Response(applicationUtil,150);
                r1.start();
                while (true){
                    if (r1.getIsGo() == 2){
                        showToast("指令响应失败");
                        System.out.println("指令响应失败");
                        //提前退出
                        isOK = true;
                        return;
                    }else {
                        if(r1.getIsGo() == 1){
                            System.out.println("指令响应成功");
                            num++;
                            break;
                        }
                    }
                    sleep(20);
                }
                applicationUtil.sendMessageF("CMST1\r\n");
                Response r2 = new Response(applicationUtil,150);
                r2.start();
                while (true){
                    if (r2.getIsGo() == 2){
                        showToast("指令响应失败");
                        System.out.println("指令响应失败");
                        isOK = true;
                        return;
                    }else {
                        if(r2.getIsGo() == 1){
                            System.out.println("指令响应成功");
                            num++;
                            break;
                        }
                    }
                    sleep(20);
                }
                applicationUtil.sendMessageF("CMST2\r\n");
                Response r3 = new Response(applicationUtil,150);
                r3.start();
                while (true){
                    if (r3.getIsGo() == 2){
                        showToast("指令响应失败");
                        System.out.println("指令响应失败");
                        isOK = true;
                        return;
                    }else {
                        if(r3.getIsGo() == 1){
                            System.out.println("指令响应成功");
                            num++;
                            break;
                        }
                    }
                    sleep(20);
                }
                applicationUtil.sendMessageF("CMSH\r\n");
                Response r4 = new Response(applicationUtil,150);
                r4.start();
                while (true){
                    if (r4.getIsGo() == 2){
                        showToast("指令响应失败");
                        System.out.println("指令响应失败");
                        isOK = true;
                        return;
                    }else {
                        if(r4.getIsGo() == 1){
                            System.out.println("指令响应成功");
                            num++;
                            break;
                        }
                    }
                    sleep(20);
                }
                applicationUtil.sendMessageF("CMSP\r\n");
                Response r5 = new Response(applicationUtil,150);
                r5.start();
                while (true){
                    if (r5.getIsGo() == 2){
                        showToast("指令响应失败");
                        System.out.println("指令响应失败");
                        isOK = true;
                        return;
                    }else {
                        if(r5.getIsGo() == 1){
                            System.out.println("指令响应成功");
                            num++;
                            break;
                        }
                    }
                    sleep(20);
                }
                isOK = true;
            }catch (InterruptedException e){
                e.printStackTrace();
            }
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
