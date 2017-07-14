package com.transsion.core.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 回收图片资源
 *
 * @author peng.sun
 * @data 2017/06/29
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ViewBitmapRecycleUtil {


    /**
     * 回收view或viewgroup中的子image的bitmap
     *
     * @param contentView
     */

    public static void bitmapsRecycle(View contentView) {
        if (contentView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) contentView).getChildCount(); i++) {
                View view = ((ViewGroup) contentView).getChildAt(i);
                bitmapsRecycle(view);
            }
        } else if (contentView instanceof ImageView) {
            bitmapRecycle((ImageView) contentView);
        }
    }

//================================================================================================ 以下是辅助方法

    /**
     * 回收imageview中的bitmap
     *
     * @param imageView
     */
    private static void bitmapRecycle(ImageView imageView) {
        try {
            if (imageView.getDrawable() instanceof BitmapDrawable) {
                BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
                if (bd != null) {
                    Bitmap bitmap = bd.getBitmap();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
            }
            imageView.setImageBitmap(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
