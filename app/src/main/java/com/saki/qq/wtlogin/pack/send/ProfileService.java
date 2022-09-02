package com.saki.qq.wtlogin.pack.send;

import com.saki.encode.Canvart;
import com.saki.qq.wtlogin.pack.ByteWriter;
import com.saki.qq.wtlogin.pack.protobuff.ProtoBuff;

public class ProfileService {
    public static class Pb {
        public static class ReqSystemMsgNew {
            public static class Group extends T0B01 {

                public Group(int seq) {
                    super("ProfileService.Pb.ReqSystemMsgNew.Group");
                    setSeq(seq);
                }

                @Override
                protected byte[] getContent() {
                    ByteWriter p = new ByteWriter();
                    p.writeBytes(ProtoBuff.writeVarint(1, 20));
                    p.writeBytes(ProtoBuff.writeVarint(2, 1458646553000000l));
                    p.writeBytes(ProtoBuff.writeVarint(3, System.currentTimeMillis() * 1000));
                    p.writeBytes(ProtoBuff.writeVarint(4, 1000));
                    p.writeBytes(ProtoBuff.writeVarint(5, 3));
                    p.writeBytes(ProtoBuff.writeLengthDelimt(6, Canvart
                            .Hex2Byte("08 01 10 01 18 01 28 01 30 01 38 01")));
                    p.writeBytes(ProtoBuff.writeVarint(8, 0));
                    p.writeBytes(ProtoBuff.writeVarint(9, 0));
                    p.writeBytes(ProtoBuff.writeVarint(10, 1));
                    return p.getDataAndDestroy();
                }

            }
        }

        public static class ReqSystemMsgRead {
            public static class Group extends T0B01 {

                private long requstid;

                public Group(int seq, long requstid) {
                    super("ProfileService.Pb.ReqSystemMsgRead.Group");
                    setSeq(seq);
                    this.requstid = requstid;
                }

                @Override
                protected byte[] getContent() {
                    ByteWriter p = new ByteWriter();
                    p.writeBytes(ProtoBuff.writeVarint(1, requstid));
                    p.writeBytes(ProtoBuff.writeVarint(2, 3));
                    return p.getDataAndDestroy();
                }

            }
        }

        public static class ReqSystemMsgAction {// 同意入群

            public static class Group extends T0B01 {

                private long groupid;
                private long memberid;
                private long requstId;
                private boolean agree;

                public Group(int seq, long groupid, long memberid,
                             long requstId, boolean agree) {
                    super("ProfileService.Pb.ReqSystemMsgAction.Group");
                    setSeq(seq);
                    this.groupid = groupid;
                    this.memberid = memberid;
                    this.requstId = requstId;
                    this.agree = agree;
                }

                @Override
                protected byte[] getContent() {
                    ByteWriter p = new ByteWriter();
                    p.writeBytes(ProtoBuff.writeVarint(1, 2));
                    p.writeBytes(ProtoBuff.writeVarint(2, requstId));
                    p.writeBytes(ProtoBuff.writeVarint(3, memberid));
                    p.writeBytes(ProtoBuff.writeVarint(4, 1));
                    p.writeBytes(ProtoBuff.writeVarint(5, 0));
                    p.writeBytes(ProtoBuff.writeVarint(6, 0));
                    p.writeBytes(ProtoBuff.writeVarint(7, 1));
                    ByteWriter p2 = new ByteWriter();
                    p2.writeBytes(ProtoBuff.writeVarint(1, 11));
                    p2.writeBytes(ProtoBuff.writeVarint(2, groupid));
                    p.writeBytes(ProtoBuff.writeLengthDelimt(8, p2.getDataAndDestroy()));
                    p.writeBytes(ProtoBuff.writeVarint(9, 1000));
                    return p.getDataAndDestroy();
                }

            }
        }
    }
}
