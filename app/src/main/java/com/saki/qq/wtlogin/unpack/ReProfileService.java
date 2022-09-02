package com.saki.qq.wtlogin.unpack;

import java.util.ArrayList;

import com.saki.encode.ByteReader;
import com.saki.qq.wtlogin.pack.protobuff.ProtoBuff;

public class ReProfileService {

    public static class Pb {
        public static class ReqSystemMsgNew {
            public static class Group extends RecviePack {

                public class SysMsg {
                    public long requstId;
                    public long group;
                    public long uin;
                    public long inviter;
                    public String msg;
                    public String uinName;
                    public String groupName;
                    public String inviterName;
                    public long time;
                    public int type;

                    @Override
                    public String toString() {
                        return "[系统消息" + type + "]:" + requstId + " " + group
                                + " " + uin;
                    }
                }

                public static final int UNREAD = 1;
                public static final int AGREED = 2;
                public static final int REFUSED = 3;
                public long requstId;
                public ArrayList<SysMsg> list = new ArrayList<SysMsg>();

                public Group(byte[] data) {
                    super(data);
                    ByteReader un = new ByteReader(data);
                    long len = un.readInt();
                    if (len == un.length()) {
                        ProtoBuff pb = new ProtoBuff(un.readRestBytes());
                        requstId = pb.readVarint(5);
                        byte[] bin = null;
                        while ((bin = pb.readLD(10)) != null) {
                            ProtoBuff pb2 = new ProtoBuff(bin);
                            SysMsg msg = new SysMsg();
                            msg.requstId = pb2.readVarint(3);
                            msg.time = pb2.readVarint(4);
                            msg.uin = pb2.readVarint(5);
                            bin = pb2.readLD(50);
                            if (bin != null) {
                                pb2 = new ProtoBuff(bin);
                                msg.type = (int) pb2.readVarint(1);
                                msg.msg = new String(pb2.readLD(2));
                                msg.group = pb2.readVarint(10);
                                msg.inviter = pb2.readVarint(11);
                                byte[] n = pb2.readLD(51);
                                if (n != null)
                                    msg.uinName = new String(n);
                                n = pb2.readLD(52);
                                if (n != null)
                                    msg.groupName = new String(n);
                                byte[] bin2 = pb2.readLD(53);
                                if (bin2 != null)
                                    msg.inviterName = new String(bin2);
                            }
                            list.add(msg);
                        }
                    }
                }

            }
        }
    }
}
