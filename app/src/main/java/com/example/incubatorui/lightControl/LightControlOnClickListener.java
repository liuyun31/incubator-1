package com.example.incubatorui.lightControl;

import android.view.View;

public class LightControlOnClickListener implements View.OnClickListener {
    int mRow;//行
    int nList;//列
    public LightControlOnClickListener(int m,int n){
        this.mRow = m;
        this.nList = n;
    }


    public int getmRow() {
        return mRow;
    }

    public int getnList() {
        return nList;
    }

    @Override
    public void onClick(View v) {

    }
}
