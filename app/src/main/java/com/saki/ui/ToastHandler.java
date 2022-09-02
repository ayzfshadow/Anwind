package com.saki.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by Saki on 2018/6/5.
 */

public class ToastHandler extends Handler{
    private Context c;
    public ToastHandler(Context c)
    {
        this.c =c;
    }
    @Override
    public void handleMessage(Message msg) {
        Toast.makeText(c,msg.obj.toString(),Toast.LENGTH_SHORT).show();
    }

    public void show(String msg){
        Message m = obtainMessage();
        m.obj = msg;
        sendMessage(m);
    }
}
