package com.setqq.free;

import java.io.*;
import java.util.*;

public class Msg implements/* Externalizable*/java.io.Serializable
{
	private static final long serialVersionUID = 202107202113L;

    public static final int TYPE_GROUP_MSG = 0;// 群消息
    public static final int TYPE_BUDDY_MSG = 1;// 好友消息
    public static final int TYPE_DIS_MSG = 2;// 讨论组消息
    public static final int TYPE_SESS_MSG = 4;// 临时消息
    public static final int TYPE_SYS_MSG = 3;// 系统消息
    public static final int TYPE_GET_GROUP_LIST = 5;// 群列表
    public static final int TYPE_GET_GROUP_INFO = 6;// 群信息
    public static final int TYPE_GET_GROUP_MEBMER = 7;// 群成员
    public static final int TYPE_GET_MEMBER_INFO = 14;// 成员信息
    public static final int TYPE_FAVORITE = 8;// 点赞
    public static final int TYPE_SET_MEMBER_CARD = 9;// 设置群名片
    public static final int TYPE_SET_MEMBER_SHUTUP = 10;// 成员禁言
    public static final int TYPE_SET_GROUP_SHUTUP = 11;// 群禁言
    public static final int TYPE_DELETE_MEMBER = 12;// 删除群成员
    public static final int TYPE_AGREE_JOIN = 13;// 同意入群
    public static final int TYPE_GET_LOGIN_ACCOUNT = 15;// 获取机器人QQ
    public static final int TYPE_MEMBER_DELETE = 16;// 退群
    public static final int TYPE_ADMIN_CHANGE = 17;// 管理员变更
    public static final int TYPE_STOP = 18;
    public int type = -1;
    public long groupid = -1;
    public long code = -1;
    public long uin = -1;
    public long time = -1;
    public int value = -1;
    public String groupName;
    public String uinName;
    public String title;
    public HashMap<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();

    public Msg()
    {
    }
    public Msg(int type)
    {
        this.type=type;
    }
    public void addMsg(String key,String msg)
    {
        ArrayList<String> index = data.get("index");
        if (index == null)
        {
            index=new ArrayList<String>();
            data.put("index",index);
        }
        index.add(key);
        ArrayList<String> list = data.get(key);
        if (list == null)
        {
            list=new ArrayList<String>();
            data.put(key,list);
        }
        list.add(msg);
    }

    public void clearMsg()
    {
        data=new HashMap<String, ArrayList<String>>();
    }

    public HashMap<String, ArrayList<String>> getData()
    {
        return data;
    }

    public ArrayList<String> getMsg(String key)
    {
        return data.get(key);
    }

    public String getTextMsg()
    {
        StringBuilder build = new StringBuilder("");
        ArrayList<String> list = data.get("msg");
        if (list != null)
        {
            for (String m : list)
                build.append(m);
        }
        return build.toString();
    }

	/*
	 public void readFromParcel ( Parcel source )
	 {
	 type = source.readInt ( );
	 groupid = source.readLong ( );
	 code = source.readLong ( );
	 uin = source.readLong ( );
	 time = source.readLong ( );
	 value = source.readInt ( );
	 groupName = source.readString ( );
	 uinName = source.readString ( );
	 title = source.readString ( );
	 source.readMap ( data, getClass ( ).getClassLoader ( ) );
	 }  
	 */

    public void setData(HashMap<String, ArrayList<String>> data)
    {
        if (data != null)
        {
            this.data=data;
        }
    }

    /*

     @Override
     public void writeExternal ( ObjectOutput objectOutput ) throws IOException
     {
     objectOutput.writeInt ( type );
     objectOutput.writeLong ( groupid );
     objectOutput.writeLong ( code );
     objectOutput.writeLong ( uin );
     objectOutput.writeLong ( time );
     objectOutput.writeInt ( value );
     objectOutput.writeUTF ( groupName );
     objectOutput.writeUTF ( uinName );
     objectOutput.writeUTF ( title );
     objectOutput.writeObject ( data );
     }

     @Override
     public void readExternal ( ObjectInput objectInput ) throws IOException, ClassNotFoundException
     {
     type = objectInput.readInt ( );
     groupid = objectInput.readLong ( );
     code = objectInput.readLong ( );
     uin = objectInput.readLong ( );
     value = objectInput.readInt ( );
     groupName = objectInput.readUTF ( );
     uinName = objectInput.readUTF ( );
     title = objectInput.readUTF ( );
     data = (HashMap<String, ArrayList<String>>) objectInput.readObject ( );
     }

     */


    @Override
    public String toString()
    {
        return groupName + "[" + groupid + "]" + uinName + "[" + uin + "]" + code + ":" + getTextMsg();
    }
}
