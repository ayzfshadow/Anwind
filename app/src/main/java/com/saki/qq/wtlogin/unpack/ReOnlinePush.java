package com.saki.qq.wtlogin.unpack;

import com.qq.taf.jce.JceInputStream;
import com.saki.encode.ByteReader;
import com.saki.encode.Canvart;
import com.saki.qq.global.Global;
import com.saki.qq.wtlogin.pack.jce.ReadJceStruct;
import com.saki.qq.wtlogin.pack.jce.RequestPacket;
import com.saki.qq.wtlogin.pack.protobuff.ProtoBuff;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ReOnlinePush {

    public static class PbPushDisMsg extends PbPushGroupMsg {

        public PbPushDisMsg(byte[] data) {
            super(data);
        }

        @Override
        protected int getGroupDateIndex() {
            return 13;
        }

        @Override
        protected int getGroupNameIndex() {
            return 5;
        }

    }

    public static class PbPushGroupMsg extends RecviePack {

        public long fromUin;
        public long toUin;
        public long groupId;
        public String groupName;
        public String fromName;
        public String fontName;
        public String sendTitle;
        public long requstId;
        public long sendTime;
        public HashMap<String, ArrayList<String>> msg = new HashMap<String, ArrayList<String>>();

        public PbPushGroupMsg(byte[] data) {
            super(data);
            ByteReader un = new ByteReader(data);
            long len = un.readInt();
            if (len == un.length()) {
                ProtoBuff pb = new ProtoBuff(un.readRestBytes());
                pb = new ProtoBuff(pb.readLD(1));
                readUinInfo(pb.readLD(1));
                readFontStyleInfo(pb.readLD(2));
                readContent(pb.readLD(3));
            }
        }

        private void addMsg(String key, String msg) {
            ArrayList<String> list = this.msg.get(key);
            if (list == null) {
                list = new ArrayList<String>();
                this.msg.put(key, list);
            }
            list.add(msg);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PbPushGroupMsg) {
                PbPushGroupMsg m = (PbPushGroupMsg) o;
                return this.fromUin == m.fromUin && this.toUin == m.toUin
                        && this.groupId == m.groupId
                        && this.sendTime == m.sendTime
                        && this.requstId == m.requstId;
            }
            return false;
        }

        protected int getGroupDateIndex() {
            return 9;
        }

        protected int getGroupNameIndex() {
            return 8;
        }

        private void readContent(byte[] readLD) {
            ProtoBuff pb = new ProtoBuff(readLD);
            pb = new ProtoBuff(pb.readLD(1));
            readOther(pb.readLD(1));
            byte[] bin = null;
            while ((bin = pb.readLD(2)) != null) {
                readMsg(bin);
                readTitle(bin);
            }
        }

        private void readFontStyleInfo(byte[] b) {

        }

        private void readMsg(byte[] bin) {
            byte[] bin2 = new ProtoBuff(bin).readLD(1);
            if (bin2 != null) {
                String atName = "";
                byte[] bmsg = new ProtoBuff(bin2).readLD(1);// 普通消息
                if (bmsg != null)
                    addMsg("msg", atName = new String(bmsg));
                byte[] bat = new ProtoBuff(bin2).readLD(3);// @消息
                if (bat != null) {
                    ByteReader un = new ByteReader(bat);
                    un.readShort();
                    un.readInt();
                    un.readByte();
                    long id = Canvart.Bytes2UnsignedInt32(un.readBytes(4));
                    addMsg("at", id + atName);
                }
            }
            byte[] img = new ProtoBuff(bin).readLD(8);// 图片消息
            if (img != null)
                addMsg("img", new String(new ProtoBuff(img).readLD(2)));
            byte[] xml = new ProtoBuff(bin).readLD(12);// XML消息
            if (xml != null) {
                addMsg("xml",
                        new String(checkZip(new ProtoBuff(xml).readLD(1))));
            }
            byte[] json = new ProtoBuff(bin).readLD(51);// json消息
            if (json != null) {
                addMsg("json",
                        new String(checkZip(new ProtoBuff(json).readLD(1))));
            }
        }

        private void readOther(byte[] readLD) {
            ProtoBuff pb = new ProtoBuff(readLD);
            sendTime = pb.readVarint(2);
            requstId = pb.readVarint(3);
            fontName = new String(pb.readLD(9));
        }

        private void readTitle(byte[] bin) {
            bin = new ProtoBuff(bin).readLD(16);
            if (bin != null) {
                bin = new ProtoBuff(bin).readLD(7);
                if (bin != null)
                    sendTitle = new String(bin);
            }
        }

        private void readTroopInfo(byte[] b) {
            ProtoBuff g = new ProtoBuff(b);
            groupId = g.readVarint(1);
            fromName = new String(g.readLD(4));
            groupName = new String(g.readLD(getGroupNameIndex()));
        }

        private void readUinInfo(byte[] p) {
            ProtoBuff pb = new ProtoBuff(p);
            fromUin = pb.readVarint(1);
            toUin = pb.readVarint(2);
            readTroopInfo(pb.readLD(getGroupDateIndex()));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("sendUin:" + fromUin + "\r\n");
            sb.append("toUin:" + toUin + "\r\n");
            sb.append("groupId:" + groupId + "\r\n");
            sb.append("groupName:" + groupName + "\r\n");
            sb.append("sendName:" + fromName + "\r\n");
            sb.append("fontName:" + fontName + "\r\n");
            sb.append("sendTitle:" + sendTitle + "\r\n");
            sb.append("requstId:" + requstId + "\r\n");
            sb.append("sendTime:" + sendTime + "\r\n");
            sb.append("msg:" + msg + "");
            return sb.toString();
        }

    }

    public static class PbPushTransMsg extends RecviePack {

        public static final int T_ADMIN = 44;
        public static final int T_DELETE = 34;
        public long code;
        public long groupId;
        public long requstId;
        public long uin;
        public long make;
        public int type;
        public long time;
        public boolean b;

        public PbPushTransMsg(byte[] data) {
            super(data);
            ByteReader un = new ByteReader(data);
            long len = un.readInt();
            if (len == un.length()) {
                ProtoBuff pb = new ProtoBuff(un.readRestBytes());
                code = pb.readVarint(1);
                type = (int) pb.readVarint(3);
                requstId = pb.readVarint(5);
                time = pb.readVarint(7);
                byte[] bin = pb.readLD(10);
                un.update(bin);
                groupId = Canvart.Bytes2UnsignedInt32(un.readBytes(4));
                un.readBytes(1);
                if (type == 34)
                    make = Canvart.Bytes2UnsignedInt32(un.readBytes(4));
                un.readBytes(1);
                uin = Canvart.Bytes2UnsignedInt32(un.readBytes(4));
                b = un.readByte() == (byte) 0x01;
                Global.DisReq = pb.readVarint(11);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PbPushTransMsg) {
                return ((PbPushTransMsg) o).requstId == this.requstId;
            }
            return super.equals(o);
        }

    }

    public static class ReqPush extends RecviePack {

        public static class ListPack extends ReadJceStruct {
            public ArrayList<MsgInfo> list = new ArrayList<MsgInfo>();

            @Override
            public void readFrom(JceInputStream in) {
                list.add(new MsgInfo());
                list = (ArrayList<MsgInfo>) in.readobj(list, 2, true);
                long id = in.read(Long.MAX_VALUE, 3, true);
                Global.DisReq = id;
            }
        }

        public static class MsgInfo extends ReadJceStruct {

            public long id;
            public int unknow;
            public byte[] cookies;

            @Override
            public void readFrom(JceInputStream in) {
                id = in.read(Long.MAX_VALUE, 0, true);
                unknow = in.read(Short.MAX_VALUE, 3, true);
                cookies = in.read(new byte[0], 8, true);
                // System.out.println("id:" + id + "  unknow:" + unknow);
                // System.out.println(Canvart.Bytes2Hex(cookies));
            }
        }

        public ListPack listPack;
        public long requstid;

        public ReqPush(byte[] data) {
            super(data);
            ByteReader un = new ByteReader(data);
            long len = un.readInt();
            if (len == un.length()) {
                RequestPacket r = new RequestPacket();
                r.readFrom(new JceInputStream(un.readRestBytes()));
                requstid = r.iRequestId;
                // System.out.println(r);
                JceInputStream i = new JceInputStream(r.sBuffer);
                HashMap<String, HashMap<String, byte[]>> map1 = new HashMap<>();
                HashMap<String, byte[]> map2 = new HashMap<>();
                map2.put("", new byte[0]);
                map1.put("", map2);
                map1 = (HashMap<String, HashMap<String, byte[]>>) i.readobj(map1,
                        0, true);

                Iterator<String> it = map1.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    map2 = map1.get(key);
                    Iterator<String> it2 = map2.keySet().iterator();
                    while (it2.hasNext()) {
                        String key2 = it2.next();
                        byte[] bin = map2.get(key2);
                        JceInputStream j = new JceInputStream(bin);
                        this.listPack = (ListPack) j.read(new ListPack(), 0,
                                true);
                    }
                }
            }
        }

    }
}
