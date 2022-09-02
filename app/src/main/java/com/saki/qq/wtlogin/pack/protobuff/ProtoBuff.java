package com.saki.qq.wtlogin.pack.protobuff;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.saki.encode.Canvart;

public class ProtoBuff {

    class HeadData {
        int type;
        int tag;
    }

    public static void main(String[] args) {
        byte[] bin = Canvart.Hex2Byte("0A E7 01"
                + "0A 6A 08 DA AD B7 87 05 10 CC F5 F3 BA 05 18 52"
                + "20 00 28 E5 B9 56 30 D1 85 C7 C7 05 38 D2 80 B8"
                + "85 80 80 80 80 02 4A 3B 08 F8 CC C0 6C 10 01 18"
                + "D3 EE 01 22 0F E5 8D 81 E5 85 AD E5 A4 9C E5 92"
                + "B2 E5 A4 9C 30 05 38 01 42 19 E5 AF B9 E9 BE 99"
                + "E7 89 B9 E6 AE 8A E9 83 A8 E9 98 9F 2D E8 90 9D"
                + "E6 9D 80 50 01 58 01 60 00 88 01 10 12 06 08 01"
                + "10 00 18 00 1A 71 0A 6F 0A 21 08 00 10 D0 85 C7"
                + "C7 05 18 C6 FB D0 C0 04 20 00 28 0A 30 00 38 86"
                + "01 40 02 4A 06 E5 AE 8B E4 BD 93 12 07 0A 05 0A"
                + "03 E4 BA BA 12 07 4A 05 08 D6 03 40 01 12 25 82"
                + "01 22 12 0F E5 8D 81 E5 85 AD E5 A4 9C E5 92 B2"
                + "E5 A4 9C 18 05 20 10 28 01 3A 09 E5 A5 B3 E4 BB"
                + "86 E9 95 BF 12 11 AA 02 0E 50 00 60 21 68 00 9A"
                + "01 05 08 06 20 BF 50 10 8A 8C AF EC FC FF FF FF" + "FF 01 ");
        ProtoBuff pb = new ProtoBuff(bin);
        byte[] b = pb.readLD(1);
        pb = new ProtoBuff(b);
        b = pb.readLD(1);
        pb = new ProtoBuff(b);
        long l = pb.readVarint(17);
        System.out.println(l);
        System.out.println(Canvart.Bytes2Hex(b));
    }

    public static byte[] writeLengthDelimt(int tag, byte... data) {
        byte[] l = writeVarint(data.length);
        byte[] key = writeVarint(tag << 3 | 2);
        byte[] bin = new byte[data.length + key.length + l.length];
        System.arraycopy(key, 0, bin, 0, key.length);
        System.arraycopy(l, 0, bin, key.length, l.length);
        System.arraycopy(data, 0, bin, l.length + key.length, data.length);
        return bin;
    }

    public static byte[] writeVarint(int field_number, long value) {
        byte[] bin = writeVarint(value);
        byte[] key = writeVarint(field_number << 3 | 0);
        byte[] data = new byte[bin.length + key.length];
        System.arraycopy(key, 0, data, 0, key.length);
        System.arraycopy(bin, 0, data, key.length, bin.length);
        return data;
    }

    public static byte[] writeVarint(long value) {
        value = Math.abs(value);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        do {
            out.write((byte) ((value & 0x7F) | 0x80));
        } while ((value >>= 7) != 0);
        byte[] data = out.toByteArray();
        data[data.length - 1] &= 0x7F;
        return data;
    }

    public static long Zigzag(long n) {
        return (n << 1) ^ (n >> 31);
    }

    public ByteBuffer bs;

    public ProtoBuff(byte[] bin) {
        bs = ByteBuffer.wrap(bin);
    }

    private long Byte2Long(byte... bin) {
        String s = "";
        for (int i = bin.length - 1; i >= 0; i--) {
            s += deleteHightOnebit(bin[i]);
        }
        return new BigInteger(s, 2).longValue();
    }

    private String deleteHightOnebit(byte b) {
        return (b >> 6 & 0x1) + "" + (b >> 5 & 0x1) + "" + (b >> 4 & 0x1) + ""
                + (b >> 3 & 0x1) + "" + (b >> 2 & 0x1) + "" + (b >> 1 & 0x1)
                + "" + (b & 0x1);
    }

    private boolean hasMoreByte(byte b) {
        return ((b >> 7) & 0x1) == 1;
    }

    private byte[] read2End() {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        byte b;
        while (hasMoreByte(b = bs.get())) {
            o.write(b);
        }
        o.write(b);
        return o.toByteArray();
    }

    private HeadData readHead() {
        HeadData hd = new HeadData();
        long b = Byte2Long(read2End());
        hd.type = (int) (b & 7);
        hd.tag = (int) (b >> 3);
        return hd;
    }

    public byte[] readLD(int tag) {
        int i = this.bs.position();
        if (skipTag(tag)) {
            int len = (int) Byte2Long(read2End());
            return readLen(len);
        }
        this.bs.position(i);
        return null;
    }

    private byte[] readLen(int len) {
        byte[] b = new byte[len];
        bs.get(b);
        return b;
    }

    public long readVarint(int tag) {
        if (skipTag(tag)) {
            return Byte2Long(read2End());
        }
        return 0;
    }

    private void skip(int len) {
        this.bs.position(bs.position() + len);
    }

    private void skipField(int type) {
        if (type == 2) {
            int len = (int) Byte2Long(read2End());
            skip(len);
        } else {
            while (hasMoreByte(bs.get()))
                ;
        }
    }

    private boolean skipTag(int tag) {
        try {
            HeadData hd = readHead();
            while (hd.tag != tag) {
                skipField(hd.type);
                hd = readHead();
            }
            return true;
        } catch (BufferUnderflowException e) {
            return false;
        }

    }

}
