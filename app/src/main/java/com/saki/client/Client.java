package com.saki.client;

import com.ayzf_shadow.Activity.*;
import com.saki.*;
import com.saki.encode.*;
import com.saki.http.*;
import com.saki.log.*;
import com.saki.qq.global.*;
import com.saki.qq.wtlogin.pack.send.*;
import com.saki.qq.wtlogin.unpack.*;
import com.saki.qq.wtlogin.unpack.ReFriendList.*;
import com.saki.qq.wtlogin.unpack.ReMessageSvc.*;
import com.saki.qq.wtlogin.unpack.ReMessageSvc.PbGetMsg.*;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.*;
import com.saki.qq.wtlogin.unpack.ReProfileService.Pb.ReqSystemMsgNew.*;
import com.saki.qq.wtlogin.unpack.ReProfileService.Pb.ReqSystemMsgNew.Group.*;
import com.saki.service.*;
import com.saki.service.IOData.*;
import com.saki.tools.*;
import com.saki.ui.*;
import com.saki.ui.listener.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import org.json.*;
@QVMProtect
public class Client implements IClient
{

	
	

	public void getUnlockPacket(final String str)
	{
        pool.execute(new Runnable() {
				@Override
				public void run()
				{
					try
					{
						byte[] bin = h.call("GET", BASE_URL + "?unlockcode=" + str, null);
						doServerReturn(bin);
					}
					catch (Exception e)
					{
						service.updataUI(NewLoginActivity.CMD_LOGIN_FAILT, "网络异常");
					}
				}
			});
    }
	
	
    @QVMProtect
    public class SQ extends SocketClient
	{
        private SimpleGetThread simlpeGet;

		private boolean islogined;

        public SQ() throws UnknownHostException, IOException
		{
            super("120.241.139.239", 8080);
        }

		@Override public void channelActive(ChannelHandlerContext ctx) throws Exception
        {
            super.channelActive(ctx);
            if (islogined)
            {
				//  send(new com.saki.qq.datapacket.pack.send.OidbSvc.Ox59f(Client.this.b()).toByteArray());
				Key.changeKey(Key.KDEFAULT);
				send(new GrayUinPro.Check(getRequstionID()).toByteArray());
				//send(new OidbSvc.Ox59f(getRequstionID()).toByteArray());
		    }
        }

