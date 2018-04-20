package com.cabinet.bezir.view.tree;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/4/20
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Snapshot {
    Canvas canvas;
    Bitmap bitmap;
    public Snapshot(Bitmap bitmap){
        this.bitmap = bitmap;
        this.canvas = new Canvas(bitmap);
    }
}
