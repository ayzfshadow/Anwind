package com.ayzf_shadow.tool.jiance;

import android.app.*;
import android.os.*;
import android.os.Process;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

/**
 * @Author 暗影之风
 * 
 * @Class XPOSED检测类
 * 
 * @CreateTime 2021-01-11 12:35 周一
 */
public final class xposed
{
    public static boolean ֏֏֏֏֏֏()
    {
        Application r0 = xposed2.֏();
        if (r0 != null)
        {
            try
            {
                if (checkXpFormMap())
                {
                    return false;
                }
                if (r0.getClassLoader().loadClass("de.robv.android.xposed.XposedBridge") != null)
                {
                    return false;
                }
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
    private static boolean checkXpFormMap()
    {
        File file;
        String readLine;
        int myPid = Process.myPid();
        if (myPid < 0)
        {
            file = new File("/proc/self/maps");
        }
        else
        {
            file = new File("/proc/" + myPid + "/maps");
        }
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            do {
                readLine = bufferedReader.readLine();
                if (readLine == null)
                {
                    return false;
                }
                if (readLine.contains("libdexposed.so") || readLine.contains("libsubstrate.so"))
                {
                    return true;
                }
            } while (!readLine.contains("libepic.so"));
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return true;
        }
    }
}
class xposed2
{
    public static Application ֏()
    {
        try
        {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method method = cls.getMethod("currentApplication",new Class[0]);
            method.invoke(cls,new Object[0]);
            return (Application) method.invoke(cls,new Object[0]);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
class xposed3
{
    public static boolean ֏()
    {
        try
        {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces == null)
            {
                return false;
            }
            Iterator it = Collections.list(networkInterfaces).iterator();
            while (it.hasNext())
            {
                NetworkInterface networkInterface = (NetworkInterface) it.next();
                if (networkInterface.isUp() && networkInterface.getInterfaceAddresses().size() != 0)
                {
                    if ("tun0".equals(networkInterface.getName()) || "ppp0".equals(networkInterface.getName()))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        catch (Throwable th)
        {
            th.printStackTrace();
            return false;
        }
    }
}
