package com.saki.qq.wtlogin.pack.send.data;


import com.saki.qq.wtlogin.pack.ByteWriter;
import com.saki.tools.Zip;
import com.saki.log.SQLog;

public class XMLData extends MsgData {
    private byte[] a;

    public XMLData(String str) {
		SQLog.w("xml为特别增加的体验功能，更多功能请使用付费版v9或者vx");
        String replaceAll = str.replaceAll("(?s)(?i).*?service[iI][dD]=['|\"]([0-9]+).*", "$1");
        int i = 0;
        if (replaceAll.matches("[0-9]+")) {
            i = Integer.parseInt(replaceAll);
        }
        this.a = Zip.inflate(str.getBytes());
        ByteWriter classa = new ByteWriter();
        classa.writeByte((byte)1);
        classa.writeBytes(this.a);
        this.a = classa.getDataAndDontDestroyAndClean();
        classa.writeBytes(com.saki.qq.wtlogin.pack.protobuff.ProtoBuff.writeLengthDelimt(1, this.a));
        classa.writeBytes(com.saki.qq.wtlogin.pack.protobuff.ProtoBuff.writeVarint(2, (long) i));
        this.a = com.saki.qq.wtlogin.pack.protobuff.ProtoBuff.writeLengthDelimt(12, classa.getDataAndDestroy());
        this.a = com.saki.qq.wtlogin.pack.protobuff.ProtoBuff.writeLengthDelimt(2, this.a);
    }

    public byte[] getBytes() {
        return this.a;
    }
}

