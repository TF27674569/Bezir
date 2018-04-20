package com.cabinet.bezir.view.tree;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.LinkedList;

/**
 * Description : 树枝
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/4/20
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Branch {

    // 树枝的颜色
    private static final int BRANCH_COLOR = Color.rgb(35, 31, 32);

    // 控制点
    Point[] cp = new Point[3];

    int currLen;
    int maxLen;
    float radius;
    float part;

    float growX;
    float growY;

    LinkedList<Branch> childList;

    public Branch(int[] a){
        cp[0] = new Point(a[2], a[3]);
        cp[1] = new Point(a[4], a[5]);
        cp[2] = new Point(a[6], a[7]);
        radius = a[8];
        maxLen = a[9];
        part = 1.0f / maxLen;
    }

    public boolean grow(Canvas canvas, float scareFactor){
        if(currLen <= maxLen){
            bezier(part * currLen);
            draw(canvas, scareFactor);
            currLen++;
            radius *= 0.97f;
            return true;
        }else{
            return false;
        }
    }

    private void draw(Canvas canvas, float scareFactor){
        Paint paint = CommonUtil.getPaint();
        paint.setColor(BRANCH_COLOR);

        canvas.save();
        canvas.scale(scareFactor, scareFactor);
        canvas.translate(growX, growY);
        canvas.drawCircle(0,0, radius, paint);
        canvas.restore();
    }

    private void bezier(float t) {
        float c0 = (1 - t) * (1 - t);
        float c1 = 2 * t * (1 - t);
        float c2 = t * t;
        growX =  c0 * cp[0].x + c1 * cp[1].x + c2* cp[2].x;
        growY =  c0 * cp[0].y + c1 * cp[1].y + c2* cp[2].y;
    }

    public void addChild(Branch branch){
        if(childList == null){
            childList = new LinkedList<>();
        }
        childList.add(branch);
    }


}
