package com.saki.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Http {
    public static final int HTTP_LOST_CODE = 404;
    private Cookie cookie;
    private String CharSet = "UTF-8";
    private int resaultCode = 0;

    public Http() {
        cookie = new Cookie();
    }
    public byte[] call(String method,String addrs,byte[] param,String... agreehard){
        try {
            return call(method, addrs, param, -1, -1, true, agreehard);
        }catch (IOException e){
            return new byte[]{};
        }
    }
    public byte[] call(String method,String addrs,byte[] param,int linkTimeOut,int readTimeOut,boolean autoJump,String... agreehard) throws IOException {
        URL url = new URL(addrs);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        if(param != null) {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
        }
        connection.addRequestProperty("Cookie", cookie.toString());
        connection.addRequestProperty("Accept-Charset", CharSet);
        connection
                .addRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        for (String referer : agreehard) {
            if (referer.contains(":")) {
                int p = referer.indexOf(":");
                connection.addRequestProperty(referer.substring(0, p),
                        referer.substring(p + 1, referer.length()));
            }
        }
        if(linkTimeOut > 0)
            connection.setConnectTimeout(linkTimeOut);
        if(readTimeOut > 0)
            connection.setReadTimeout(readTimeOut);
        connection.connect();
        OutputStream write = null;
        if(param != null) {
            write = connection.getOutputStream();
            write.write(param);
            write.flush();
        }
        resaultCode = connection.getResponseCode();
        if (connection.getHeaderFields().get("Set-Cookie") != null) {
            for (String cookie : connection.getHeaderFields().get("Set-Cookie")) {
                this.cookie.addCookie(cookie);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream isr = connection.getInputStream();
        int len = -1;
        byte[] buff = new byte[0x1000];
        while ((len = isr.read(buff)) != -1) {
            baos.write(buff,0,len);
        }
        if (write != null)
            write.close();
        if (isr != null)
            isr.close();
        connection.disconnect();
        return baos.toByteArray();
    }

    public Cookie getCookie() {
        return cookie;
    }

    public int getResaultCode() {
        return resaultCode;
    }


    public void setCharset(String charset) {
        this.CharSet = charset;
    }
}
