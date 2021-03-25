package com.example.incubatorui.utils.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTime {
    /**
     * 获取10位时间戳
     * @return
     */
    public static String getTime10(){
        long time = System.currentTimeMillis()/1000;
        String s = String.valueOf(time);
        return s;
    }
    /**
     * 获取13位时间戳
     * @return
     */
    public static String getTime13(){
        long time = System.currentTimeMillis();
        String s = String.valueOf(time);
        return s;
    }

    /**
     * 获取当前时间，加上时间戳后三位，命名图片
     * @return
     */
    public String getTimeYYYYMMDD(){
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String times = sdf.format(new Date(System.currentTimeMillis()));
        times = times + String.valueOf(time%1000);
        if(times.length() < 17){
            times = times + "0";
        }
        return times;
    }
}
