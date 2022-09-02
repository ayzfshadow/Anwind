package com.qq.taf.jce.dynamic;

public final class ShortField
  extends NumberField
{
  private short data;
  
  ShortField(short data, int tag)
  {
    super(tag);
    this.data = data;
  }
  
  public Number getNumber()
  {
    return Short.valueOf(this.data);
  }
  
  public short get()
  {
    return this.data;
  }
  
  public void set(short s)
  {
    this.data = s;
  }
}
