package com.example.incubatorui.sendMessage;

import com.example.incubatorui.ApplicationUtil;

public class Response extends Thread {
    ApplicationUtil applicationUtil;
    int for_int_j=0;
    int isGo=0;

    public Response(ApplicationUtil a,int j){
        this.applicationUtil = a;
        this.for_int_j = j;
    }
    public int getIsGo(){
        return isGo;
    }
    @Override
    public void run() {
        System.out.println("等待线程*****当前isGo" + isGo);
        int i;
        for (i = 0; i < for_int_j; i++) {
            try {
                sleep(20);
                if (applicationUtil.getTcpSendIs()) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //响应结果1成功，2失败；
        if(i >=for_int_j){
            isGo = 2;
        }else {
            isGo = 1;
        }
        System.out.println("线程结束*****当前isGo" + isGo);
    }
}
