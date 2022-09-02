package com.qq.taf.jce.dynamic;

public class DoubleField
  extends NumberField
{
  private double data;
  
  DoubleField(double data, int tag)
  {
    super(tag);
    this.data = data;
  }
  
  public Number getNumber()
  {
    return Double.valueOf(this.data);
  }
  
  public double get()
  {
    return this.data;
  }
  
  public void set(double n)
  {
    this.data = n;
  }
}
