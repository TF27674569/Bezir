package com.cabinet.bezir.view.tree;

import android.graphics.Paint;

import java.util.Random;


/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/4/20
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class CommonUtil {

    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final Paint PAINT = new Paint();

    /**
     * 获取范围[0, n]的随机数
     * @param
     * @return
     */
    public static int random(int n){
        return RANDOM.nextInt(n+1);
    }

    /**
     * 获取范围[m, n]的整数，
     * 要求 m < n
     * @return
     */
    public static int random(int m, int n){
        int d = n - m;
        return m + RANDOM.nextInt(d+1);
    }

    /**
     * 获取范围[m, n]的浮点数，
     * 要求 m < n
     * @return
     */
    public static float random(float m , float n){
        float d = n - m;
        return m + RANDOM.nextFloat() * d;
    }

    public static Paint getPaint(){
        return PAINT;
    }

}
