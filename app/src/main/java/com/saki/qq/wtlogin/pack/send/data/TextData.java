package com.saki.qq.wtlogin.pack.send.data;

import com.saki.qq.wtlogin.pack.protobuff.ProtoBuff;

public class TextData extends MsgData {

    private byte[] bin;

    public TextData(String msg) {
        bin = ProtoBuff.writeLengthDelimt(1, msg.getBytes());
        bin = ProtoBuff.writeLengthDelimt(1, bin);
        bin = ProtoBuff.writeLengthDelimt(2, bin);
    }

    @Override
    public byte[] getBytes() {
        return bin;
    }

}
