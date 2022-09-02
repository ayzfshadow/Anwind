package com.saki.qq.wtlogin.pack.jce;

import com.qq.taf.jce.HexUtil;
import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import com.saki.encode.Canvart;
import java.util.HashMap;
import java.util.Map;

public final class RequestPacket extends JceStruct {
    static Map<String, String> cache_context;
    static byte[] cache_sBuffer;
    public byte cPacketType = 0;
    public Map<String, String> context;
    public int iMessageType = 0;
    public int iRequestId = 0;
    public int iTimeout = 0;
    public short iVersion = 0;
    public byte[] sBuffer;
    public String sFuncName = null;
    public String sServantName = null;
    public Map<String, String> status;

    public RequestPacket() {
    }

    public RequestPacket(short iVersion, byte cPacketType, int iMessageType,
                         int iRequestId, String sServantName, String sFuncName,
                         byte[] sBuffer, int iTimeout, Map<String, String> context,
                         Map<String, String> status) {
        this.iVersion = iVersion;
        this.cPacketType = cPacketType;
        this.iMessageType = iMessageType;
        this.iRequestId = iRequestId;
        this.sServantName = sServantName;
        this.sFuncName = sFuncName;
        this.sBuffer = sBuffer;
        this.iTimeout = iTimeout;
        this.context = context == null ? new HashMap() : context;
        this.status = status == null ? new HashMap() : status;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException localCloneNotSupportedException) {
            throw new AssertionError();
        }
    }

    @Override
    public void readFrom(JceInputStream paramJceInputStream) {
        try {
            this.iVersion = paramJceInputStream.read(this.iVersion, 1, true);
            this.cPacketType = paramJceInputStream.read(this.cPacketType, 2,
                    true);
            this.iMessageType = paramJceInputStream.read(this.iMessageType, 3,
                    true);
            this.iRequestId = paramJceInputStream
                    .read(this.iRequestId, 4, true);
            this.sServantName = paramJceInputStream.readString(5, true);
            this.sFuncName = paramJceInputStream.readString(6, true);
            if (cache_sBuffer == null) {
                cache_sBuffer = new byte[]{0};
            }
            this.sBuffer = (paramJceInputStream.read(cache_sBuffer, 7, true));
            this.iTimeout = paramJceInputStream.read(this.iTimeout, 8, true);
            if (cache_context == null) {
                cache_context = new HashMap();
                cache_context.put("", "");
            }
            this.context = ((Map) paramJceInputStream.readobj(cache_context, 9,
                    true));
            if (cache_context == null) {
                cache_context = new HashMap();
                cache_context.put("", "");
            }
            this.status = ((Map) paramJceInputStream.readobj(cache_context, 10,
                    true));
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
            System.out.println("RequestPacket decode error "
                    + HexUtil.bytes2HexStr(this.sBuffer));
            throw new RuntimeException(localException);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sFuncName:" + sFuncName + "\r\n");
        sb.append("sServantName:" + sServantName + "\r\n");
        sb.append("cPacketType:" + cPacketType + "\r\n");
        sb.append("iMessageType:" + iMessageType + "\r\n");
        sb.append("iRequestId:" + iRequestId + "\r\n");
        sb.append("iTimeout:" + iTimeout + "\r\n");
        sb.append("iVersion:" + iVersion + "\r\n");
        sb.append("cPacketType:" + cPacketType + "\r\n");
        sb.append("sBuffer:" + Canvart.Bytes2Hex(sBuffer) + "\r\n");
        return sb.toString();
    }

    @Override
    public void writeTo(JceOutputStream paramJceOutputStream) {
        paramJceOutputStream.write(this.iVersion, 1);
        paramJceOutputStream.write(this.cPacketType, 2);
        paramJceOutputStream.write(this.iMessageType, 3);
        paramJceOutputStream.write(this.iRequestId, 4);
        paramJceOutputStream.write(this.sServantName, 5);
        paramJceOutputStream.write(this.sFuncName, 6);
        paramJceOutputStream.write(this.sBuffer, 7);
        paramJceOutputStream.write(this.iTimeout, 8);
        paramJceOutputStream.write(this.context, 9);
        paramJceOutputStream.write(this.status, 10);
    }
}
