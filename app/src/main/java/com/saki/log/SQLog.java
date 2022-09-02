package com.saki.log;

import java.util.ArrayList;
import java.util.Iterator;

import com.saki.ui.LogHandler;

public class SQLog {
    private static LogHandler handler;
    public static ArrayList<String> log = new ArrayList<>();

    public static void setLogListener(LogHandler h) {
        handler = h;
    }

    public static void e(Object obj) {
        notify(2, obj);
    }

    public static void i(Object obj) {
        notify(0, obj);
    }

    private static void notify(int type, Object o) {
       if (handler != null) {
            handler.Log(type,o);
        }
    }

    public static void remove() {
        handler = null;
    }

    public static void w(Object obj) {
        notify(1, obj);
    }
}
