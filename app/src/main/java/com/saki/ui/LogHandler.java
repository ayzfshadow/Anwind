package com.saki.ui;

import android.os.Handler;

public abstract class LogHandler extends Handler {
    public abstract void Log(int type, Object obj);
}