        @Override
        public void doReceive(ByteBuf byteArray)
		{
            RecviePack pack;
            try
			{
                pack = QQUnPacket.unPacket(byteArray);
                // 未处理的数据包
                if (pack instanceof ReUnHandled)
				{
                    Log.i(this, "read unHandler");
                    Log.i(this, Canvart.Bytes2Hex(pack.getData()));
                    return;
                }
                // 服务器数据包
                if (pack instanceof ReServer)
				{
                    doServerReturn(post2Server(pack.getData()));
                    return;
                }
                // 离线数据包
                if (pack instanceof ReMessageSvc.PushForceOffline)
				{

                }
				else
                // 其他消息数据包，好友消息，临时消息，系统消息
                if (pack instanceof PbGetMsg)
				{
                    // 其他消息
                    for (Msg m : ((PbGetMsg) pack).msgList)
					{
                        if (m.type == PbGetMsg.T_SYS_UNREAD
							|| m.type == PbGetMsg.T_JOIN_GRUP
							|| m.type == PbGetMsg.T_SYS_INVIT)
                            send(new ProfileService.Pb.ReqSystemMsgNew.Group(
									 getRequstionID()).toByteArray());
                    }
                    iMessage.onOtherMsg((PbGetMsg) pack);
                }
				else
                // 新的系统消息
                if (pack instanceof Group)
				{
                    Log.i(this, "系统消息");
                    Group g = (Group) pack;
                    send(new ProfileService.Pb.ReqSystemMsgRead.Group(
							 getRequstionID(), g.requstId).toByteArray());
                    for (SysMsg m : g.list)
					{
                        Log.i(this, m.toString());
                        if (m.type == Group.UNREAD)
						{
                            iMessage.onSystemMsg(m);
                        }
                    }
                }
				else
                // 消息推送
                if (pack instanceof ReMessageSvc.PbshNotify)
				{
                    send(new MessageSvc.PbGetMsg(getRequstionID())
						 .toByteArray());
                }
				else if (pack instanceof ReStatSvc.Register)
				{
					islogined = true;
					SQLog.i("上线成功");
					if (simlpeGet == null)
					{
                        simlpeGet = new SimpleGetThread(Client.this);
                        super.ctx.executor().scheduleAtFixedRate(simlpeGet, 0, 100, TimeUnit.SECONDS);
						service.updataUI(NewLoginActivity.CMD_LOGIN_SUCCESS, null);
                    }
                    
					
				}
				else if (pack instanceof /* ReStatSvc.Register){// */ ReConfigPushSvc.PushDomain)
				{
                    
                }
				else
                // 状态变化消息
                if (pack instanceof PbPushTransMsg)
				{
                    PbPushTransMsg m = (PbPushTransMsg) pack;
                    send(new OnlinePush.RespPush(getRequstionID(), m.requstId,
												 null).toByteArray());
                    iMessage.onTransMsg((PbPushTransMsg) pack);
                }
				else
                // 消息推送
                if (pack instanceof ReqPush)
				{
                    ReqPush p = (ReqPush) pack;
                    send(new OnlinePush.RespPush(getRequstionID(), p.requstid,
												 p.listPack.list).toByteArray());
                }
				else
                // 讨论组消息
                if (pack instanceof PbPushDisMsg)
				{

                    send(new OnlinePush.RespPush(getRequstionID(),
												 pack.info.seq, null).toByteArray());
                    iMessage.onDisMsg((PbPushDisMsg) pack);
                }
				else
                // 群消息
                if (pack instanceof PbPushGroupMsg)
				{
                    iMessage.onGroupMsg((PbPushGroupMsg) pack);
                }
				else
                // 获取群列表
                if (pack instanceof ReFriendList.GetTroopListReqV2)
				{
                    iTroop.onTroop(((ReFriendList.GetTroopListReqV2) pack).list.list);
                }
				else
                // 获取好友列表
                if (pack instanceof ReFriendList.GetFriendGroupList)
				{

                }
				else
                // 获取群成员列表
                if (pack instanceof GetTroopMemberList)
				{
                    GetTroopMemberList members = (GetTroopMemberList) pack;
                    iTroop.onTroopMember(members.list.groupid,
										 members.list.list);
                }
				else if (pack instanceof ReOidSvc.Ox59f)
				{
                    send(new OidbSvc.Ox496(getRequstionID()).toByteArray());
                }
				else if (pack instanceof ReOidSvc.Ox496)
				{
                    send(new OidbSvc.Ox7c4_0(getRequstionID()).toByteArray());
                }
				else if (pack instanceof ReOidSvc.Ox7c4_0)
				{
                    send(new OidbSvc.Ox7a2_0(getRequstionID()).toByteArray());
                }
				else if (pack instanceof ReOidSvc.Ox7a2_0)
				{
                    send(new StatSvc.Register(getRequstionID()).toByteArray());
                }
				else if (pack instanceof ReGrayUinPro)
				{
                    Key.changeKey(Key.KSESSION);
                    send(new OidbSvc.Ox59f(getRequstionID()).toByteArray());
                }
            }
			catch (UnknownPacketException e)
			{
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }

        }

    }

    public static final String BASE_URL = AuthorityActivity.server+"ServerV9.php";
    private IMessageListener iMessage = MsgManager.getInstance();
    private ITroopListener iTroop = TroopManager.getInstance();
    private ExecutorService pool = Executors.newCachedThreadPool();
    private int seq;
    private SocketClient socket;
    private NewService service;
    Http h = new Http();

