package com.qq.taf.jce.dynamic;

import com.qq.taf.jce.JceDecodeException;
import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceInputStream.HeadData;
import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class DynamicInputStream
{
  private String sServerEncoding = "GBK";
  private ByteBuffer bs;
  
  public DynamicInputStream(ByteBuffer bs)
  {
    this.bs = bs;
  }
  
  public DynamicInputStream(byte[] bs)
  {
    this.bs = ByteBuffer.wrap(bs);
  }
  
  public int setServerEncoding(String se)
  {
    this.sServerEncoding = se;
    return 0;
  }
  
  public JceField read()
  {
    try
    {
      JceInputStream.HeadData hd = new JceInputStream.HeadData();
      JceInputStream.readHead(hd, this.bs);
      switch (hd.type)
      {
      case 0: 
        return JceField.create(this.bs.get(), hd.tag);
      case 1: 
        return JceField.create(this.bs.getShort(), hd.tag);
      case 2: 
        return JceField.create(this.bs.getInt(), hd.tag);
      case 3: 
        return JceField.create(this.bs.getLong(), hd.tag);
      case 4: 
        return JceField.create(this.bs.getFloat(), hd.tag);
      case 5: 
        return JceField.create(this.bs.getDouble(), hd.tag);
      case 6: {
        int len = this.bs.get();
        if (len < 0) {
          len += 256;
        }
        return readString(hd, len);
      }
      case 7: 
        return readString(hd, this.bs.getInt());
      case 9: {
        int len = ((NumberField) read()).intValue();
        JceField[] fs = new JceField[len];
        for (int i = 0; i < len; i++) {
          fs[i] = read();
        }
        return JceField.createList(fs, hd.tag);
      }
      case 8: {
        int len = ((NumberField) read()).intValue();
        JceField[] keys = new JceField[len];
        JceField[] values = new JceField[len];
        for (int i = 0; i < len; i++) {
          keys[i] = read();
          values[i] = read();
        }
        return JceField.createMap(keys, values, hd.tag);
      }
      case 10: 
        List<JceField> ls = new ArrayList();
        for (;;)
        {
          JceField f = read();
          if (f == null) {
            break;
          }
          ls.add(f);
        }
        return JceField.createStruct((JceField[])ls.toArray(new JceField[0]), hd.tag);
      case 11: 
        return null;
      case 12: 
        return JceField.createZero(hd.tag);
      case 13: 
        int tag = hd.tag;
        JceInputStream.readHead(hd, this.bs);
        if (hd.type != 0) {
          throw new JceDecodeException("type mismatch, simple_list only support byte, tag: " + tag + ", type: " + hd.type);
        }
        int len = ((NumberField)read()).intValue();
        byte[] data = new byte[len];
        this.bs.get(data);
        return JceField.create(data, tag);
      }
    }
    catch (BufferUnderflowException e)
    {
      return null;
    }
    return null;
  }
  
  private JceField readString(JceInputStream.HeadData hd, int len)
  {
    byte[] ss = new byte[len];
    this.bs.get(ss);
    String s = null;
    try
    {
      s = new String(ss, this.sServerEncoding);
    }
    catch (UnsupportedEncodingException e)
    {
      s = new String(ss);
    }
    return JceField.create(s, hd.tag);
  }
}
