package com.saki.qq.wtlogin.pack.send;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import com.saki.qq.global.App;
import com.saki.qq.global.Device;
import com.saki.qq.global.Global;
import com.saki.qq.global.Token;
import com.saki.qq.wtlogin.pack.jce.RequestPacket;
import java.util.HashMap;

public class GrayUinPro {

    public static class Check extends T0A02 {

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
                        out.write(App.SUBAPP, 1);
                        out.write(String.valueOf(Global.qq), 2);
                        out.write("", 3);
                        out.write("", 4);
                    }
                }, 0);
                m.put("req", o.toByteArray());
                out.write(m, 0);
            }
        }

        public Check(int seq) {
            super("GrayUinPro.Check");
            setRequestId(seq);
            setAppsub(App.SUBAPP);
            setImei(Device.imei);
            setVersion(App.PACKVERSION);
            setToken4c(Token.T004C);
        }

        @Override
        protected byte[] getContent() {
            return new RequestPacket((short) 3, (byte) 0, 0,
                    MessageSvc.PbSendMsg.getMsgSeq(),
                    "KQQ.ConfigService.ConfigServantObj", "ClientReq",
                    new JceData().toByteArray(), 0, null, null).toByteArray();
        }
    }
}
