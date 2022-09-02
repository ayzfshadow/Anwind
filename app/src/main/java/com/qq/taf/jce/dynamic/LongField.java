package com.qq.taf.jce.dynamic;

public class LongField
  extends NumberField
{
  private long data;
  
  LongField(long data, int tag)
  {
    super(tag);
    this.data = data;
  }
  
  public Number getNumber()
  {
    return Long.valueOf(this.data);
  }
  
  public long get()
  {
    return this.data;
  }
  
  public void set(long n)
  {
    this.data = n;
  }
}
