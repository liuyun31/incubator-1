package com.example.incubatorui.lightControl;

import android.view.View;

public class MyOnClickRGB implements View.OnClickListener {
    int mRow;//行
    int nList;//列
    int[][][] rgb = new int[4][6][3];
    public MyOnClickRGB(){
        //初始化
        for(int i=0;i<4;i++){
            for(int j=0;j<6;j++){
                for(int k=0;k<3;k++){
                    rgb[i][j][k] = 0;
                }
            }
        }

    }
    public void setMN(int m,int n ){
        this.mRow = m;
        this.nList = n;
    }

    public int getmRow() {
        return mRow;
    }

    public int getnList() {
        return nList;
    }

    public int[][][] getRgb() {
        return rgb;
    }
    public void setRgb(int[][][] rgb) {
        this.rgb = rgb;
    }

    /**
     * 更改某个灯的状态
     * @param m 行
     * @param n 列
     * @param num 第几个
     * @param is 亮还是灭
     */
    public void setNumLight(int m,int n,int num,int is){
        rgb[m][n][num] = is;
    }

    @Override
    public void onClick(View v) {

    }
}
