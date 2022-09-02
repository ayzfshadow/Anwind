package com.saki.http;

import java.util.HashMap;
import java.util.Iterator;

public class Cookie {
    private HashMap<String, String> key_and_value = new HashMap<String, String>();

    public boolean addCookie(String cookie) {
        if (cookie.contains("=")) {
            int point = cookie.indexOf("=");
            String key = cookie.substring(0, point).replaceAll(" ", "");
            String value = null;
            if (cookie.contains(";"))
                value = cookie.substring(point + 1, cookie.indexOf(";"));
            else
                value = cookie.substring(point + 1);
            if (value.equals(""))
                return false;
            this.key_and_value.put(key, value);
            return true;
        }
        return false;
    }

    public void addCookie(String key, String value) {
        this.key_and_value.put(key, value);
    }

    public String getValue(String key) {
        String value = key_and_value.get(key);
        return value == null ? "" : value;
    }

    public void setCookie(String cookie) {
        String[] c = cookie.split(";");
        for (String c1 : c) {
            String key = c1.substring(0, c1.indexOf("="));
            String value = c1.substring(c1.indexOf("=") + 1, c1.length());
            addCookie(key, value);
        }
    }

    @Override
    public String toString() {
        String cookie = "";
        Iterator<String> keys = key_and_value.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            cookie += key + "=" + key_and_value.get(key) + ";";
        }
        return cookie;
    }
}
