package com.saki.qq.global;

public class Key {
    public static final byte[] KDEFAULT = new byte[16];
    public static byte[] K010E = KDEFAULT;
    public static byte[] KSESSION = KDEFAULT;
    private static byte[] usekey = KDEFAULT;

    public static void changeKey(byte[] key) {
        usekey = key;
    }

    public static byte[] inUseKey() {
        return usekey;
    }
}
