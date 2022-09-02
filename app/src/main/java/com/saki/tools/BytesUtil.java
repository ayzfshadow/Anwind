package com.saki.tools;

import java.io.ByteArrayOutputStream;

public class BytesUtil {
    public static byte[] addBytes(byte[]... bins) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] bin : bins) {
            for (byte b : bin)
                out.write(b);
        }
        return out.toByteArray();
    }

    public static byte[][] splitBytes(byte[] data, int len) {
        int count = data.length / len;
        int r = data.length % len;
        int c = r == 0 ? 0 : 1;
        byte[][] bins = new byte[count + c][];
        for (int i = 0; i < bins.length; i++) {
            bins[i] = i == bins.length - 1 ? new byte[r] : new byte[len];
            System.arraycopy(data, i * len, bins[i], 0, bins[i].length);
        }
        return bins;
    }
}
