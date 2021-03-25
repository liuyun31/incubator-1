package com.example.incubatorui.BigImageUilts;

import android.view.View;

public class MyOnClickListener implements View.OnClickListener {
    String path;

    /**
     * 保存该图片事件的地址值
     * @param path 事件图片地址值
     */
    public MyOnClickListener(String path){
        this.path = path;
    }

    /**
     * 获取地址值
     * @return 图片地址值
     */
    public String getPath() {
        return path;
    }

    @Override
    public void onClick(View v) {

    }
}
