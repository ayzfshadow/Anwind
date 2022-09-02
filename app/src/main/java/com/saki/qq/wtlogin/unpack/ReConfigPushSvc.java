package com.saki.qq.wtlogin.unpack;

public class ReConfigPushSvc {
    public static class PushDomain extends RecviePack {
        public PushDomain(byte[] data) {
            super(data);
        }

    }

    public static class PushReq extends RecviePack {

        public PushReq(byte[] data) {
            super(data);
        }
    }
}
