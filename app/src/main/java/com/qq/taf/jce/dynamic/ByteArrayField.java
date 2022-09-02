package com.qq.taf.jce.dynamic;

public class ByteArrayField
  extends JceField
{
  private byte[] data;
  
  ByteArrayField(byte[] data, int tag)
  {
    super(tag);
    this.data = data;
  }
  
  public byte[] get()
  {
    return this.data;
  }
  
  public void set(byte[] bs)
  {
    this.data = bs;
  }
}
