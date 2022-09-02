package com.saki.qq.wtlogin.pack;

import io.netty.buffer.*;
import io.netty.util.*;
public class ByteWriter
{
    private ByteBuf byteBuffer;

    public ByteWriter(){
        this.byteBuffer =Unpooled.directBuffer();
    }

    public boolean isEmpty()
    {
        // TODO: Implement this method
        return this.byteBuffer.writerIndex()==0;
    }
    public void reCreate()
    {
        this.byteBuffer=Unpooled.directBuffer();
        this.byteBuffer.writerIndex(0);
    }

    public void destroy(){
        this.byteBuffer.release();
    }

    public int length()
    {
        return this.byteBuffer.writerIndex();
    }

    public byte[] getDataAndDestroy()
    {
        byte[] data = new byte[this.length()];
        this.byteBuffer.getBytes(0,data);
        this.destroy();
        return data;
    }

    public byte[] getDataAndDontDestroyAndClean()
    {
        byte[] data = new byte[this.length()];
        this.byteBuffer.getBytes(0,data);
        this.reCreate();
        return data;
    }

    public byte[] getData()
    {
        byte[] data = new byte[this.length()];
        this.byteBuffer.getBytes(0,data);
        return data;
    }

    public ByteBuf getBuf(){
        return this.byteBuffer;
    }

    private byte[] selfGetData()
    {
        byte[] data = new byte[this.length()];
        this.byteBuffer.getBytes(0,data);
        return data;
    }

    public ByteWriter writeBytes(byte... to_write)
    {
        this.byteBuffer.writeBytes(to_write);
        return this;
    }


    public ByteWriter writeBytes(String to_write)
    {
        this.writeBytes(to_write.getBytes());
        return this;
    }

    public ByteWriter writeBytes(ByteBuf to_write)
    {
        this.byteBuffer.writeBytes(to_write);
        return this;
    }

    public ByteWriter writeInt(long to_write)
    {
        this.byteBuffer.writeInt((int)to_write);
        return this;
    }
    public ByteWriter writeShort(int to_write)
    {
        this.byteBuffer.writeShort((short)to_write);
        return this;
    }

    public ByteWriter writeByte(byte to_write)
    {
        this.byteBuffer.writeByte(to_write);
        return this;
    }

    public ByteWriter rewriteShort(short to_write)
    {
        byte[] data = this.selfGetData();
        this.byteBuffer.writerIndex(0);
        this.byteBuffer.writeShort(to_write);
        this.byteBuffer.writeBytes(data);
        return this;
    }

    public ByteWriter rewriteByte(byte to_write)
    {
        byte[] data = this.selfGetData();
        this.byteBuffer.writerIndex(0);
        this.byteBuffer.writeByte(to_write);
        this.byteBuffer.writeBytes(data);
        return this;
    }

    public ByteWriter rewriteInt(int to_write)
    {
        byte[] data = this.selfGetData();
        this.byteBuffer.clear();
        this.byteBuffer.writeInt(to_write);
        this.byteBuffer.writeBytes(data);
        return this;
    }

    public ByteWriter rewriteBytes(byte[] to_write)
    {
        byte[] data = this.selfGetData();
        this.byteBuffer.writerIndex(0);
        this.byteBuffer.writeBytes(to_write);
        this.byteBuffer.writeBytes(data);
        return this;
    }

    public ByteWriter writeBytesByShortLength(byte[] to_write)
    {
        this.writeShort(to_write.length);
        this.writeBytes(to_write);
        return this;
    }

    public ByteWriter rewriteSelfShortLength(int i)
    {
        rewriteShort((short)(this.length() + i));
        return this;
    }

    public ByteWriter rewriteSelfIntLength(int i)
    {
        rewriteInt((this.length() + i));
        return this;
    }

    public ByteWriter writePb(int paramInt1, int paramInt2)
    {
        writeVarint(paramInt1, paramInt2);
        return this;
    }

    public ByteWriter writePb(int paramInt, long paramLong)
    {
        writeVarint(paramInt, paramLong);
        return this;
    }

    public ByteWriter writePb(int paramInt, String paramString)
    {
        writeLengthDelimt(paramInt, paramString.getBytes());
        return this;
    }

    public ByteWriter writePb(int paramInt, byte[] paramArrayOfByte)
    {
        writeLengthDelimt(paramInt, paramArrayOfByte);
        return this;
    }

    private void writeLengthDelimt(int paramInt, byte... paramVarArgs)
    {
        writeVarint(paramInt << 3 | 0x2);
        writeVarint(paramVarArgs.length);
        this.byteBuffer.writeBytes(paramVarArgs);
    }

    private void writeVarint(int paramInt, long paramLong)
    {
        writeVarint(paramInt << 3 | 0x0);
        writeVarint(paramLong);
    }

    private void writeVarint(long j)
    {
        long abs = Math.abs(j);
        do {
            this.byteBuffer.writeByte((byte) ((int) ((127 & abs) | 128)));
            abs >>= 7;
        } while (abs != 0);
        this.byteBuffer.readerIndex(this.byteBuffer.readableBytes()-1);
        byte y =this.byteBuffer.readByte();
        this.byteBuffer.readerIndex(0);
        this.byteBuffer.writerIndex(this.byteBuffer.readableBytes()-1);
        this.byteBuffer.writeByte(y&127);
    }
}


