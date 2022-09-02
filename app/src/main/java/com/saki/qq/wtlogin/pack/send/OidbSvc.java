package com.saki.qq.wtlogin.pack.send;

import com.saki.encode.Canvart;
import com.saki.qq.global.App;
import com.saki.qq.global.Device;
import com.saki.qq.global.Global;
import com.saki.qq.global.Key;
import com.saki.qq.global.Token;
import com.saki.qq.wtlogin.pack.ByteWriter;
import com.saki.qq.wtlogin.pack.protobuff.ProtoBuff;

public class OidbSvc {

    public static class Ox496 extends T0B01 {

        public Ox496(int seq) {
            super("OidbSvc.0x496");
            setSeq(seq);
        }

        @Override
        protected byte[] getContent() {
            return Canvart.Hex2Byte("08 96 09 10 00 22 04 20 01 28 01");
        }

    }

    // 成员禁言
    public static class Ox570_8 extends T0B01 {

        private long groupid;
        private long uin;
        private int time;

        public Ox570_8(int seq, long group, long uin, int time) {
            super("OidbSvc.0x570_8");
            setSeq(seq);
            this.groupid = group;
            this.uin = uin;
            this.time = time;
        }

        @Override
        protected byte[] getContent() {
            ByteWriter p = new ByteWriter();
            p.writeInt(groupid);
            p.writeByte((byte)0x20);
            p.writeShort(1);
            p.writeInt(uin);
            p.writeInt(time);
            byte[] bin = p.getDataAndDontDestroyAndClean();
        
            p.writeBytes(ProtoBuff.writeVarint(1, 1392));
            p.writeBytes(ProtoBuff.writeVarint(2, 8));
            p.writeBytes(ProtoBuff.writeVarint(3, 0));
            p.writeBytes(ProtoBuff.writeLengthDelimt(4, bin));
            return p.getDataAndDestroy();
        }

    }

    public static class Ox59f extends SendPack {
        public Ox59f(int seq) {
            super("OidbSvc.0x59f");
            setSeq(seq);
            setRequestId(seq);
            setToken44(Token.T0044);
            setToken4c(Token.T004C);
            setAppsub(App.SUBAPP);
            setImei(Device.imei);
            setVersion(App.PACKVERSION);
        }

        @Override
        protected byte[] getContent() {
            return Canvart
                    .Hex2Byte("08 9F 0B 10 01 18 00 22 00 32 0D 61 6E 64 72 6F 69 64 20 36 2E 35 2E 30");
        }

        @Override
        protected byte[] UsedKey() {
            return Key.KSESSION;
        }
    }

    public static class Ox7a2_0 extends T0B01 {

        public Ox7a2_0(int seq) {
            super("OidbSvc.0x7a2_0");
            setSeq(seq);
        }

        @Override
        protected byte[] getContent() {
            ByteWriter p = new ByteWriter();
            p.writeBytes(ProtoBuff.writeVarint(1, 1954));
            p.writeBytes(ProtoBuff.writeVarint(2, 0));
            p.writeBytes(ProtoBuff.writeVarint(3, 0));
            p.writeBytes(ProtoBuff.writeLengthDelimt(4,
                    ProtoBuff.writeVarint(1, 0)));
            return p.getDataAndDestroy();
        }

    }

    public static class Ox7c4_0 extends T0B01 {

        public Ox7c4_0(int seq) {
            super("OidbSvc.0x7c4_0");
            setSeq(seq);
        }

        @Override
        protected byte[] getContent() {
            ByteWriter p = new ByteWriter();
            p.writeBytes(ProtoBuff.writeVarint(1, Global.qq));
            p.writeBytes(ProtoBuff.writeVarint(2, 0));
            p.writeBytes(ProtoBuff.writeVarint(3, 0));
            p.writeBytes(ProtoBuff.writeVarint(4, 100));
            byte[] bin = p.getDataAndDontDestroyAndClean();
           
            p.writeBytes(ProtoBuff.writeLengthDelimt(2, bin));
            p.writeBytes(ProtoBuff.writeVarint(3, 2));
            bin = p.getDataAndDontDestroyAndClean();
          
            p.writeBytes(ProtoBuff.writeVarint(1, 1988));
            p.writeBytes(ProtoBuff.writeVarint(2, 0));
            p.writeBytes(ProtoBuff.writeVarint(3, 0));
            p.writeBytes(ProtoBuff.writeLengthDelimt(4, bin));
            return p.getDataAndDestroy();
        }

    }

    // 全体禁言
    public static class Ox89a_0 extends T0B01 {

        private long groupid;
        private boolean shutup;

        public Ox89a_0(int seq, long group, boolean shutup) {
            super("OidbSvc.0x89a_0");
            setSeq(seq);
            this.groupid = group;
            this.shutup = shutup;
        }

        @Override
        protected byte[] getContent() {
            ByteWriter p = new ByteWriter();
            p.writeBytes(ProtoBuff.writeVarint(1, groupid));
            p.writeBytes(ProtoBuff.writeLengthDelimt(2,
                    ProtoBuff.writeVarint(17, shutup ? 268435455 : 0)));
            byte[] bin = p.getDataAndDontDestroyAndClean();
          
            p.writeBytes(ProtoBuff.writeVarint(1, 2202));
            p.writeBytes(ProtoBuff.writeVarint(2, 0));
            p.writeBytes(ProtoBuff.writeVarint(3, 0));
            p.writeBytes(ProtoBuff.writeLengthDelimt(4, bin));
            return p.getDataAndDestroy();
        }

    }

    // 移除成员
    public static class Ox8a0_0 extends T0B01 {

        private long id;
        private long gid;

        public Ox8a0_0(int seq, long gid, long id) {
            super("OidbSvc.0x8a0_0");
            setSeq(seq);
            this.id = id;
            this.gid = gid;
        }

        @Override
        protected byte[] getContent() {
            ByteWriter p = new ByteWriter();
            p.writeBytes(ProtoBuff.writeVarint(1, 5));
            p.writeBytes(ProtoBuff.writeVarint(2, id));
            p.writeBytes(ProtoBuff.writeVarint(3, 0));
            byte[] bin = p.getDataAndDontDestroyAndClean();
            
            p.writeBytes(ProtoBuff.writeVarint(1, gid));
            p.writeBytes(ProtoBuff.writeLengthDelimt(2, bin));
            bin = p.getDataAndDontDestroyAndClean();
           
            p.writeBytes(ProtoBuff.writeVarint(1, 2208));
            p.writeBytes(ProtoBuff.writeVarint(2, 0));
            p.writeBytes(ProtoBuff.writeVarint(3, 0));
            p.writeBytes(ProtoBuff.writeLengthDelimt(4, bin));
            return p.getDataAndDestroy();
        }

    }
}
