package com.bignerdranch.android.JournalPrime;

import android.graphics.BitmapFactory;

/**
 * Created by ad939564 on 4/16/2018.
 */

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){
        //Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float 
    }
}
