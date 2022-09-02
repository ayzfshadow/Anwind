package com.qq.taf.jce.dynamic;

public class FloatField
  extends NumberField
{
  private float data;
  
  FloatField(float data, int tag)
  {
    super(tag);
    this.data = data;
  }
  
  public Number getNumber()
  {
    return Float.valueOf(this.data);
  }
  
  public void set(float n)
  {
    this.data = n;
  }
  
  public float get()
  {
    return this.data;
  }
}
