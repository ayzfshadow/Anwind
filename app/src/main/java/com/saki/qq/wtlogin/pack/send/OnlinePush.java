package com.saki.qq.wtlogin.pack.send;

import com.qq.taf.jce.JceOutputStream;
import com.saki.qq.global.Global;
import com.saki.qq.wtlogin.pack.jce.RequestPacket;
import com.saki.qq.wtlogin.pack.jce.WriteJceStruct;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.ReqPush.MsgInfo;
import java.util.ArrayList;
import java.util.HashMap;

public class OnlinePush {
    public static class RespPush extends T0B01 {

        public class W extends WriteJceStruct {

            private long id;
            private int unknow;
            private byte[] cookies;

            public W(long id, int unknow, byte[] cookies) {
                this.id = id;
                this.unknow = unknow;
                this.cookies = cookies;
            }

            @Override
            public void writeTo(JceOutputStream out) {
                out.write(id, 0);
                out.write(0, 1);
                out.write(unknow, 2);
                out.write(cookies, 3);
            }
        }

        ArrayList<W> list = new ArrayList<W>();

        private long id;

        public RespPush(int seq, long requstid, ArrayList<MsgInfo> arry) {
            super("OnlinePush.RespPush");
            setSeq(seq);
            this.id = requstid;
            if (arry != null)
                for (MsgInfo j : arry) {
                    list.add(new W(j.id, j.unknow, j.cookies));
                }
        }

        @Override
        protected byte[] getContent() {
            JceOutputStream o = new JceOutputStream();
            o.write(new WriteJceStruct() {
                @Override
                public void writeTo(JceOutputStream out) {
                    out.write(Global.qq, 0);
                    out.write(list, 1);
                    out.write((int) Global.DisReq, 2);
                }
            }, 0);
            HashMap<String, byte[]> m = new HashMap<String, byte[]>();
            m.put("resp", o.toByteArray());
            o = new JceOutputStream();
            o.write(m, 0);
            return new RequestPacket((short) 3, (byte) 0, 0, (int) id,
                    "OnlinePush", "SvcRespPushMsg", o.toByteArray(), 0, null,
                    null).toByteArray();
        }

    }
}
