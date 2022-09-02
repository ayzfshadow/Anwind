package com.saki.ui.view;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.saki.tools.HttpUtil;
import com.saki.tools.InstallUtil;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

public class UpdateView extends View implements OnClickListener {

    public static final int STROKE = Color.argb(0xff, 0xf0, 0xf0, 0xf0);
    public static final int START = Color.argb(0xff, 0x7a, 0xdf, 0xb8);
    public static final int END = Color.WHITE;//Color.argb(0xff, 0x6c, 0xa6, 0xcd);
    private Paint paint;
    private RectF rect;
    private int maxPoint = 100;
    private int point = 100;
    private String downUrl = "";
    private String info;
    private String msg = "检查更新中...";
    private boolean isUpdate = false;
    private boolean isInstall = false;
    private boolean isDown = false;
    private int textColor = Color.argb(0xff, 0xff, 0x8c, 0x00);
    private int code;
    File f = new File(Environment.getExternalStorageDirectory()+ "/SQ/update/temp.apk");

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                invalidate();
            } else if (msg.what == 2) {
                UpdateView.this.msg = (String) msg.obj;
                invalidate();
            }
        }

        ;
    };

    public int getCode(){
        return code;
    }

    public UpdateView(Activity context) {
        super(context);
        init();
    }

    public UpdateView(Context context, AttributeSet attr) {
        super(context, attr);
        init();

    }

    public UpdateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        rect = new RectF();
        rect.top = 0;
        rect.left = 0;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        setOnClickListener(this);
        Context c = getContext();
        try {
            code = c.getPackageManager().getPackageInfo(c.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void checkUpdate(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = new String(HttpUtil.getUrlContent(url));
                try {
                    JSONObject obj = new JSONObject(json);
                    waitUpdateAction(obj.getInt("versionCode"),
                            obj.optString("info"), obj.optString("down"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    h.sendMessage(Message.obtain(h, 2, "更新检测失败"));
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if (isInstall) {
            InstallUtil.installAPK(getContext(), f);
            return;
        }
        if (isDown) {
            isDown = false;
            f.delete();
            h.sendMessage(Message.obtain(h, 2, "更新已取消"));
            return;
        }
        if (isUpdate && !isDown) {
            h.sendMessage(Message.obtain(h, 2, "正在下载更新"));
            new Thread(new Runnable() {
                @Override
                public void run() {
                if (!f.exists()) {
                    try {
                        File dir = f.getParentFile();
                        if(!dir.exists()) {
                            dir.mkdirs();
                        }else{
                            File[] dfile = dir.listFiles();
                            for(File tmpf:dfile){
                                tmpf.delete();
                            }
                        }
                        f.createNewFile();
                        point = 0;
                        BufferedInputStream is = new BufferedInputStream(
                                openUrl(downUrl));
                        BufferedOutputStream os = new BufferedOutputStream(
                                new FileOutputStream(f));
                        int i = -1;
                        isDown = true;
                        while (isDown && (i = is.read()) != -1) {
                            os.write(i);
                            point++;
                            if (point % (maxPoint / 100) == 0)
                                h.sendEmptyMessage(0);
                        }
                        if (isDown) {
                            isUpdate = false;
                            os.flush();
                            os.close();
                            os.close();
                            is.close();
                            h.sendMessage(Message.obtain(h, 2, "下载完成!点击安装!"));
                            isInstall = true;
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    f.delete();
                    h.sendMessage(Message.obtain(h, 2, "下载失败"));
                }else{
                    h.sendMessage(Message.obtain(h, 2, "下载完成!点击安装!"));
                    isInstall = true;
                }

                }
            }).start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(0, 0, 0, 0);// 红色背景
        int id = canvas.saveLayer(rect, null, Canvas.ALL_SAVE_FLAG);
        // src圆
        float r = 0;
//		paint.setStyle(Style.STROKE);
//		paint.setColor(Color.WHITE);
//		canvas.drawRoundRect(rect, r, r, paint);

        //paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
        // dst方
        paint.setStyle(Style.FILL);
        LinearGradient linear = new LinearGradient(0, 0, getWidth(), getHeight(), new int[]{START, END}, null, TileMode.CLAMP);
        float right = (rect.right / maxPoint) * point;
        paint.setShader(linear);
        canvas.drawRect(rect.left, rect.top, right, rect.bottom, paint);
        paint.setShader(null);

        paint.setStyle(Style.STROKE);
        paint.setColor(STROKE);
        paint.setStrokeWidth(1.0f);
        canvas.drawRoundRect(rect, r, r, paint);

        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1.0f);
        paint.setColor(textColor);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(25.0f);
        Rect fr = new Rect();
        paint.getTextBounds(msg, 0, msg.length(), fr);
        canvas.drawText(msg, rect.width() / 2 - fr.width() / 2, rect.height()
                / 2 + fr.height() / 2, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        rect.bottom = rect.top + h;
        rect.right = rect.left + w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public InputStream openUrl(String url) throws MalformedURLException,
            IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setInstanceFollowRedirects(false);
        c.connect();
        if (c.getResponseCode() == 302) {
            String location = c.getHeaderField("Location");

            return openUrl(location);
        }
        maxPoint = c.getContentLength();
        return c.getInputStream();
    }

    private void waitUpdateAction(int version, String info, String down) {
        Context c = getContext();
        if ( code < version) {
            isUpdate = true;
            this.info = "发现新版本[点击更新]:" + info;
            this.downUrl = down;
            textColor = Color.RED;
            f = new File(Environment.getExternalStorageDirectory()+ "/SQ/update/"+version+".apk");
            h.sendMessage(Message.obtain(h, 2, this.info));
        } else {
            h.sendMessage(Message.obtain(h, 2, "当前已经是最新版本"));
        }
    }
}
