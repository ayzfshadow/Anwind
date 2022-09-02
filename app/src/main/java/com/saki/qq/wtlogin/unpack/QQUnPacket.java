package com.saki.qq.wtlogin.unpack;

import com.saki.encode.ByteReader;
import com.saki.encode.Canvart;
import com.saki.encode.Code;
import io.netty.buffer.ByteBuf;
import com.saki.qq.global.Key;

public class QQUnPacket {

    private static RecviePack cmdUnpacket(String cmd, byte[] bin) {
		System.out.println("package: "+cmd);
        try {
            if (cmd.equals("wtlogin.login")) {
                return new ReServer(bin);
            }
            if (cmd.equals("GrayUinPro.Check")) {
                return new ReGrayUinPro(bin);
            }
            if (cmd.equals("OidbSvc.0x59f")) {
                return new ReOidSvc.Ox59f(bin);
            }
            if (cmd.equals("OidbSvc.0x496")) {
                return new ReOidSvc.Ox496(bin);
            }
            if (cmd.equals("OidbSvc.0x7c4_0")) {
                return new ReOidSvc.Ox7c4_0(bin);
            }
            if (cmd.equals("OidbSvc.0x7a2_0")) {
                return new ReOidSvc.Ox7a2_0(bin);
            }
            if (cmd.equals("StatSvc.SimpleGet")) {
                return new ReStatSvc.SimpleGet(bin);
            }
            if (cmd.equals("StatSvc.register")) {
                return new ReStatSvc.Register(bin);
            }
            if (cmd.equals("ConfigPushSvc.PushReq")) {
                return new ReConfigPushSvc.PushReq(bin);
            }
            if (cmd.equals("ConfigPushSvc.PushDomain")) {
                return new ReConfigPushSvc.PushDomain(bin);
            }
            if (cmd.equals("friendlist.GetTroopListReqV2")) {
                return new ReFriendList.GetTroopListReqV2(bin);
            }
            if (cmd.equals("friendlist.getFriendGroupList")) {
                return new ReFriendList.GetFriendGroupList(bin);
            }
            if (cmd.equals("friendlist.getTroopMemberList")) {
                return new ReFriendList.GetTroopMemberList(bin);
            }
            // if (cmd.equals("PhSigLcId.Check")) {
            // return new RePhSigLcId.Check(bin);
            // }
            if (cmd.equals("OnlinePush.PbPushTransMsg")) {
                return new ReOnlinePush.PbPushTransMsg(bin);
            }
            if (cmd.equals("OnlinePush.PbPushGroupMsg")) {
                return new ReOnlinePush.PbPushGroupMsg(bin);
            }
            if (cmd.equals("OnlinePush.PbPushDisMsg")) {
                return new ReOnlinePush.PbPushDisMsg(bin);
            }
            if (cmd.equals("OnlinePush.ReqPush")) {
                return new ReOnlinePush.ReqPush(bin);
            }
            if (cmd.equals("MessageSvc.PushNotify")) {
                return new ReMessageSvc.PbshNotify(bin);
            }
            if (cmd.equals("MessageSvc.PushForceOffline")) {
                return new ReMessageSvc.PushForceOffline(bin);
            }
            if (cmd.equals("MessageSvc.PbGetMsg")) {
                return new ReMessageSvc.PbGetMsg(bin);
            }
            if (cmd.equals("ProfileService.Pb.ReqSystemMsgNew.Group")) {
                return new ReProfileService.Pb.ReqSystemMsgNew.Group(bin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ReUnHandled(bin);
    }

    public static RecviePack unPacket(ByteBuf bArr) throws UnknownPacketException
	{
		ByteReader com_saki_a_classd = new ByteReader(bArr);
        byte[] com_saki_a_classd1 = com_saki_a_classd.readRestBytes();
		//Log.d("recv",HexTool.a(com_saki_a_classd1));
        com_saki_a_classd.readerIndex(0);
		Info a = getInfo(com_saki_a_classd, Key.inUseKey());
		if (a == null)
		{
			throw new UnknownPacketException();
		}
//		DebugLoger.log(a, "---------------" + a.cmd + "---------------");
//		//DebugLoger.log(a, com.saki.tool.HexTool.a(com_saki_a_classd.readRestBytes()));
//		DebugLoger.log(a, "---------------------end--------------------");
//		 
		RecviePack receivepack = cmdUnpacket(a.cmd, com_saki_a_classd.readRestBytesAndDestroy()).setInfo(a);
        receivepack.setData(com_saki_a_classd1);
        return receivepack;
	}

    public static Info getInfo(ByteReader reader, byte[] key)
	{

        reader.readBytes(8);
        int type = reader.readBytes(1)[0];
//      int index = buf.readerIndex();
//      buf.readerIndex(0);
//      byte[] g = new byte[buf.readableBytes()];
//      buf.readBytes(g);
		
//      buf.readerIndex(index);

        reader.readBytes(1);//0
        reader.readBytes((int)reader.readInt() - 4);//qq长度+4+qq

        byte[] data = reader.readRestBytes();
        if (data.length % 8 != 0)
        {
            return null;//包体长度不是8的解密不出来的，丢弃这个包
		}
        
        byte[] data_decrypted = Code.QQTEAdecrypt(data, key);
        if (data_decrypted == null || data_decrypted.length == 0)
        {
            data_decrypted = Code.QQTEAdecrypt(data, new byte[16]);
        }
        if (data_decrypted == null || data_decrypted.length == 0)
        {
			
            return null;
        }
        System.out.println(Canvart.Bytes2Hex(data_decrypted));
        reader.update(data_decrypted);
		//Log.d("decrypted",HexTool.a(data_decrypted));
        return new Info(reader);
	}
	
}
