package com.ayzf_shadow.tool.jiance;

import android.app.*;
import android.net.*;
import android.net.Proxy;
import android.os.*;
import android.text.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

/**
 * @Author 暗影之风
 * 
 * @Class VPN检测类
 * 
 * @CreateTime 2021-01-11 12:10 周一
 */
public final class vpn
{
    public static boolean ֏֏֏֏֏֏()
    {
        int i;
        String str;
        if (vpn3.֏())
        {
            return false;
        }
        Application r0 = vpn2.֏();
        if (r0 != null)
        {
            boolean z = true;
            if (Build.VERSION.SDK_INT >= 14)
            {
                str = System.getProperty("http.proxyHost");
                String property = System.getProperty("http.proxyPort");
                if (property == null)
                {
                    property = "-1";
                }
                i = Integer.parseInt(property);
            }
            else
            {
                String host = Proxy.getHost(r0);
                i = Proxy.getPort(r0);
                str = host;
            }
            if (TextUtils.isEmpty(str) || i == -1)
            {
                z = false;
            }
            if (z)
            {
                return false;
            }
        }
        return true;
    }
}
class vpn2
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
class vpn3
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
