package com.saki.encode;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * 包封装类
 *
 * @author Saki
 */
public class Canvart {
    private static long UINT32_MAX = 0xFFFFFFFFL;
    private static long BYTE_1 = 0xFFL;
    private static long BYTE_2 = 0xFF00L;
    private static long BYTE_3 = 0xFF0000L;
    private static long BYTE_4 = 0xFF000000L;

    public static String Bytes2Hex(byte[] b) {
        return Bytes2Hex(0, b.length, b, " ");
    }

    public static String Bytes2Hex(byte[] b, String slipt) {
        return Bytes2Hex(0, b.length, b, slipt);
    }

    public static String Bytes2Hex(int o, int len, byte[] b, String slipt) {
        if (b == null)
            return "";
        StringBuilder builder = new StringBuilder();
        for (int i = o; i < b.length && len > 0; i++) {
            len--;
            builder.append(String.format("%02X" + slipt, b[i]));
        }
        return builder.toString();
    }

    public static int Bytes2Int(byte[] b) {
        return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16)
                | ((b[2] & 0xff) << 8) | (b[3] & 0xff);
    }

    public static String Bytes2IP(byte[] bytes) {
        if (bytes.length != 4)
            return "";
        return (bytes[0] & 0xff) + "." + (bytes[1] & 0xff) + "."
                + (bytes[2] & 0xff) + "." + (bytes[3] & 0xff);
    }

    public static long Bytes2Long(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        return bb.getLong();
    }

    public static short Bytes2Short(byte[] b) {
        return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
    }

    /**
     * 将4个byte转为 Unsigned Integer 32，以 long 形式返回
     *
     * @param bs 需要转换的字节
     * @return 返回 long，高32位为0，低32位视为Unsigned Integer
     */
    public static long Bytes2UnsignedInt32(byte[] bs) {
        if (bs.length != 4)
            return 0;
        return ((bs[0] << 24) & BYTE_4) + ((bs[1] << 16) & BYTE_3)
                + ((bs[2] << 8) & BYTE_2) + (bs[3] & BYTE_1);
    }

    public static int findByte(byte[] src, byte[] tag, int pos) {
        if (src == null || tag == null || src.length == 0 || tag.length == 0)
            return -1;
        for (int i = pos; i < src.length; i++) {
            boolean b = true;
            for (int p = 0; p < tag.length; p++) {
                if (i + p < src.length && src[i + p] != tag[p]) {
                    b = false;
                    break;
                }
            }
            if (b)
                return i;
        }
        return -1;
    }

    public static byte[] getMiddleBytes(byte[] src, byte[] start, byte[] end,
                                        boolean include) {
        if (src == null || src.length == 0 || start == null
                || start.length == 0 || end == null || end.length == 0)
            return new byte[]{};
        int s = findByte(src, start, 0);
        if (s != -1) {
            int e = findByte(src, end, 0);
            if (e != -1 && s + start.length < e) {
                byte[] bin;
                int index = s;
                if (include) {
                    bin = new byte[e + end.length - s];
                } else {
                    index += start.length;
                    bin = new byte[e - s - start.length];
                }
                System.arraycopy(src, index, bin, 0, bin.length);
                return bin;
            }
        }
        return new byte[]{};
    }

    // 转换指定规格的十六进制文本到byte数组
    public static byte[] Hex2Byte(String content) {
        content = content.replaceAll(" ", "");
        ByteArrayOutputStream out = new ByteArrayOutputStream(
                content.length() >> 1);
        for (int i = 0; i <= content.length() - 2; i += 2) {
            int c = Integer.parseInt(content.substring(i, i + 2), 16);
            out.write(c & 0xff);
        }
        return out.toByteArray();
    }

    public static byte[] Int2Bytes(int i) {
        byte[] bb = new byte[4];
        bb[0] = (byte) (i >> 24);
        bb[1] = (byte) (i >> 16);
        bb[2] = (byte) (i >> 8);
        bb[3] = (byte) (i >> 0);
        return bb;
    }

    public static byte[] IP2Bytes(String ip) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        for (String b : ip.split("\\."))
            bb.put((byte) (Integer.parseInt(b) & 0xff));
        return bb.array();
    }

    /**
     * 将 long 类型的 n 转为 byte 数组，如果 len 为 4，则只返回低32位的4个byte
     *
     * @param n   需要转换的long
     * @param len 若为4，则只返回低32位的4个byte，否则返回8个byte
     * @return 转换后byte数组
     */
    public static byte[] Long2Bytes(long n, int len) {
        byte a = (byte) ((n & BYTE_4) >> 24);
        byte b = (byte) ((n & BYTE_3) >> 16);
        byte c = (byte) ((n & BYTE_2) >> 8);
        byte d = (byte) (n & BYTE_1);
        if (len == 4) {
            return new byte[]{a, b, c, d};
        }
        byte ha = (byte) (n >> 56);
        byte hb = (byte) ((n >> 48) & BYTE_1);
        byte hc = (byte) ((n >> 40) & BYTE_1);
        byte hd = (byte) ((n >> 32) & BYTE_1);
        return new byte[]{ha, hb, hc, hd, a, b, c, d};
    }

    public static byte[] Short2Bytes(short s) {
        byte[] b = new byte[2];
        b[0] = (byte) (s >> 8);
        b[1] = (byte) (s >> 0);
        return b;
    }

    /**
     * 将long的高32位清除，只保留低32位，低32位视为Unsigned Integer
     *
     * @param n 需要清除的long
     * @return 返回高32位全为0的long
     */
    public static long toUnsignedInt32(long n) {
        return n & UINT32_MAX;
    }

}
