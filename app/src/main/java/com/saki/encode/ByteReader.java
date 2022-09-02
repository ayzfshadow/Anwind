package com.saki.encode;



import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

public class ByteReader
{
    private ByteBufAllocator alloc = PooledByteBufAllocator.DEFAULT;

    private ByteBuf byteBuffer;

	public int readUnsignedByte()
	{
		// TODO: Implement this method
		return this.byteBuffer.readUnsignedByte();
	}

    public int readerIndex()
    {

        return this.byteBuffer.readerIndex();
    }

    public boolean hasMore()
    {
        return this.byteBuffer.readableBytes()>0;
    }

    public int restBytesCount(){
        return this.byteBuffer.readableBytes();
    }

    public int length(){
        int mark = this.byteBuffer.readerIndex();
        this.byteBuffer.readerIndex(0);
        int length = this.byteBuffer.readableBytes();
        this.byteBuffer.readerIndex(mark);
        return length;
    }

    public void readerIndex(int index)
    {
        this.byteBuffer.readerIndex(index);
    }

    public void readerIndexDown(int i){
        this.byteBuffer.readerIndex(this.byteBuffer.readerIndex()-i);
    }

    public ByteReader(byte[] _data)
    {
        this.byteBuffer = this.alloc.directBuffer();
        this.byteBuffer.writeBytes(_data);
    }

    public ByteReader update(byte[] data){
        this.byteBuffer = this.alloc.directBuffer();
        this.byteBuffer.writeBytes(data);
        return this;
    }

    public ByteReader(ByteBuf _data)
    {
        this.byteBuffer=_data;
    }

    public void destroy()
    {
        this.byteBuffer.release();
    }

    public byte[] readRestBytes()
    {
        return this.readBytes(this.byteBuffer.readableBytes());
    }

    public byte[] readRestBytesAndDestroy()
    {
        return this.readBytesAndDestroy(this.byteBuffer.readableBytes());
    }

    public String readStringByShortLength()
    {
        int length = readShort();
        return this.readString(length);
    }

    public String readStringByShortLengthAndDestroy()
    {
        int length = readShort();
        return this.readStringAndDestroy(length);
    }

    public String readString(int length)
    {
        return new String(this.readBytes(length));
    }

    public String readStringAndDestroy(int length)
    {
        String y= new String(this.readBytes(length));
        this.destroy();
        return y;
    }

    public byte[] readBytesByShortLength()
    {
        int length = readShort();
        return this.readBytes(length);
    }

    public byte[] readBytesByShortLengthAndDestroy()
    {
        int length = readShort();
        return this.readBytesAndDestroy(length);
    }

    public byte[] readBytes(int length)
    {
        byte[] data = new byte[length];
        this.byteBuffer.readBytes(data);
        return data;
    }

    public byte[] readBytesAndDestroy(int length)
    {
        byte[] data = new byte[length];
        this.byteBuffer.readBytes(data);
        this.destroy();
        return data;
    }

    public int readShort()
    {
        return this.byteBuffer.readShort();
    }

    public int readUnsignedShort()
    {
        return this.byteBuffer.readUnsignedShort();
    }

    public byte readByte()
    {
        return this.byteBuffer.readByte();
    }

    public int readShortAndDestroy()
    {
        int y = this.byteBuffer.readShort();
        this.destroy();
        return y;
    }

    public long readInt()
    {
        return this.byteBuffer.readInt();
    }

    public long readUnsignedInt()
    {
        return this.byteBuffer.readUnsignedInt();
    }

    public long readIntAndDestroy()
    {
        int y = this.byteBuffer.readInt();
        this.destroy();
        return y;
    }
}


