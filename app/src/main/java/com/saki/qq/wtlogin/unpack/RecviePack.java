package com.saki.qq.wtlogin.unpack;

import com.saki.tools.Zip;

public abstract class RecviePack {
    protected byte[] data;
    private byte[] allData;
    public Info info;

    public RecviePack(byte[] data) {
        this.data = data;
    }

    public byte[] checkZip(byte[] bArr) {
        if (bArr.length < 2) {
            return bArr;
        }
        if (bArr[0] == 120 && bArr[1] == -38) {
            return Zip.deflate(bArr);
        }
        if (bArr[0] != 1 || bArr[1] != 120) {
            return bArr;
        }
        byte[] bArr2 = new byte[(bArr.length - 1)];
        System.arraycopy(bArr, 1, bArr2, 0, bArr2.length);
        return Zip.deflate(bArr2);
    }

    public byte[] getData() {
        return allData;
    }

    public RecviePack setData(byte[] data) {
        this.allData = data;
        return this;
    }

    public RecviePack setInfo(Info info) {
        this.info = info;
        return this;
    }
}
