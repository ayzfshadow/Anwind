package com.saki.qq.global;

import java.io.Serializable;

public class Global implements Serializable {

    public static class Ksid {
        byte[] sid;

        public byte[] getBytes() {
            return sid;
        }

        public void setSid(byte[] sid) {
            this.sid = sid;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (byte b : sid) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }

    private static final long serialVersionUID = -6973342156095519891L;
    public static long qq;
    public static String password = "";
    public static Ksid ksid = new Ksid();
    public static byte[] MSGCOOKIE = new byte[]{0x1D,(byte)0xA1,(byte)0xCB,(byte)0xBF};
    public static long DisReq;
}
