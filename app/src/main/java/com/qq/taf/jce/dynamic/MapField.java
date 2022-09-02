package com.qq.taf.jce.dynamic;

public final class MapField
  extends JceField
{
  private JceField[] keys;
  private JceField[] values;
  
  MapField(JceField[] keys, JceField[] values, int tag)
  {
    super(tag);
    this.keys = keys;
    this.values = values;
  }
  
  public JceField[] getKeys()
  {
    return this.keys;
  }
  
  public JceField[] getValues()
  {
    return this.values;
  }
  
  public int size()
  {
    return this.keys.length;
  }
  
  public JceField getKey(int n)
  {
    return this.keys[n];
  }
  
  public JceField getValue(int n)
  {
    return this.values[n];
  }
  
  public void setKey(int n, JceField field)
  {
    this.keys[n] = field;
  }
  
  public void setValue(int n, JceField field)
  {
    this.values[n] = field;
  }
}
