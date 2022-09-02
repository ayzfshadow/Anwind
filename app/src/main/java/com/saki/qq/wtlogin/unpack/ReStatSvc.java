package com.saki.qq.wtlogin.unpack;

public class ReStatSvc {
    public static class Register extends RecviePack {

        public Register(byte[] data) {
            super(data);
        }
    }

    public static class SimpleGet extends RecviePack {

        public SimpleGet(byte[] data) {
            super(data);
        }

    }
}
