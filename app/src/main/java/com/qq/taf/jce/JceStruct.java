package com.qq.taf.jce;

import java.io.Serializable;

public abstract class JceStruct
  implements Serializable
{
  public static final byte BYTE = 0;
  public static final byte SHORT = 1;
  public static final byte INT = 2;
  public static final byte LONG = 3;
  public static final byte FLOAT = 4;
  public static final byte DOUBLE = 5;
  public static final byte STRING1 = 6;
  public static final byte STRING4 = 7;
  public static final byte MAP = 8;
  public static final byte LIST = 9;
  public static final byte STRUCT_BEGIN = 10;
  public static final byte STRUCT_END = 11;
  public static final byte ZERO_TAG = 12;
  public static final byte SIMPLE_LIST = 13;
  public static final int JCE_MAX_STRING_LENGTH = 104857600;
  
  public abstract void writeTo(JceOutputStream paramJceOutputStream);
  
  public abstract void readFrom(JceInputStream paramJceInputStream);
  
  public void display(StringBuilder sb, int level) {}
  
  public void displaySimple(StringBuilder sb, int level) {}
  
  public JceStruct newInit()
  {
    return null;
  }
  
  public void recyle() {}
  
  public boolean containField(String name)
  {
    return false;
  }
  
  public Object getFieldByName(String name)
  {
    return null;
  }
  
  public void setFieldByName(String name, Object value) {}
  
  public byte[] toByteArray()
  {
    JceOutputStream os = new JceOutputStream();
    writeTo(os);
    return os.toByteArray();
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    display(sb, 0);
    return sb.toString();
  }
  
  public static String toDisplaySimpleString(JceStruct jceStruct)
  {
    if (jceStruct == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    jceStruct.displaySimple(sb, 0);
    return sb.toString();
  }
}
