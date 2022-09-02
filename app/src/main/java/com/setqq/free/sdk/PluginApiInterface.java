package com.setqq.free.sdk;

import com.setqq.free.Msg;

public interface PluginApiInterface
 {
    Msg send(Msg msg);

    void log(int tag,String data);
}

