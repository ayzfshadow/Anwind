package com.ayzf_shadow.tool;
import java.io.ByteArrayOutputStream;
import android.util.Log;

/**
 * @Author 暗影之风
 * 
 * @Class Tea加密总调用类
 * 
 * @CreateTime 2020-11-13 23:25 周五
 */
public class Tea
{
    public static String encrypt(String key,String text)
    {
        if (key.matches("[0-9]+") & key.length()==32)
        {
            return byte2Hex(Code.QQTEAencrypt(text.getBytes(),parseHexString(key)));
        }
        else
        {
            Log.e("Tea加解密","Tea加密key不符合全数字或长度不为32位");
            return null;
        }
    }

    public static String decrypt(String key,String text)
    {
        try
        {
            if (key.matches("[0-9]+") & key.length()==32)
            {
                return new String(Code.QQTEAdecrypt(parseHexString(text),parseHexString(key)));
            }
            else
            {
                Log.e("Tea加解密","Tea解密key不符合全数字或长度不为32位");
                return null;
            }
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    private static String byte2Hex(byte[] b)//字节数组转十六进制
    {
        StringBuilder sb = new StringBuilder();
        for (byte aB : b)
        {
            String s = Integer.toHexString(aB&0xFF);
            if (s.length()==1)
            {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    private static byte[] parseHexString(String hex)//十六进制转字节数组
    {
        if (hex==null)
        {
            return null;
        }
        hex=hex.replaceAll(" ","");
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(hex.length()>>1);
        for (int i = 0; i<=hex.length()-2; i+=2)
        {
            baos.write(Integer.parseInt(hex.substring(i,i+2),16)&255);
        }
        return baos.toByteArray();
    }
}
