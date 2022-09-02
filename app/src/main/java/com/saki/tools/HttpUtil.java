package com.saki.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HttpUtil {
    public static byte[] getUrlContent(String url) {
        try {
            InputStream in = new URL(url).openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int b = -1;
            byte[] buff = new byte[0x1000];
            while ((b = in.read(buff)) != -1) {
                out.write(buff,0,b);
            }
            out.flush();
            byte[] data = out.toByteArray();
            in.close();
            out.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
