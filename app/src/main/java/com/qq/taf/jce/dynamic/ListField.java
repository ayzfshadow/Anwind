package com.qq.taf.jce.dynamic;

public final class ListField
  extends JceField
{
  private JceField[] data;
  
  ListField(JceField[] data, int tag)
  {
    super(tag);
    this.data = data;
  }
  
  public JceField[] get()
  {
    return this.data;
  }
  
  public JceField get(int n)
  {
    return this.data[n];
  }
  
  public void set(int n, JceField field)
  {
    this.data[n] = field;
  }
  
  public void set(JceField[] fields)
  {
    this.data = fields;
  }
  
  public int size()
  {
    return this.data.length;
  }
}
