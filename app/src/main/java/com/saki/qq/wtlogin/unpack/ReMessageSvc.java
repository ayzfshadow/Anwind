package com.saki.qq.wtlogin.unpack;

import java.util.ArrayList;

import com.saki.encode.Canvart;
import com.saki.encode.ByteReader;
import com.saki.qq.global.Global;
import com.saki.qq.wtlogin.pack.protobuff.ProtoBuff;

public class ReMessageSvc {

    public static class PbGetMsg extends RecviePack {

        public class Msg {
            public long sendTime;
            public long groupid;
            public long code;
            public long fromUin;
            public long toUin;
            public String fontName;
            public String msg;
            public int type;
            public long applyUin;
            public long invateUin;
            public String applyName;

            @Override
            public String toString() {
                return "[" + sendTime + "](" + type + ")来自:[" + fromUin + "("
                        + code + ")][" + toUin + "]" + applyName + "("
                        + applyUin + "):" + msg;
            }
        }

        public static final int T_MSG = 166; // 好友消息
        public static final int T_SESS = 141; // 临时消息
        public static final int T_AD = 144; // 推广广告
        public static final int T_FRIEND_REQ = 528; // 好友申请
        public static final int T_JOIN_GRUP = 33; // 入群消息
        public static final int T_SYS_UNREAD = 34; // 系统未读
        public static final int T_SYS_INVIT = 87; // 系统邀请

        public static final int T_SYS_READ = 84; // 系统已读

        public ArrayList<Msg> msgList = new ArrayList<Msg>();

        public PbGetMsg(byte[] data) {
            super(data);
            ByteReader un = new ByteReader(data);
            long len = un.readInt();
            if (len == un.length()) {
                byte[] bin = checkZip(un.readRestBytes());
                ProtoBuff pb = new ProtoBuff(bin);
                //Global.MSGCOOKIE = pb.readLD(3);
                while ((bin = pb.readLD(5)) != null)
                    readContent(bin);
            }
        }

        private void readContent(byte[] bin) {
            ProtoBuff pb = new ProtoBuff(bin);
            while ((bin = pb.readLD(4)) != null) {
                Msg m = new Msg();
                ProtoBuff pb2 = new ProtoBuff(bin);
                readUin(pb2.readLD(1), m);

                bin = pb2.readLD(3);
                if (bin != null) {
                    pb2 = new ProtoBuff(bin);
                    readMsg(pb2.readLD(1), m);
                    bin = pb2.readLD(2);
                    if (bin != null) {
                        ByteReader un = new ByteReader(bin);
                        m.groupid = Canvart.Bytes2UnsignedInt32(un.readBytes(4));
                        try {
                            un.readByte();
                            un.readBytes(4);
                            un.readByte();
                            m.invateUin = Canvart.Bytes2UnsignedInt32(un
																	  .readBytes(4));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                msgList.add(m);
            }
        }

        private void readFont(byte[] bin, Msg m) {
            if (bin != null) {
                ProtoBuff pb = new ProtoBuff(bin);
                m.fontName = new String(pb.readLD(9));
            }
        }

        private void readMsg(byte[] bin, Msg m) {
            if (bin != null) {
                ProtoBuff pb = new ProtoBuff(bin);
                readFont(pb.readLD(1), m);
                bin = pb.readLD(2);
                if (bin != null) {
                    pb = new ProtoBuff(bin);
                    if ((bin = pb.readLD(1)) == null) {
                        bin = pb.readLD(12);
                    }
                    if (bin != null) {
                        pb = new ProtoBuff(bin);
                        m.msg = new String(checkZip(pb.readLD(1)));
                    }
                }
            }
        }

        private void readUin(byte[] bin, Msg msg) {
            ProtoBuff pb = new ProtoBuff(bin);
            msg.fromUin = pb.readVarint(1);
            msg.toUin = pb.readVarint(2);
            msg.type = (int) pb.readVarint(3);
            msg.sendTime = pb.readVarint(6);
            bin = pb.readLD(8);
            if (bin != null) {
                ProtoBuff p = new ProtoBuff(bin);
                msg.code = p.readVarint(3);
                msg.groupid = p.readVarint(4);
            }
            // msg.code = msg.fromUin;
            msg.applyUin = pb.readVarint(15);
            bin = pb.readLD(16);
            if (bin != null)
                msg.applyName = new String(bin);
        }

    }

    public static class PbshNotify extends RecviePack {

        public PbshNotify(byte[] data) {
            super(data);
        }
    }

    public static class PushForceOffline extends RecviePack {

        public PushForceOffline(byte[] data) {
            super(data);
        }

    }
}
