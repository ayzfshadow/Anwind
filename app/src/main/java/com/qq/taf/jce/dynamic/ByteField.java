package com.qq.taf.jce.dynamic;

public final class ByteField
  extends NumberField
{
  private byte data;
  
  ByteField(byte b, int tag)
  {
    super(tag);
    this.data = b;
  }
  
  public Number getNumber()
  {
    return Byte.valueOf(this.data);
  }
  
  public byte get()
  {
    return this.data;
  }
  
  public void set(byte n)
  {
    this.data = n;
  }
}
