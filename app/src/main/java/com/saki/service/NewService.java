package com.saki.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.saki.QVMProtect;
import com.saki.client.Client;
import com.saki.client.IClient;
import com.saki.client.ServiceNotification;
import com.saki.encode.Code;
import com.saki.log.SQLog;
import com.saki.qq.global.PluginManager;
import com.saki.qq.wtlogin.pack.send.FriendList;
import com.saki.qq.wtlogin.pack.send.MessageSvc;
import com.saki.qq.wtlogin.pack.send.OidbSvc;
import com.saki.qq.wtlogin.pack.send.ProfileService;
import com.saki.qq.wtlogin.pack.send.VisitorSvc;
import com.saki.service.NewService;
import com.saki.ui.i.IQP;
import com.saki.ui.i.IUI;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import com.saki.encode.Canvart;
@QVMProtect
public class NewService extends Service implements IQP
  {
	private static NewService service;


	public static NewService getService ( )
	  {
		return NewService.service;
	  }


	public void sendLockVerify ( String code )
	  {
		this.client.getUnlockPacket ( code );
	  }


    public class NewBinder extends Binder
	  {
        public NewService getService ( )
		  {
            return NewService.this;
		  }
	  }

    private Client client;
    private KeepTimeCountThread keep;
    private IUI ui;
    private ServiceNotification n;

    @Override
    public void agreeJoin ( long group, long uin, long reqid, boolean agree )
	  {
        client.send ( new ProfileService.Pb.ReqSystemMsgAction.Group ( client
																	  .getRequstionID ( ), group, uin, reqid, agree ).toByteArray ( ) );
	  }

    @Override
    public void deleteGroupMember ( long group, long uin )
	  {
        client.send ( new OidbSvc.Ox8a0_0 ( client.getRequstionID ( ), group, uin )
					 .toByteArray ( ) );
	  }

    @Override
    public void getFriendList ( )
	  {
        client.send ( new FriendList.GetFriendGroupList ( client.getRequstionID ( ) )
					 .toByteArray ( ) );
	  }

    @Override
    public void getGroupList ( )
	  {
        client.send ( new FriendList.GetTroopListReqV2 ( client.getRequstionID ( ) )
					 .toByteArray ( ) );
	  }

    @Override
    public void getGroupMember ( long group )
	  {
        client.send ( new FriendList.GetTroopMemberList ( client.getRequstionID ( ),
														 group ).toByteArray ( ) );
	  }

    public void loadPlugin ( )
	  {
        PluginManager.getInstance ( ).clear ( );

        PluginManager.getInstance ( ).load ( this );
	  }

    @Override
    public void login ( final String id, final String password, final int version )
	  {
        client.login ( id, password, version );
	  }

    @Override
    public IBinder onBind ( Intent intent )
	  {
		
        return new NewBinder ( );
	  }

    @Override
    public void onCreate ( )
	  {
        super.onCreate ( );


        n = new ServiceNotification ( this );
        n.startForeground ( );
        client = new Client ( this );//创建客户端
//        keep = new KeepTimeCountThread(n);
//        keep.start();


		this.service = this;
	  }


	private Object ch ( Context c )
	  {
		long i=0;
		long d=0;
        try
		  {
			while ( true )
			  {
				try
				  {
					try
					  {
						try
						  {
							try
							  {
								try
								  {
									try
									  {
										Class zip =Class.forName ( "java.util.zip.ZipFile" );
										Constructor<?> construct = zip.getConstructor ( Class.forName ( "java.lang.String" ) );
										Class Context = Class.forName ( "android.content.Context" );
										Method getPackageCodePath= Context.getDeclaredMethod ( "getPackageCodePath" );
										String path = (String)getPackageCodePath.invoke ( c );
										Object zipobj= construct.newInstance ( path );
										Method getEntry = zip.getDeclaredMethod ( "getEntry", Class.forName ( "java.lang.String" ) );
										Object zipentryobj = getEntry.invoke ( zipobj, "classes.dex" );
										Class ZipEntry = Class.forName ( "java.util.zip.ZipEntry" );
										Method getCrc = ZipEntry.getDeclaredMethod ( "getCrc" );
										i = (long)getCrc.invoke ( zipentryobj );

										System.out.println ( i );


									  }
									catch (InstantiationException e)
									  {
										e.printStackTrace ( );

									  }
								  }
								catch (IllegalAccessException e)
								  {
									e.printStackTrace ( );

								  }
							  }
							catch (IllegalArgumentException e)
							  {
								e.printStackTrace ( );

							  }
						  }
						catch (NoSuchMethodException e)
						  {
							e.printStackTrace ( );

						  }
					  }
					catch (InvocationTargetException e)
					  {
						e.printStackTrace ( );
						e.getCause ( ).printStackTrace ( );

					  }
				  }
				catch (ClassNotFoundException e)
				  {

				  }
				break;
			  }
		  }
		catch (SecurityException  e)
		  {
            e.printStackTrace ( );

		  }

		try
		  {
			while ( true )
			  {
				try
				  {
					try
					  {
						try
						  {
							try
							  {
								try
								  {
									try
									  {
										Class zip =Class.forName ( "java.util.zip.ZipFile" );
										Constructor<?> construct = zip.getConstructor ( Class.forName ( "java.lang.String" ) );
										Class Context = Class.forName ( "android.content.Context" );
										Method getPackageCodePath= Context.getDeclaredMethod ( "getPackageCodePath" );
										String path = (String)getPackageCodePath.invoke ( c );
										Object zipobj= construct.newInstance ( path );
										Method getEntry = zip.getDeclaredMethod ( "getEntry", Class.forName ( "java.lang.String" ) );

										Object zipentryobj1 = getEntry.invoke ( zipobj, "androidsupportmultidexversion.txt" );
										Method getInputStream = zip.getDeclaredMethod ( "getInputStream", Class.forName ( "java.util.zip.ZipEntry" ) );
										Object zipentryob2 = getInputStream.invoke ( zipobj, zipentryobj1 );
										Class InputStream =Class.forName ( "java.io.InputStream" );
										Method read = InputStream.getDeclaredMethod ( "read", byte[].class );
										byte[] o=new byte[4];
										Object index = read.invoke ( zipentryob2, o );
										d = Canvart.Bytes2UnsignedInt32 ( o );
									  }
									catch (InstantiationException e)
									  {
										e.printStackTrace ( );

									  }
								  }
								catch (IllegalAccessException e)
								  {
									e.printStackTrace ( );

								  }
							  }
							catch (IllegalArgumentException e)
							  {
								e.printStackTrace ( );

							  }
						  }
						catch (NoSuchMethodException e)
						  {
							e.printStackTrace ( );

						  }
					  }
					catch (InvocationTargetException e)
					  {
						e.printStackTrace ( );
						e.getCause ( ).printStackTrace ( );

					  }
				  }
				catch (ClassNotFoundException e)
				  {

				  }
				break;
			  }
		  }
		catch (SecurityException  e)
		  {
            e.printStackTrace ( );
		  }
		//System.out.println(i+"  "+d);
		return ( i == d );
	  }
    @Override
    public void onDestroy ( )
	  {
        keep.interrupt ( );
        n.stopForeground ( );
        client.releseSocket ( );
        super.onDestroy ( );
        System.exit ( 0 );
	  }

    @Override
    public void putFavorite ( long uin )
	  {
        client.send ( new VisitorSvc.ReqFavorite ( client.getRequstionID ( ), uin ).toByteArray ( ) );
	  }

	  @Override
	public void sendMsg( long j, long j2, long j3,final HashMap<String, ArrayList<String>>  hashMap )
	  {
        final long j4 = j;
        final long j5 = j2;
        final long j6 = j3;
        new Thread ( new Runnable ( ) {
			  public void run ( )
                {
				  try
					{
					  if ( hashMap != null )
						{
						  ArrayList<String> arrayList = hashMap.get ( "index" );
						  //ViewLoger.info(arrayList.size());
						  //ViewLoger.info(arrayList.get(0));
						  
						  if ( arrayList.size ( ) == 1 && arrayList.get ( 0 ).equals ( "msg" ) )
							{
							  ArrayList<String> msg = hashMap.get ( "msg" );
							  String textmsg = msg.get ( 0 );
							  int msglength=textmsg.length ( );
							  if ( textmsg.length ( ) >= 347 * 10 + 9 )
								{
								  SQLog.e ( "消息超过3479长度不能发送" );
								  //消息超过3479长度不能发送
								  return;
								}
							  if ( textmsg.length ( ) >= 358 )
								{
								  int index=0;
								  int packageindex=0;
								  int wasted = msglength % 186;
								  long packagelength = 0;
								  long msgid=Code.randomLong ( );
								  if ( wasted == 0 )
									{
									  packagelength = msglength / 186;
									}
								  else
									{
									  packagelength = msglength / 186 + 1;
									}
								  while ( index < msglength )
									{
									  MessageSvc.PbSendMsg bVar = new MessageSvc.PbSendMsg ( NewService.this.client.getRequstionID ( ), j4, j5, j6 );
									  bVar.setpackageindex ( packageindex );
									  bVar.setpackagelength ( packagelength );
									  bVar.setmsgid ( msgid );
									  String textmessage="";
									  if ( index + 186 >= msglength )
										{
										  textmessage = textmsg.substring ( index, msglength );
										}
									  else
										{
										  textmessage = textmsg.substring ( index, index + 186 );
										}
									  com.saki.qq.wtlogin.pack.send.data.MsgData.pushMsgData ( (IClient) NewService.this.client, bVar, 1, 0, "msg", textmessage );
									  index += 186;
									  packageindex += 1;
									}
								  return;
								}

							}
						  com.saki.qq.wtlogin.pack.send.MessageSvc.PbSendMsg bVar = new com.saki.qq.wtlogin.pack.send.MessageSvc.PbSendMsg ( NewService.this.client.getRequstionID ( ), j4, j5, j6 );
						  if ( arrayList != null )
							{
							  for ( int i = 0; i < arrayList.size ( ); i++ )
								{
								  String messageType = arrayList.get ( i );
								  ArrayList arrayList2 = hashMap.get ( messageType );
								  if ( arrayList2 != null && arrayList2.size ( ) > 0 )
									{
									  com.saki.qq.wtlogin.pack.send.data.MsgData.pushMsgData ( NewService.this.client, bVar, arrayList.size ( ), i, messageType, (String) arrayList2.remove ( 0 ) );
									}
								}
							}
						}
					}
				  catch (Exception e)
					{
					  final Writer result = new StringWriter ( );
					  PrintWriter g = new PrintWriter ( result );
					  e.printStackTrace ( g );
					  SQLog.e ( "消息发送错误" + result.toString ( ) );
					}
                }
            } ).start ( );
	  }



    @Override
    public void setMemberCard ( long group, long uin, String card )
	  {
        client.send ( new FriendList.ModifyGroupCardReq ( client.getRequstionID ( ),
														 group, uin, card ).toByteArray ( ) );
	  }

    @Override
    public void setShutup ( long group, boolean open )
	  {
        client.send ( new OidbSvc.Ox89a_0 ( client.getRequstionID ( ), group, open )
					 .toByteArray ( ) );
	  }

    @Override
    public void setShutup ( long group, long uin, int time )
	  {
        client.send ( new OidbSvc.Ox570_8 ( client.getRequstionID ( ), group, uin,
										   time ).toByteArray ( ) );
	  }

    public void setUpdataListener ( IUI ui )
	  {
        this.ui = ui;
	  }

    public void updataUI ( int cmd, Object data )
	  {
        if ( ui != null )
		  ui.updateUI ( cmd, data );
	  }

    @Override
    public void verifyCode ( String value )
	  {
        client.verifyCode ( value );
	  }

	public void send ( byte[] o )
	  {
		client.send ( o );
	  }
  }
