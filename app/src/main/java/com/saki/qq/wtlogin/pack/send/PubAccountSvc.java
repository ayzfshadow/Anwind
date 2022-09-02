package com.saki.qq.wtlogin.pack.send;

import com.saki.qq.global.Global;
import com.saki.qq.wtlogin.pack.ByteWriter;
import com.saki.qq.wtlogin.pack.protobuff.ProtoBuff;

public class PubAccountSvc {
    public static class send_msg_receipt extends T0B01 {

        private long requid;

        public send_msg_receipt(String command, long requid) {
            super("PubAccountSvc.send_msg_receipt");
            this.requid = requid;
        }

        @Override
        protected byte[] getContent() {
            ByteWriter p = new ByteWriter();
            p.writeBytes(ProtoBuff.writeVarint(1, requid));
            p.writeBytes(ProtoBuff.writeVarint(2, 100));
            p.writeBytes(ProtoBuff.writeVarint(3, Global.DisReq));
            p.writeBytes(ProtoBuff.writeVarint(4, Global.qq));
            p.writeBytes(ProtoBuff.writeVarint(5, 1));
            return p.getDataAndDestroy();
        }

    }
}
