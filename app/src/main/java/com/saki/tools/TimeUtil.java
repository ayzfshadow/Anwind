package com.saki.tools;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimeZone;

public class TimeUtil {
    public static long getNetWorkTime() {
        long time = 0;
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            URLConnection cc = new URL("https://www.baidu.com/")
                    .openConnection();
            cc.connect();
            time = cc.getDate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return time == 0 ? System.currentTimeMillis() : time;
    }
}