    public Client(NewService service)
	{
        this.service = service;
        pool.execute(new Runnable() {
				@Override
				public void run()
				{
					try
					{
						socket = new SQ();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			});
    }
    public void releseSocket()
	{
        //this.socket.destroy();
    }
    public void doServerReturn(byte[] bin)
	{
        if (bin == null || bin.length == 0)
		{
            service.updataUI(NewLoginActivity.CMD_LOGIN_FAILT, "服务器解包失败");
            return;
        }
        // 创建解析类
        IOData data = new IOData(bin);
        Data d = null;
        // 遍历结果
        while ((d = data.next()) != null)
		{
            if (d.type == IOData.VERIFFYCODE)
			{
                // 需要验证码
                service.updataUI(NewLoginActivity.CMD_VERIFYCODE, d.data);
            }
			else if (d.type == IOData.ERROR)
			{
                // 错误消息
                service.updataUI(NewLoginActivity.CMD_LOGIN_FAILT, new String(d.data).replaceAll("[^\u0020-\u9FA5]", ""));
            }
			else if (d.type == IOData.TOKEN)
			{
                if (d.data.length == 0x40)
				{
                    Token.T0044 = d.data;
                }
				else if (d.data.length == 0x48)
				{
                    Token.T004C = d.data;
                }
				send(new GrayUinPro.Check(getRequstionID()).toByteArray());
				
            }
			else if (d.type == IOData.Lock)
			{
                service.updataUI(NewLoginActivity.CMD_VERIFYLOCK, d.data);
				
            }
			else if (d.type == IOData.LockCode)
			{
                service.updataUI(NewLoginActivity.CMD_VERIFYLOCKCODE, d.data);

            }
			else if (d.type == IOData.LockFalse)
			{
			service.updataUI(NewLoginActivity.CMD_VERIFYLOCKFALSE, d.data);

            }
			else if (d.type == IOData.SEQ)
			{
                seq = Canvart.Bytes2Int(d.data);
            }
			else if (d.type == IOData.KEY)
			{
                Key.KSESSION = d.data;
				

            }
			else if (d.type == IOData.PACK)
			{
                send(d.data);
            }
			else if (d.type == IOData.MSG)
			{
                service.updataUI(NewLoginActivity.CMD_OTHER, new String(d.data));
            }
        }
    }
  

    @Override
    public int getRequstionID()
	{
        return seq++;
    }

    public void login(final String id, final String password, final int version)
	{
        pool.execute(new Runnable() {
				@Override
				public void run()
				{
					try
					{
						String path = service.getPackageCodePath();
						byte[] bin = FileUtil.readFile(new File(path));
						byte[] key = Code.MD5(bin);
						byte[] psdData = Code.QQTEAencrypt(password.getBytes(), key);
						bin = h.call("GET", BASE_URL + "?ver=" + version + "&id=" + id + "&password="
									 + Canvart.Bytes2Hex(psdData, "")+"&deviceInfo="+getdiviceInfo(), null);
						doServerReturn(bin);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						service.updataUI(NewLoginActivity.CMD_LOGIN_FAILT, "网络异常 "+e.getMessage());
					}
				}
			});
    }

	
	
	
	private String getdiviceInfo() throws JSONException
	{
		JSONObject js = new JSONObject();
		js.put("display",Device.display );
		js.put("product",Device.product );
		js.put("device",Device.device );
		js.put("board",Device.board );
		js.put("brand",Device.brand );
		js.put("model",Device.model );
		js.put("bootloader",Device.bootloader );
		js.put("fingerprint",Device.fingerprint );
		js.put("bootId",Device.bootId );
		js.put("procVersion",Device.procVersion );
		js.put("baseBand",Device.baseBand );
		js.put("simInfo",Device.simInfo );
		js.put("osType",Device.osType );
		js.put("macAddress",Device.macAddress );
		js.put("wifiBSSID",Device.wifiBSSID );
		js.put("wifiSSID",Device.wifiSSID);
		js.put("imsiMd5",Canvart.Bytes2Hex(Device.imsiMd5,""));
		js.put("imei",Device.imei);
		js.put("apn",Device.apn);
		js.put("androidId",Device.androidId );
		js.put("codename",Device.codename );
		js.put("incremental",Device.incremental );
		js.put("innerVersion",Device.innerVersion );
		js.put("osVersion",Device.osVersion );
		return Canvart.Bytes2Hex(js.toString().getBytes(),"");
	}
	
	
    private byte[] post2Server(byte[] bin)
	{
        try
		{
            return h.call("POST", BASE_URL, bin);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void send(final byte[] bin)
	{
        pool.execute(new Runnable() {
				@Override
				public void run()
				{
					socket.send(bin);
				}
			});
    }

    public void verifyCode(final String vc)
	{
        pool.execute(new Runnable() {
				@Override
				public void run()
				{
					try
					{
						byte[] bin = h.call("GET", BASE_URL + "?verifycode=" + vc, null);
						doServerReturn(bin);
					}
					catch (Exception e)
					{
						service.updataUI(NewLoginActivity.CMD_LOGIN_FAILT, "网络异常");
					}
				}
			});
    }
}
