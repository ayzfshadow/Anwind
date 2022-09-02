package com.saki.qq.wtlogin.pack.send;

import com.saki.encode.Canvart;
import com.saki.encode.Code;
import com.saki.qq.global.Global;
import com.saki.qq.wtlogin.pack.ByteWriter;

public abstract class SendPack {
    public static final byte A = 0x0A;
    public static final byte B = 0x0B;
    ByteWriter pack = new ByteWriter();

    private byte cmdType = A;
    private byte type = 1;
    private byte[] token44;
    private int seq = -1;
    private long uin = Global.qq;
    protected String command;
    private long appsub = -1;
    private boolean dub = true;
    private byte[] token4c;
    protected byte[] msgCookie = new byte[]{0x1D,(byte)0xA1,(byte)0xCB,(byte)0xBF};
    private String imei;
    private byte[] token14;
    private String version;
    private byte[] key;
    private byte[] publicKey;
    private byte[] cmd;
    private int seqid = -1;

    public SendPack(String command) {
		System.out.println(command);
        this.command = command;
    }

    private void encodeContent(byte[] content, byte[] key) {
		//System.out.println("encryptn "+Canvart.Bytes2Hex(content));
        content = Code.QQTEAencrypt(content, key);
		
        pack.writeBytes(content);
        pack.rewriteBytes(Canvart.Int2Bytes(pack.length() + 4));
    }

    protected abstract byte[] getContent();

    public int getSeq() {
        return seq;
    }

    public long getUin() {
        return uin;
    }

    private void packAppSub(long appsub, boolean dub) {
        if (appsub != -1) {
            pack.writeInt(appsub);
            if (dub)
                pack.writeInt(appsub);
            else
                pack.writeInt(-1);
            pack.writeBytes(new byte[]{0x01, 0x00, 0x00, 0x00});
            pack.writeInt(0);
            pack.writeInt(0);
        }
    }

    private void packCMD(byte cmd, byte type) {
        pack.writeByte((byte)0);
        pack.writeByte((byte)0);
        pack.writeByte((byte)0);
        pack.writeByte(cmd);
        pack.writeByte(type);
    }

    private void packCommand(String cmd) {
        pack.writeInt(cmd.length() + 4);
        pack.writeBytes(cmd);
    }

    protected void packContent() {
        packContent(uin, cmd, key, publicKey, getContent());
    }

    public void packContent(long qq, byte[] cmd, byte[] key, byte[] publicKey,
                            byte[] bin) {
        if (cmd == null || key == null || publicKey == null) {
            pack.writeInt(bin.length + 4);
            pack.writeBytes(bin);
        } else {
            ByteWriter body = new ByteWriter();
            body.writeBytes(new byte[]{0x1F, 0x41});
            body.writeBytes(cmd);
            body.writeBytes(new byte[]{0x00, 0x01});
            body.writeInt(qq);
            body.writeBytes(new byte[]{0x03, 0x07, 0x00, 0x00, 0x00, 0x00,
                    0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
                    0x01});
            body.writeBytes(key);
            body.writeBytes(new byte[]{0x01, 0x02});
            body.writeShort(publicKey.length);
            body.writeBytes(publicKey);
            //
            body.writeBytes(bin);
            body.rewriteShort((short)(body.getData().length + 4));
            body.rewriteByte((byte) 0x02);
            body.rewriteInt(body.length() + 5);
            body.writeByte((byte)3);
            pack.writeBytes(body.getDataAndDestroy());
        }
    }

    protected void packHead() {
        byte[] bin = pack.getDataAndDestroy();
        pack.reCreate();
        packCMD(cmdType, type);
        packToken44(token44, seq);
        packUin(uin == -1 ? null : String.valueOf(uin));
        encodeContent(bin, UsedKey());
    }

    private void packImei(String imei) {
        if (imei != null) {
            pack.writeInt(imei.length() + 4);
            pack.writeBytes(imei);
        }
    }

    protected void packInfo() {
        packRequestionId(seqid);
        packAppSub(appsub, dub);
        packToken4c(token4c);
        packCommand(command);
        packMsgCookie(msgCookie);
        packITV(imei, token14, version);
        //pack.writeInt(4);// 未知数据位置

        pack.rewriteInt(pack.length() + 4);
        packContent();
    }

    private void packITV(String imei, byte[] token14, String version) {
        if (imei != null && version != null) {
            packImei(imei);
            packToken14(token14);
            packVersion(version);
        }
    }

    private void packMsgCookie(byte[] msgCookie) {
        if (msgCookie != null) {
            pack.writeInt(msgCookie.length + 4);
            pack.writeBytes(msgCookie);
        } else {
            pack.writeInt(4);
        }
    }

    private void packRequestionId(int seq) {
        if (seq != -1)
            pack.writeInt(seq);
    }

    private void packToken14(byte[] token14) {
        if (token14 != null) {
            pack.writeInt(token14.length + 4);
            pack.writeBytes(token14);
        } else {
            pack.writeInt(4);
        }
    }

    private void packToken44(byte[] token44, int seq) {
        if (token44 != null) {
            pack.writeInt(token44.length + 4);
            pack.writeBytes(token44);
        } else if (seq != -1) {
			System.out.println(seq);
            pack.writeInt(seq);
        } else {
            pack.writeInt(4);
        }
    }

    protected void packToken4c(byte[] token4c) {
        if (token4c != null) {
            pack.writeInt(token4c.length + 4);
            pack.writeBytes(token4c);
        } else {
            pack.writeInt(4);
        }
    }

    private void packUin(String uin) {
        pack.writeByte((byte)0);
        if (uin == null) {
            pack.writeByte((byte)0);
            pack.writeInt(1328);
        } else {
            pack.writeInt(uin.length() + 4);
            pack.writeBytes(uin);
        }
    }

    private void packVersion(String version) {
        if (version != null) {
            pack.writeShort(version.length() + 2);
            pack.writeBytes(version);
        }
    }

    public void setAppsub(long appsub) {
        this.appsub = appsub;
    }

    public void setAppsub(long appsub, boolean dub) {
        this.appsub = appsub;
        this.dub = dub;
    }

    public void setCmd(byte[] cmd) {
        this.cmd = cmd;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public void setMsgCookie(byte[] msgCookie) {
        this.msgCookie = msgCookie;
    }

    public void setMsgType(byte cmdType) {
        this.cmdType = cmdType;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public void setRequestId(int seqid) {
        this.seqid = seqid;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setToken14(byte[] token14) {
        this.token14 = token14;
    }

    public void setToken44(byte[] token44) {
        this.token44 = token44;
    }

    public void setToken4c(byte[] token4c) {
        this.token4c = token4c;
    }

    public void setType(int i) {
        this.type = (byte) i;
    }

    public void setUin(long uin) {
        this.uin = uin;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public byte[] toByteArray() {
        if (pack.isEmpty()) {
            packInfo();
            packHead();
        }
		byte[]o=pack.getDataAndDestroy();
		System.out.println("send: "+Canvart.Bytes2Hex(o));
        return o;
    }

    protected abstract byte[] UsedKey();

}
