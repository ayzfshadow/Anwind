package com.saki.qq.wtlogin.pack.send;

import com.saki.qq.global.Key;

public abstract class T0A02 extends SendPack {

    public T0A02(String command) {
        super(command);
        setType(2);
    }

    @Override
    protected byte[] UsedKey() {
        return Key.KDEFAULT;
    }

}
