package com.saki.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "SQ Crash";
    private UncaughtExceptionHandler defaultCrashHandler;
    private static CrashHandler instance;
    private Context context;
    public static CrashHandler getInstance() {
        if (instance == null)
            instance = new CrashHandler();
        return instance;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public HashMap<String, String> collectDeviceInfo(Context ctx) {
        HashMap<String, String> infos = new HashMap<String, String>();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
        return infos;
    }

    public void exitApplication(long time) {
        Log.e(TAG, time + "ms 后退出程序!");
        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.cancelAll();
        if (time != 0) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                Log.e(TAG, "error :", e);
            }
        }
        // 退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void init(Context context) {
        this.context = context;
        defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private void saveLog(String log) {
        try {
            String path = Environment.getExternalStorageDirectory()
                    + "/SQ/log_red/crash.log_red";
            File f = new File(path);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            out.write(log.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex == null && defaultCrashHandler != null)
            defaultCrashHandler.uncaughtException(thread, ex);
        if (ex != null) {
            ex.printStackTrace();
            // HashMap<String, String> info = collectDeviceInfo(context);
            final StringBuilder sb = new StringBuilder();
            // for (Map.Entry<String, String> enty : info.entrySet()) {
            // sb.append(enty.getKey() + "=" + enty.getValue() + "\n");
            // }
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(pw);
                cause = cause.getCause();
            }
            pw.close();
            sb.append("\r\n" + sw.toString() + "\r\n");
            saveLog(sb.toString());
        }
        exitApplication(3000);
    }
}
