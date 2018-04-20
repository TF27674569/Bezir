package com.cabinet.bezir;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabinet.bezir.utils.ScreenUtils;
import com.cabinet.bezir.view.TreeView;
import com.plattysoft.leonids.ParticleSystem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements View.OnClickListener, TreeView.IBloomFall {

    private TreeView treeView;
    private ImageView imageAnima;
    private ImageView ivShadow;
    private AnimatorSet animatorSet;
    private Handler mHandler = new Handler();
    private LrcRead mLrcRead = new LrcRead();
    private  TextView lrcTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initAnima();
        initListener();

    }

    private void initView() {
        treeView = findViewById(R.id.treeView);
        imageAnima = findViewById(R.id.image_anima);
        ivShadow = findViewById(R.id.ivShadow);

        lrcTextView = new TextView(this);
        lrcTextView.setTextSize(22);
//        lrcTextView.setGravity(Gravity.CENTER);
        lrcTextView.setTextColor(Color.parseColor("#f02635"));

        treeView.setBloomFall(this);
        mLrcRead.Read(getResources().openRawResource(R.raw.jsai));
    }

    private void initAnima() {
        //  心的上下移动动画
        ObjectAnimator translatAnimator = ObjectAnimator.ofFloat(imageAnima, "translationY", -100f, 0f, -100f);
        translatAnimator.setRepeatCount(-1);
        // 阴影缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivShadow, "scaleX", 0.7f, 1f, 0.7f);
        scaleX.setRepeatCount(-1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivShadow, "scaleY", 0.7f, 1f, 0.7f);
        scaleY.setRepeatCount(-1);

        // 动画集
        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        // 同时执行
        animatorSet.playTogether(translatAnimator, scaleX, scaleY);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }


    private void initListener() {
        imageAnima.setOnClickListener(this);
    }

    MediaPlayer player;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_anima:
                v.setEnabled(false);
                startLrc();
                player = MediaPlayer.create(MainActivity.this, R.raw.jiushiaini);
                player.start();
                particleBezierAnima();
                break;
        }
    }

    /**
     * 下落动画
     */
    private void particleBezierAnima() {
        animatorSet.cancel();
        ivShadow.setVisibility(View.GONE);

        // 获取在屏木中的位置
        int[] location = new int[2];
        imageAnima.getLocationOnScreen(location);

        // 爆炸心形效果
        new ParticleSystem(this, 1000, R.mipmap.heart3, 3000)
                .setSpeedRange(0.2f, 0.5f)
                .setRotationSpeed(20)
                .oneShot(imageAnima, 200);

        PointF startPointF = new PointF(location[0], location[1]);
        int x = ScreenUtils.getScreenWidth(this) / 2 - 100;
        int y = ScreenUtils.getScreenHeight(this);
        PointF endPointF = new PointF(x, y);

        // 同时执行贝赛尔移动动画
        ValueAnimator animator = ValueAnimator.ofObject(new Bezier(startPointF, endPointF), startPointF, endPointF);
        animator.setStartDelay(2500);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF value = (PointF) animation.getAnimatedValue();
                imageAnima.setX(value.x);
                imageAnima.setY(value.y);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                imageAnima.setVisibility(View.GONE);
                treeView.setVisibility(View.VISIBLE);
                // 执行心形树
                treeView.start();
            }
        });
        animator.start();

    }

    @Override
    public void bloomFall() {
        // TODO 落叶回调 添加文本

    }


    public class Bezier implements TypeEvaluator<PointF> {

        // 由于这里使用的是三阶的贝塞儿曲线, 所以我们要定义两个控制点
        private PointF ctrlPointF1;
        private PointF ctrlPointF2;

        public Bezier(PointF startPointF, PointF endPointF) {
            float dy = (startPointF.y + endPointF.y) / 2;
            ctrlPointF1 = new PointF(startPointF.x - 150, dy - 100);
            ctrlPointF2 = new PointF(startPointF.x + 150, dy + 100);


        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            // 这里运用了三阶贝塞儿曲线的公式, 请自行上网查阅
            float leftTime = 1.0f - fraction;
            PointF resultPointF = new PointF();

            // 三阶贝塞儿曲线
            resultPointF.x = (float) Math.pow(leftTime, 3) * startValue.x
                    + 3 * (float) Math.pow(leftTime, 2) * fraction * ctrlPointF1.x
                    + 3 * leftTime * (float) Math.pow(fraction, 2) * ctrlPointF2.x
                    + (float) Math.pow(fraction, 3) * endValue.x;
            resultPointF.y = (float) Math.pow(leftTime, 3) * startValue.y
                    + 3 * (float) Math.pow(leftTime, 2) * fraction * ctrlPointF1.y
                    + 3 * leftTime * fraction * fraction * ctrlPointF2.y
                    + (float) Math.pow(fraction, 3) * endValue.y;
            return resultPointF;
        }
    }


    /**
     * 播放音乐
     */
    int position = 0;
    long time = 0;
    public void startLrc(){
        final LyricContent lyricContent = mLrcRead.GetLyricContent().get(position);
        if (position == mLrcRead.GetLyricContent().size()-1) return;
        Log.e("TAG", "time -> "+time);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "run: "+lyricContent.data );

                onDataByView(lyricContent.data );
                position++;
                // 后面一个减去前面一个的时间
                time = mLrcRead.GetLyricContent().get(position).time- lyricContent.time;
                startLrc();
            }
        },time);
    }

    /**
     * 歌词
     */
    private void onDataByView(final String lrcString) {
        if (position == 0){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onData(lrcString);
                }
            },7500);
        }else {
            onData(lrcString);
        }
    }

    /**
     * 显示歌词
     */
    private void onData(String lrcString) {

        int strHeightCount = countInnerStr(lrcString, " ");

        lrcString = lrcString.replaceAll(" ","\n");
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeView(lrcTextView);
        lrcTextView.setText(lrcString);
//        float textWidth = lrcTextView.getPaint().measureText(lrcString);
//        int screenWidth = ScreenUtils.getScreenWidth(this);
//        float x =((screenWidth/2-textWidth)/2);
        lrcTextView.setX(30);

        float height = getTextHeight()*strHeightCount;
        float y = ScreenUtils.getScreenHeight(this)/2 - height/2 - 30;

        lrcTextView.setY(y);
        viewGroup.addView(lrcTextView);
    }

    public float getTextHeight(){
        Paint.FontMetrics fontMetrics = lrcTextView.getPaint().getFontMetrics();
       return fontMetrics.bottom - fontMetrics.top;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    public static int countInnerStr(final String str, final String patternStr) {
        int count = 0;
        final Pattern r = Pattern.compile(patternStr);
        final Matcher m = r.matcher(str);
        while (m.find()) {
            count++;
        }
        return count;
    }

}
