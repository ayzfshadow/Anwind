package com.saki.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by Saki on 2018/6/24.
 */

public class DownloadButton extends View{
    public interface onDownload{
        public void complet(byte[] data);
    }
    private class DownLoadThread extends Thread{

        private String url;
        private float mProgress = 0;
        private int maxLen = 0;
        private DecimalFormat df;
        public float getProgress(){
            return mProgress;
        }
        public String getProgressFormat(){
            return df.format(mProgress * 100)+"%";
        }
        public DownLoadThread(String url){
            this.url = url;
            df = new DecimalFormat("#.00");
        }
        public void cancelDownload(){
            interrupt();
        }
        @Override
        public void run() {
            postInvalidate();
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.connect();
                maxLen = conn.getContentLength();
                buff = new ByteArrayOutputStream(maxLen);
                load = conn.getInputStream();
                download();
            } catch (IOException e) {
                mState = -1;
                postInvalidate();
            }
        }
        private void download() throws IOException {
            byte[] bin = new byte[0x10000];
            int l = -1;
            while((l = load.read(bin)) != -1) {
                buff.write(bin,0,l);
                mProgress = buff.size()/(float)maxLen;
                if(mProgress > 1){
                    mProgress = 1;
                }
                postInvalidate();
                while(interrupted()){
                    return;
                }
            }
            od.complet(buff.toByteArray());
        }
    }
    private Paint mTextPaint;
    private Paint mPaint;
    private String title;
    private int mState = 0;
    private InputStream load;
    private ByteArrayOutputStream buff;
    private onDownload od;
    private DownLoadThread download;
    private void init(){
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mPaint = new Paint();
        mPaint.setColor(Color.argb(0xFF,0x32,0xCD,0x32));
        title = "下载";
    }
    public void setTitle(String title){
        this.title = title;
    }
    public DownloadButton(Context context) {
        super(context);
        init();
    }

    public DownloadButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DownloadButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void startDownload(String url,onDownload od2){
        this.od = od2;
        if(download == null) {
            download = new DownLoadThread(url);
            download.start();
            mState = 1;
        }else{
            download.cancelDownload();
            download = null;
            mState = 0;
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //填充色
        mPaint.setStyle(Paint.Style.FILL);
        float right = download!=null?getWidth()*download.getProgress():getWidth();
        canvas.drawRect(0,0,right,getHeight(),mPaint);
        //边框
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
        //文字
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float y = getHeight() / 2 + (fm.descent - fm.ascent) / 2 - fm.descent;
        String c = title;
        if(mState == 1)
            c = download.getProgressFormat();
        else if(mState == -1)
            c = "下载失败";
        canvas.drawText(c, getWidth() / 2 - mTextPaint.measureText(c)/2, y, mTextPaint);
    }
}
