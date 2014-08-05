package com.hugelol.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public abstract class ImageUtils {   
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap){
        int height = (bitmap.getHeight() > 2048) ? 2048 : bitmap.getHeight();
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inDither=false;                    
        opts.inSampleSize = 8;                   
        opts.inPurgeable=true;                 
        opts.inInputShareable=true;             
        opts.inTempStorage=new byte[16 * 1024];
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, height, true);
        if(bitmap != scaled){
            bitmap.recycle();
        }
        return new BitmapDrawable(context.getResources(),scaled);
    }
    
    public static Drawable byteArrayToDrawable(Context context, byte[] bytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        if(height > 2048 && width > 2048){
            bitmap = Bitmap.createScaledBitmap(bitmap, 2048, 2048, true);
        }else if(height > 2048){
            bitmap = Bitmap.createScaledBitmap(bitmap, width, 2048, true);
        }else if(width > 2048){
            bitmap = Bitmap.createScaledBitmap(bitmap, 2048, height, true);
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
