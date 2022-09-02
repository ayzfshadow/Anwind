package com.saki.qq.wtlogin.pack.send;

import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import com.saki.qq.global.Global;
import com.saki.qq.wtlogin.pack.jce.RequestPacket;
import com.saki.qq.wtlogin.pack.jce.Troop;
import com.saki.qq.wtlogin.pack.jce.WriteJceStruct;
import java.util.ArrayList;
import java.util.HashMap;

public class FriendList {

    public static class GetFriendGroupList extends T0B01 {

        public GetFriendGroupList(int seq) {
            super("friendlist.getFriendGroupList");
            setSeq(seq);
        }

        @Override
        protected byte[] getContent() {
            HashMap<String, byte[]> m = new HashMap<String, byte[]>();
            JceOutputStream o = new JceOutputStream();
            o.write(new WriteJceStruct() {

                @Override
                public void writeTo(JceOutputStream out) {
                    out.write(3, 0);
                    out.write(1, 1);
                    out.write(Global.qq, 2);
                    out.write(0, 3);
                    out.write(20, 4);
                    out.write(0, 5);
                    out.write(1, 6);
                    out.write(0, 7);
                    out.write(0, 8);
                    out.write(0, 9);
                    out.write(1, 10);
                    out.write(9, 11);
                }
            }, 0);

            m.put("FL", o.toByteArray());
            o = new JceOutputStream();
            o.write(m, 0);
            return new RequestPacket((short) 3, (byte) 0, 0,
                    MessageSvc.PbSendMsg.getMsgSeq(),
                    "mqq.IMService.FriendListServiceServantObj",
                    "GetFriendListReq", o.toByteArray(), 0, null, null)
                    .toByteArray();
        }

    }

    public static class GetTroopListReqV2 extends T0B01 {

        public GetTroopListReqV2(int seq) {
            super("friendlist.GetTroopListReqV2");
            setSeq(seq);
        }

        @Override
        protected byte[] getContent() {
            HashMap<String, byte[]> m = new HashMap<String, byte[]>();
            JceOutputStream o = new JceOutputStream();
            o.write(new WriteJceStruct() {

                @Override
                public void writeTo(JceOutputStream out) {
                    out.write(Global.qq, 0);
                    out.write(0, 1);
                    out.write(1, 4);
                    out.write(5, 5);
                }
            }, 0);
            m.put("GetTroopListReqV2", o.toByteArray());
            o = new JceOutputStream();
            o.write(m, 0);
            return new RequestPacket((short) 3, (byte) 0, 0,
                    MessageSvc.PbSendMsg.getMsgSeq(),
                    "mqq.IMService.FriendListServiceServantObj",
                    "GetTroopListReqV2", o.toByteArray(), 0, null, null)
                    .toByteArray();
        }

    }

    public static class GetTroopMemberList extends T0B01 {

        private class Troop extends WriteJceStruct {
            @Override
            public void writeTo(JceOutputStream out) {
                out.write(Global.qq, 0);
                out.write(groupid, 1);
                out.write(0, 2);
                out.write(groupid, 3);
                out.write(2, 4);
            }

        }

        private long groupid;

        public GetTroopMemberList(int seq, long groupid) {
            super("friendlist.getTroopMemberList");
            setSeq(seq);
            this.groupid = groupid;
        }

        @Override
        protected byte[] getContent() {
            HashMap<String, byte[]> map = new HashMap<String, byte[]>();
            JceOutputStream out = new JceOutputStream();
            out.write(new Troop(), 0);
            map.put("GTML", out.toByteArray());
            out = new JceOutputStream();
            out.write(map, 0);
            return new RequestPacket((short) 3, (byte) 0, 0,
                    MessageSvc.PbSendMsg.getMsgSeq(),
                    "mqq.IMService.FriendListServiceServantObj",
                    "GetTroopMemberListReq", out.toByteArray(), 0, null, null)
                    .toByteArray();
        }

    }

    public static class ModifyGroupCardReq extends T0B01 {

        class G extends WriteJceStruct {
            private ArrayList<Mc> list;
            private long groupid;

            public G(long gid, long mid, String card) {
                this.groupid = gid;
                this.list = new ArrayList<Mc>();
                list.add(new Mc(mid, card));
            }

            @Override
            public void writeTo(JceOutputStream out) {
                out.write(0, 0);
                out.write(groupid, 1);
                out.write(0, 2);
                out.write(list, 3);
            }
        }

        class Mc extends WriteJceStruct {

            private long id;
            private String card;

            ArrayList<JceStruct> list = new ArrayList<JceStruct>();

            public Mc(long id, String card) {
                this.id = id;
                this.card = card;
            }

            @Override
            public void writeTo(JceOutputStream out) {
                out.write(id, 0);
                out.write(1, 1);
                out.write(card, 2);
                out.write((byte) 255, 3);
                out.write("", 4);
                out.write("", 5);
                out.write("", 6);
            }
        }

        private G g;

        public ModifyGroupCardReq(int seq, long groupid, long memberid,
                                  String card) {
            super("friendlist.ModifyGroupCardReq");
            g = new G(groupid, memberid, card);
            setSeq(seq);
        }

        @Override
        protected byte[] getContent() {
            JceOutputStream out = new JceOutputStream();
            out.write(g, 0);
            HashMap<String, byte[]> map = new HashMap<String, byte[]>();
            map.put("MGCREQ", out.toByteArray());
            out = new JceOutputStream();
            out.write(map, 0);
            return new RequestPacket((short) 3, (byte) 0, 0,
                    MessageSvc.PbSendMsg.getMsgSeq(),
                    "mqq.IMService.FriendListServiceServantObj",
                    "ModifyGroupCardReq", out.toByteArray(), 0, null, null)
                    .toByteArray();
        }

    }
}
