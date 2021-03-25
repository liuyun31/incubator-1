package com.example.incubatorui.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

public class Printscreen {
    private Bitmap bitmap;//生成的位图
    /**
     * 获取屏幕截图
     * @param activity
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public Bitmap getBitmap(Activity activity, int x, int y, int width, int height){

        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();

        // 截图位置 生成的 bitmap
        bitmap = Bitmap.createBitmap(bitmap, x, y, width, height);


        view.setDrawingCacheEnabled(false);

        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
