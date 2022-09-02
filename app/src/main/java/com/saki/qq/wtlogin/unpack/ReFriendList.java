package com.saki.qq.wtlogin.unpack;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceStruct;
import com.saki.encode.ByteReader;
import com.saki.qq.wtlogin.pack.jce.MemberList;
import com.saki.qq.wtlogin.pack.jce.RequestPacket;
import com.saki.qq.wtlogin.pack.jce.TroopList;
import java.util.HashMap;
import java.util.Iterator;
import com.saki.encode.Canvart;

public class ReFriendList {

    public static class GetFriendGroupList extends RecviePack {
        public GetFriendGroupList(byte[] data) {
            super(data);
        }

    }

    public static class GetTroopListReqV2 extends RecviePack
	{
        public TroopList list = new TroopList();

        public GetTroopListReqV2(byte[] bArr)
		{
            super(bArr);

            ByteReader classd = new ByteReader(bArr);
            int length = (int) classd.readUnsignedInt();
            //Log.d("length",length+"    "+classd.length());

            if (length == classd.length())
			{
				byte[]o=classd.readRestBytes();
				System.out.println(Canvart.Bytes2Hex(checkZip(o)));
                JceInputStream classd2 = new JceInputStream(checkZip(o));
                com.saki.qq.wtlogin.pack.jce.RequestPacket classd3 = new com.saki.qq.wtlogin.pack.jce.RequestPacket();
                classd3.readFrom(classd2);
                JceInputStream classd4 = new JceInputStream(classd3.sBuffer);
                HashMap<String,byte[]> hashMap = new HashMap<String,byte[]>();
                hashMap.put("", new byte[0]);
                HashMap<String,byte[]> hashMap2 = classd4.readMap(hashMap, 0, true);
				TroopList u =null;
                for (String str : hashMap2.keySet())
				{
                    this.list = (TroopList) new JceInputStream(hashMap2.get(str)).read((JceStruct) this.list, 0, true);
                }
            }
            classd.destroy();
        }
    }

    public static class GetTroopMemberList extends RecviePack {
        public MemberList list = new MemberList();

        public GetTroopMemberList(byte[] data) {
            super(data);
            ByteReader unpack = new ByteReader(data);
            long len = unpack.readInt();
            if (len == unpack.length()) {
                JceInputStream in = new JceInputStream(
                        checkZip(unpack.readRestBytes()));
                RequestPacket p = new RequestPacket();
                p.readFrom(in);
                in = new JceInputStream(p.sBuffer);
                HashMap<String, byte[]> map = new HashMap<String, byte[]>();
                map.put("", new byte[0]);
                map = (HashMap<String, byte[]>) in.readobj(map, 0, true);
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    byte[] bin = map.get(key);
                    // System.out.println(key + " : " + Canvart.Bytes2Hex(bin));
                    list = (MemberList) new JceInputStream(bin).read(list, 0,
                            true);
                }
            }
        }

    }

}
