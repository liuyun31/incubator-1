package com.example.incubatorui.sendMessage;

import android.view.MotionEvent;
import android.view.View;

import com.example.incubatorui.ApplicationUtil;
import com.example.incubatorui.R;

public class MyOnTouchListener implements View.OnTouchListener  {
    ApplicationUtil applicationUtil;
    int id;
    boolean send = false;
    ThreadSend t;
    int num=0;
    int bushu=1;
    public MyOnTouchListener(ApplicationUtil a, int x){
        this.applicationUtil = a;
        this.id = x;
    }
    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) { // 按下
            bushu = applicationUtil.getMultiple();
            send = true;
            t = new ThreadSend();
            t.start();
        } else if (action == MotionEvent.ACTION_UP) { // 松开
            send = false;
        }
        return false;
    }

    class ThreadSend extends Thread{
        int i=0;
        @Override
        public void run() {
            while (send){
                //防止抢掉，关闭上个区域灯光的线程
                if(!applicationUtil.getTcpSendIs()){
                    return;
                }
                switch (id){
                    case R.id.btnArrowKeyRight:
                        if(bushu==1){
                            applicationUtil.sendMessageF("CMM1ZS\r\n");
                        }else {
                            applicationUtil.sendMessageF("CMM1Z="+bushu+"\r\n");
                        }
                    i=1;
                        break;
                    case R.id.btnArrowKeyLeft:
                        if(bushu == 1){
                            applicationUtil.sendMessageF("CMM1FS\r\n");
                        }else {
                            applicationUtil.sendMessageF("CMM1F="+bushu+"\r\n");
                        }
                    i=2;
                        break;
                    case R.id.btnArrowKeyTop:
                        if(bushu == 1){
                            applicationUtil.sendMessageF("CMM3ZS\r\n");
                        }else {
                            applicationUtil.sendMessageF("CMM3Z="+bushu+"\r\n");
                        }
                    i=3;
                        break;
                    case R.id.btnArrowKeyBottom:
                        if(bushu==1){
                            applicationUtil.sendMessageF("CMM3FS\r\n");
                        }else {
                            applicationUtil.sendMessageF("CMM3F="+bushu+"\r\n");
                        }
                    i=4;
                        break;
                    case R.id.btnFocalTop:
                        if(bushu == 1){
                            applicationUtil.sendMessageF("CMM2ZS\r\n");
                        }else {
                            applicationUtil.sendMessageF("CMM2Z="+bushu+"\r\n");
                        }
                    i=5;
                        break;
                    case R.id.btnFocalBottom:
                        if(bushu ==1 ){
                            applicationUtil.sendMessageF("CMM2FS\r\n");
                        }else {
                            applicationUtil.sendMessageF("CMM2F="+bushu+"\r\n");
                        }
                    i=6;
                        break;
                    case R.id.btn_filter_Top:
                        applicationUtil.sendMessageF("CMM4Z="+bushu+"\r\n");
                        break;
                    case R.id.btn_filter_Bottom:
                        applicationUtil.sendMessageF("CMM4F="+bushu+"\r\n");
                        break;
                    default:break;
                }
                try {
                    if(num == 0){
                        sleep(700);
                        Response r = new Response(applicationUtil,3000);
                        r.start();
                        while (true){
                            if (r.getIsGo() == 2){
                                System.out.println("指令响应失败");
                                send = false;
                                break;
                            }else {

                                if(r.getIsGo() == 1){
                                    switch (i){
                                        case 1:applicationUtil.setX_int_total(applicationUtil.getX_int_total() + bushu );
                                            break;
                                        case 2:applicationUtil.setX_int_total(applicationUtil.getX_int_total() - bushu );
                                            break;
                                        case 3:applicationUtil.setY_int_total(applicationUtil.getY_int_total() + bushu);
                                            break;
                                        case 4:applicationUtil.setY_int_total(applicationUtil.getY_int_total() - bushu);
                                            break;
                                        case 5:applicationUtil.setZ_int_total(applicationUtil.getZ_int_total() + bushu);
                                            break;
                                        case 6:applicationUtil.setZ_int_total(applicationUtil.getZ_int_total() - bushu);
                                            break;
                                        default:break;
                                    }
                                    sleep(60);
                                    System.out.println("指令响应成功");
                                    break;
                                }
                            }
                            sleep(10);
                        }
                        num++;
                    }else {
                        sleep(180);
                        Response r = new Response(applicationUtil,3000);
                        r.start();
                        while (true){
                            if (r.getIsGo() == 2){
//                            Toast.makeText(applicationUtil.getFullModeActivity(),"指令发送失败请检查设备",Toast.LENGTH_SHORT);
                                System.out.println("指令响应失败");
                                send = false;
                                break;
                            }else {
                                if(r.getIsGo() == 1){
                                    switch (i){
                                        case 1:applicationUtil.setX_int_total(applicationUtil.getX_int_total() + bushu );
                                            break;
                                        case 2:applicationUtil.setX_int_total(applicationUtil.getX_int_total() - bushu );
                                            break;
                                        case 3:applicationUtil.setY_int_total(applicationUtil.getY_int_total() + bushu);
                                            break;
                                        case 4:applicationUtil.setY_int_total(applicationUtil.getY_int_total() - bushu);
                                            break;
                                        case 5:applicationUtil.setZ_int_total(applicationUtil.getZ_int_total() + bushu);
                                            break;
                                        case 6:applicationUtil.setZ_int_total(applicationUtil.getZ_int_total() - bushu);
                                            break;
                                        default:break;
                                    }
                                    sleep(60);

                                    System.out.println("指令响应成功");
                                    break;
                                }
                            }
                            sleep(20);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
