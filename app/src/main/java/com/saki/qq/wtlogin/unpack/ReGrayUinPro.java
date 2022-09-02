package com.saki.qq.wtlogin.unpack;

import com.qq.taf.jce.JceInputStream;
import com.saki.encode.ByteReader;
import com.saki.qq.wtlogin.pack.jce.RequestPacket;

public class ReGrayUinPro extends RecviePack {

    public RequestPacket pack = new RequestPacket();

    public ReGrayUinPro(byte[] data) {
        super(data);
        ByteReader un = new ByteReader(data);
        un.readInt();
        long len = un.readInt();
        if (len == un.length()) {
            JceInputStream in = new JceInputStream(un.readRestBytes());
            pack.readFrom(in);
        }
    }

}
