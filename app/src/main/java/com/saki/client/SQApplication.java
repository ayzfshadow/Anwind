package com.saki.client;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;
import com.ayzf_shadow.app.CaptrueLogApplication;
import com.ayzf_shadow.app.SQErrorActivity;
import com.mcsqnxa.common.LocalServer;
import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SQApplication extends CaptrueLogApplication implements Application.ActivityLifecycleCallbacks
{
    private static LocalServer server;

    public static LocalServer getServer()
    {
        return SQApplication.server;
    }

    public static void setServer(LocalServer server)
    {
        SQApplication.server=server;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        CrashHandler.getInstance().init(getApplicationContext());
    }
    static
    {
        System.loadLibrary("shadow-app");
    }

    public static final native void toPiracyDetection(Context context);

    /**
     * 上下文
     */
    public static Context base;

    /**
     * Activity
     */
    public static Activity activity;

    /*
     * 全局共享线程池
     */
    public static final ExecutorService threadPool = Executors.newCachedThreadPool();

    /*
     * 崩溃日志存储路径
     */
    public static final String crashPath = Environment.getExternalStorageDirectory() + "/Android/data/com.ayzf.anwind/cache/logs/";

	@Override
	protected void attachBaseContext(Context base)
    {
		super.attachBaseContext(this.base = base);
		Thread.setDefaultUncaughtExceptionHandler(this);
		this.toPiracyDetection(this);
    }

	@Override
	public void uncaughtException(Thread thread,Throwable throwable)
    {
		if (SQApplication.activity != null)
        {
			SQApplication.activity.finishAffinity();
        }
		final File log = this.saveException(throwable);
		final Intent intent = new Intent();
		intent.setClass(this,SQErrorActivity.class);
		intent.putExtra("log",log.toString());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		super.startActivity(intent);
		System.exit(0);
    }

	public static Context getContext()
    {
		return SQApplication.base;
    }

	/**
	 * 保存异常
	 */
	private static synchronized File saveException(Throwable throwable)
    {
		final File file = new File(SQApplication.getLogFile());
		try
        {
			if (!file.isFile())
            {
				file.getParentFile().mkdirs();
				file.createNewFile();
            }
			//保存异常
			final PrintWriter printWriter = new PrintWriter(file);
			throwable.printStackTrace(printWriter);
			printWriter.flush();
			printWriter.close();
        }
		catch (Exception io)
        {
			io.printStackTrace();
        }
		return file;
    }

	/**
	 * 崩溃日志路径
	 */
	private static synchronized String getLogFile()
    {
		return "/data/user/0/com.ayzf.anwind/cache/logs/" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + ".log";
    }

//	/**
//	 * 弹窗
//	 */
//	public static synchronized void toast(String message,boolean longShow)
//    {
//		final Toast toast = Toast.makeText(SQApplication.base,null,longShow ?Toast.LENGTH_LONG: Toast.LENGTH_SHORT);
//		toast.setText(message);
//		toast.show();
//    }
//
//	/**
//	 * 检测service是否存活
//	 */
//	private static boolean isServiceRunning(Class serviceClass)
//    {
//		if (serviceClass == null)
//        {
//	        return false;
//        }
//        final ActivityManager manager = (ActivityManager)SQApplication.base.getSystemService(Context.ACTIVITY_SERVICE);
//		for (ActivityManager.RunningServiceInfo info:manager.getRunningServices(200))
//        {
//	        if (info.service.getClassName().equals(serviceClass.getCanonicalName()))
//            {
//		        return true;
//            }
//        }
//        return false;
//    }
}
