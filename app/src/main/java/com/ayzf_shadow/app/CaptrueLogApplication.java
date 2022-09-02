package com.ayzf_shadow.app;

import android.app.*;
import android.content.*;
import android.os.*;
import com.saki.client.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * @Author MCSQNXA
 *
 * @Class 捕获日志
 *
 * @CreateTime 2021-01-25 14:33 周一
 */
public class CaptrueLogApplication extends Application implements Runnable,Application.ActivityLifecycleCallbacks,Thread.UncaughtExceptionHandler
{
    private static Context context;
    private static final HashSet<Activity> activity = new HashSet<>();

    @Override
    public void onActivityCreated(Activity activity,Bundle bundle)
    {

    }

    @Override
    public void onActivityStarted(Activity activity)
    {
        this.activity.add(activity);
    }

    @Override
    public void onActivityResumed(Activity activity)
    {

    }

    @Override
    public void onActivityPaused(Activity activity)
    {

    }

    @Override
    public void onActivityStopped(Activity activity)
    {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity,Bundle bundle)
    {

    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {
        this.activity.remove(activity);
    }

    @Override
    public void uncaughtException(Thread thread,Throwable throwable)
    {
        try
        {
            final File logFile = this.getLogSaveFile();

            logFile.getParentFile().mkdirs();
            logFile.createNewFile();

            final PrintWriter printWriter = new PrintWriter(logFile);
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            printWriter.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Iterator<Activity> iterator = this.activity.iterator();

        while (iterator.hasNext())
        {
            iterator.next().finish();
        }
        System.exit(0);
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.registerActivityLifecycleCallbacks(this);
        Thread.setDefaultUncaughtExceptionHandler(this);
        new Thread(this).start();
        super.attachBaseContext(this.context=base);
    }

    @Deprecated
    public File getLogSaveFile()
    {
        return new File(SQApplication.crashPath+System.currentTimeMillis()+".log");
    }

    private static final ArrayList<String> list = new ArrayList<String>();
    private static final ArrayList<CaptrueLogApplication.DebugListener> listener = new ArrayList<>();
    private static long lastSave;

    @Override
    public void run()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("logcat -v threadtime").getInputStream()));
            while (true)
            {
                if (this.isDebug())
                {
                    final String log = reader.readLine();

                    if (this.isDebugSave()&&this.lastSave+this.getDebugSaveMillisecond()<System.currentTimeMillis())
                    {
                        final File file = this.getDebugSaveFile();
                        if (file.length()>this.getDebugFileMaxBytes())
                        {
                            file.delete();
                        }
                        if (!file.exists())
                        {
                            file.getParentFile().mkdirs();
                            file.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file,true);
                        for (String line:this.list)
                        {
                            fos.write(line.getBytes());
                            fos.write("\n".getBytes());
                        }
                        fos.flush();
                        fos.close();
                        CaptrueLogApplication.list.clear();
                    }
                    if (this.isDebugSave())
                    {
                        CaptrueLogApplication.list.add(log);
                    }
                    for (CaptrueLogApplication.DebugListener listener:CaptrueLogApplication.listener)
                    {
                        listener.onReceiveLog(log);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void addDebugListener(CaptrueLogApplication.DebugListener listener)
    {
        CaptrueLogApplication.listener.add(listener);
    }

    @Deprecated
    public static interface DebugListener
    {
        void onReceiveLog(String log);
    }

    private static String packageName;

    @Deprecated
    public String getAppPackageName()
    {
        if (this.packageName!=null)
        {
            return this.packageName;
        }
        try
        {
            Class<?> thread = Class.forName("android.app.ActivityThread");
            Method currentApplication = thread.getMethod("currentApplication");
            Object context = currentApplication.invoke(null,new Object[0]);
            Method getPackageName = context.getClass().getMethod("getPackageName");
            return this.packageName=(String)getPackageName.invoke(context,new Object[0]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this.packageName;
    }

    @Deprecated
    public boolean isDebug()
    {
        return false;
    }

    @Deprecated
    public boolean isDebugSave()
    {
        return false;
    }

    @Deprecated
    public File getDebugSaveFile()
    {
        return new File(SQApplication.crashPath+"debug.log");
    }

    @Deprecated
    public int getDebugSaveMillisecond()
    {
        return 8*1000;
    }

    @Deprecated
    public int getDebugFileMaxBytes()
    {
        return 1024*1024*10;
    }
}
