package com.saki.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class Zip {
    public static final byte[] inflate(byte[] bArr) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
            deflaterOutputStream.write(bArr);
            deflaterOutputStream.finish();
            deflaterOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
			e.printStackTrace();
            return null;
        }
    }

    public static final byte[] deflate(byte[] bArr) {
        try {
            InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(bArr));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                int read = inflaterInputStream.read();
                if (read == -1) {
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(read);
            }
        } catch (IOException e) {
			e.printStackTrace();
            return null;
        }
    }
}
