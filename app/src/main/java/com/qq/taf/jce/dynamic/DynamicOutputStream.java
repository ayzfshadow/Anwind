package com.qq.taf.jce.dynamic;

import com.qq.taf.jce.JceDecodeException;
import com.qq.taf.jce.JceOutputStream;
import java.nio.ByteBuffer;

public final class DynamicOutputStream
  extends JceOutputStream
{
  public DynamicOutputStream(ByteBuffer bs)
  {
    super(bs);
  }
  
  public DynamicOutputStream(int capacity)
  {
    super(capacity);
  }
  
  public DynamicOutputStream() {}
  
  public void write(JceField field)
  {
    int tag = field.getTag();
    if ((field instanceof ZeroField))
    {
      write(0, tag);
    }
    else if ((field instanceof IntField))
    {
      write(((IntField)field).get(), tag);
    }
    else if ((field instanceof ShortField))
    {
      write(((ShortField)field).get(), tag);
    }
    else if ((field instanceof ByteField))
    {
      write(((ByteField)field).get(), tag);
    }
    else if ((field instanceof StringField))
    {
      write(((StringField)field).get(), tag);
    }
    else if ((field instanceof ByteArrayField))
    {
      write(((ByteArrayField)field).get(), tag);
    }
    else
    {
      JceField[] arrayOfJceField;
      int j;
      if ((field instanceof ListField))
      {
        ListField lf = (ListField)field;
        reserve(8);
        writeHead((byte)9, tag);
        write(lf.size(), 0);
        j = (arrayOfJceField = lf.get()).length;
        for (int i = 0; i < j; i++)
        {
          JceField jf = arrayOfJceField[i];
          write(jf);
        }
      }
      else
      {
        int i;
        if ((field instanceof MapField))
        {
          MapField mf = (MapField)field;
          reserve(8);
          writeHead((byte)8, tag);
          int ns = mf.size();
          write(ns, 0);
          for (i = 0; i < ns; i++)
          {
            write(mf.getKey(i));
            write(mf.getValue(i));
          }
        }
        else if ((field instanceof StructField))
        {
          StructField sf = (StructField)field;
          reserve(2);
          writeHead((byte)10, tag);
          j = (arrayOfJceField = sf.get()).length;
          for (i = 0; i < j; i++)
          {
            JceField jf = arrayOfJceField[i];
            write(jf);
          }
          reserve(2);
          writeHead((byte)11, 0);
        }
        else if ((field instanceof LongField))
        {
          write(((LongField)field).get(), tag);
        }
        else if ((field instanceof FloatField))
        {
          write(((FloatField)field).get(), tag);
        }
        else if ((field instanceof DoubleField))
        {
          write(((DoubleField)field).get(), tag);
        }
        else
        {
          throw new JceDecodeException("unknow JceField type: " + field.getClass().getName());
        }
      }
    }
  }
}
