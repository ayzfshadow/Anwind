package com.saki.service;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class IOData
{

	
    public class Data {
        public int type;
        public byte[] data;
    }

    public static final int ACCOUNT = 0;
    public static final int VERIFFYCODE = 1;
    public static final int KEY = 2;
    public static final int SEQ = 3;
    public static final int TOKEN = 4;
    public static final int MSG = 5;
	
    public static final int PACK = 6;
	public static final int Lock = 11;
	public static final int LockCode = 12;
	public static final int LockFalse = 13;
    public static final int ERROR = -1;
    ByteArrayInputStream in;

    DataInputStream din;

    public IOData(byte[] bin) {
        in = new ByteArrayInputStream(bin);
        din = new DataInputStream(in);
    }

    public Data next() {
        try {
            Data data = new Data();
            data.type = din.readInt();
            int len = din.readInt();
            data.data = new byte[len];
            din.readFully(data.data);
            return data;
        } catch (IOException e) {
            return null;
        }
    }
}
