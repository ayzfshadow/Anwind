package com.qq.taf.jce.dynamic;

public final class ZeroField
  extends NumberField
{
  ZeroField(int tag)
  {
    super(tag);
  }
  
  public Number getNumber()
  {
    return Integer.valueOf(0);
  }
  
  public byte byteValue()
  {
    return 0;
  }
  
  public double doubleValue()
  {
    return 0.0D;
  }
  
  public float floatValue()
  {
    return 0.0F;
  }
  
  public int intValue()
  {
    return 0;
  }
  
  public long longValue()
  {
    return 0L;
  }
  
  public short shortValue()
  {
    return 0;
  }
}
