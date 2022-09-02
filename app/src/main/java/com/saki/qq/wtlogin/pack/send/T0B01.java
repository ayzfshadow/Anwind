package com.saki.qq.wtlogin.pack.send;

import com.saki.qq.global.Key;

public abstract class T0B01 extends SendPack {

    public T0B01(String command) {
        super(command);
        setMsgType(B);
    }

    @Override
    protected void packToken4c(byte[] token4c) {
    }

    @Override
    protected byte[] UsedKey() {
        return Key.KSESSION;
    }
}
