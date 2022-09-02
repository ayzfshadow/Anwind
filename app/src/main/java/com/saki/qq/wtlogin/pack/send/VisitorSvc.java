package com.saki.qq.wtlogin.pack.send;

import java.util.HashMap;

import com.saki.encode.Canvart;
import com.saki.qq.global.Global;
import com.saki.qq.wtlogin.pack.jce.RequestPacket;
import com.saki.qq.wtlogin.pack.jce.WriteJceStruct;

import com.qq.taf.jce.JceOutputStream;

public class VisitorSvc {

    public static class ReqFavorite extends T0B01 {

        private long uin;
        private int msgseq;

        public ReqFavorite(int seq, long uin) {
            super("VisitorSvc.ReqFavorite");
            setSeq(seq);
            this.uin = uin;
            msgseq = MessageSvc.PbSendMsg.getMsgSeq();
        }

        @Override
        protected byte[] getContent() {
            HashMap<String, HashMap<String, byte[]>> map = new HashMap<String, HashMap<String, byte[]>>();
            HashMap<String, byte[]> map2 = new HashMap<String, byte[]>();
            JceOutputStream o = new JceOutputStream();
            o.write(new Target(), 0);
            map2.put("QQService.RespFavorite", o.toByteArray());
            map.put("RespFavorite", map2);
            o = new JceOutputStream();
            o.write(map, 0);
            byte[] bs = new RequestPacket((short) 2, (byte) 0, 0, 0,
                    "VisitorSvc", "RespFavorite", o.toByteArray(), 0, null, null)
                    .toByteArray();
            System.out.println(Canvart.Bytes2Hex(bs));
            return bs;
        }

        private class Target extends WriteJceStruct {
            @Override
            public void writeTo(JceOutputStream out) {
                out.write(new This(), 0);
                out.write(uin, 1);
                out.write(0, 2);
                out.write(new byte[0], 3);
            }

        }

        private class This extends WriteJceStruct {
            @Override
            public void writeTo(JceOutputStream out) {
                out.write(1, 0);
                out.write(msgseq, 1);
                out.write(Global.qq, 2);
                out.write(0, 3);
                out.write("", 4);
            }
        }

    }
}
