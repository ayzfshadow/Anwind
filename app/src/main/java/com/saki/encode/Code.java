package com.saki.encode;

import com.saki.QVMProtect;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.zip.CRC32;

@QVMProtect
public class Code {
    public static final byte[] DEFAULT_KEY = new byte[16];

	public static long randomLong()
	{
        return (long) (1000000000 + Math.random() * (1500000000 - 1000000000 + 1));
	}
	
		
	
    public static int[] Byte2Int(byte[] data) {
        if (data.length % 4 != 0)
            return new int[]{};
        int[] i = new int[data.length >> 2];
        for (int p = 0; p < i.length; p++) {
            i[p] = ((data[p * 4] & 0xff) << 24)
                    | ((data[p * 4 + 1] & 0xff) << 16)
                    | ((data[p * 4 + 2] & 0xff) << 8) | data[p * 4 + 3] & 0xff;
        }
        return i;
    }

    public static long Crc(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }

    public static byte[] Int2Byte(int[] data) {
        byte b[] = new byte[data.length << 2];
        for (int i = 0; i < data.length; i++) {
            b[i * 4 + 0] = (byte) (data[i] >> 24);
            b[i * 4 + 1] = (byte) (data[i] >> 16);
            b[i * 4 + 2] = (byte) (data[i] >> 8);
            b[i * 4 + 3] = (byte) (data[i] >> 0);
        }
        return b;
    }

    public static byte[] MD5(byte[] bs) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bs);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return new byte[]{};
    }
    public static byte[] QQTEAdecrypt(byte in[],byte[] key){
        return new TeaCryptor().decrypt(in,key);
    }
    
    public static byte[] QQTEAencrypt(byte[] in,byte[] key){
        return new TeaCryptor().encrypt(in,key);
    }
    
}
