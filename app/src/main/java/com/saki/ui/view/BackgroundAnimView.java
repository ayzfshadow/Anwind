package com.saki.ui.view;

import java.util.Random;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class BackgroundAnimView extends SurfaceView implements Callback,
        Runnable {
    class O {
        float x;
        float y;
        float speed;
        float r;

        public void draw(Canvas c) {
            y -= speed;
            c.drawCircle(x, y, r, p);
            if (y < -2 * r) {
                reset();
            }
        }

        public O reset() {
            x = random.nextInt(getWidth());
            y = random.nextInt(getHeight()) + getHeight();
            speed = random.nextFloat() + 1;
            r = random.nextInt(4) + 2;
            return this;
        }
    }

    Random random = new Random();
    SurfaceHolder holder;
    Paint bp = new Paint();
    Paint p = new Paint();
    O[] os = new O[100];

    public BackgroundAnimView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    public BackgroundAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    public BackgroundAnimView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        holder = getHolder();
        holder.addCallback(this);
    }

    private void init() {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setMaskFilter(new BlurMaskFilter(1, Blur.NORMAL));
        bp.setStyle(Paint.Style.FILL);
        bp.setAntiAlias(true);
        for (int i = 0; i < os.length; i++) {
            os[i] = new O().reset();
        }
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            Canvas c = holder.lockCanvas();
            if (c != null) {
                LinearGradient g = new LinearGradient(0, getHeight(), 0, 0,
                        Color.argb(0xff, 0x6c, 0xa6, 0xcd), Color.argb(0xff, 0x7a,
                        0xdf, 0xb8), LinearGradient.TileMode.CLAMP);
                bp.setShader(g);
                c.drawRect(0, 0, getWidth(), getHeight(), bp);
                for (int i = 0; i < os.length; i++) {
                    os[i].draw(c);
                }
                holder.unlockCanvasAndPost(c);
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        init();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

}
