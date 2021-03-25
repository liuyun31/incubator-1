package com.example.incubatorui.imageCrop.util;

import android.content.Context;

import androidx.annotation.Nullable;

/**
 * Created by mac on 17/12/21.
 *
 */

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */

public class UIUtil {
    public static int dip2px(@Nullable Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
