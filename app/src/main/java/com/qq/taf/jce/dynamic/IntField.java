package com.qq.taf.jce.dynamic;

public class IntField
  extends NumberField
{
  private int data;
  
  IntField(int data, int tag)
  {
    super(tag);
    this.data = data;
  }
  
  public int get()
  {
    return this.data;
  }
  
  public void set(int n)
  {
    this.data = n;
  }
  
  public Number getNumber()
  {
    return Integer.valueOf(this.data);
  }
}
