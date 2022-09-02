package com.saki.c;

import com.saki.encode.Canvart;
import com.saki.encode.Code;

/**
 * Created by Saki on 2018/7/22.
 */

public class e {
    public static String a(byte[] a,byte[] b){
        return Canvart.Bytes2Hex(Code.QQTEAencrypt(a,b),"");
    }
    public static String b(byte[] a,byte[] b){
        return new String(Code.QQTEAdecrypt(a,b));
    }
    public static byte[] m(String c){
        return Code.MD5(c.getBytes());
    }
}
