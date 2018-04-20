package com.cabinet.bezir.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cabinet.bezir.view.tree.Tree;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/4/20
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class TreeView extends View{

    private Tree mTree;
    private boolean mIsDraw;
    private IBloomFall bloomFall;

    public TreeView(Context context) {
        this(context,null);
    }


    public TreeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TreeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width  = MeasureSpec.getSize(widthMeasureSpec);
        int height  = MeasureSpec.getSize(heightMeasureSpec);
        if (mTree == null){
            mTree = new Tree(width,height);
            mTree.setBloomFall(bloomFall);
        }
    }


    public void start(){
        mIsDraw = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsDraw){
            mTree.draw(canvas);

            postInvalidate();
        }
    }

    public void setBloomFall(IBloomFall bloomFall){
        this.bloomFall = bloomFall;
        if (mTree!=null){
            mTree.setBloomFall(bloomFall);
        }
    }


    public interface IBloomFall{
        void bloomFall();
    }
}
