package com.saki.qq.wtlogin.pack.send;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import com.saki.qq.global.Device;
import com.saki.qq.global.Global;
import com.saki.qq.wtlogin.pack.jce.RequestPacket;
import java.util.HashMap;

public class StatSvc {

    public static class Register extends T0B01 {
        private static class JceData extends JceStruct {

            @Override
            public void readFrom(JceInputStream in) {

            }

            @Override
            public void writeTo(JceOutputStream out) {
                HashMap<String, byte[]> m = new HashMap<String, byte[]>();
                JceOutputStream o = new JceOutputStream();
                o.write(new JceStruct() {

                    @Override
                    public void readFrom(JceInputStream in) {
                    }

                    @Override
                    public void writeTo(JceOutputStream out) {
                        out.write(Global.qq, 0);
                        out.write(7, 1);
                        out.write(0, 2);
                        out.write("", 3);
                        out.write(11, 4);
                        out.write(0, 5);
                        out.write(0, 6);
                        out.write(0, 7);
                        out.write(0, 8);
                        out.write(0, 9);
                        out.write(0, 10);// 59
                        out.write(19, 11);
                        out.write(1, 12);
                        out.write("", 13);
                        out.write(0, 14);// 1
                        out.write(Device.imsiMd5, 16);
                        out.write(2052, 17);
                        out.write(0, 18);
                        out.write(Device.model, 19);
                        out.write(Device.model, 20);
                        out.write(Device.osVersion, 21);
                        out.write(1, 22);
                        out.write(190, 23);
                        out.write(0, 24);
                        out.write(0, 26);
                        out.write(0, 27);
                    }
                }, 0);
                m.put("SvcReqRegister", o.toByteArray());
                out.write(m, 0);
            }

        }

        public Register(int seq) {
            super("StatSvc.register");
            setSeq(seq);
        }

        @Override
        protected byte[] getContent() {
            return new RequestPacket((short) 3, (byte) 0, 0, 0, "PushService",
                    "SvcReqRegister", new JceData().toByteArray(), 0, null,
                    null).toByteArray();
        }

    }

    public static class SimpleGet extends T0B01 {

        public SimpleGet(int seq) {
            super("StatSvc.SimpleGet");
            setSeq(seq);
        }

        @Override
        protected byte[] getContent() {
            return new byte[]{0x00, 0x00, 0x00, 0x04};
        }

    }
}
