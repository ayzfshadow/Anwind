package com.saki.qq.global;

import android.annotation.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import com.ayzf.anwind.*;
import com.saki.encode.*;
import com.saki.log.*;
import com.saki.qq.wtlogin.unpack.ReMessageSvc.*;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.*;
import com.saki.qq.wtlogin.unpack.ReProfileService.Pb.ReqSystemMsgNew.Group.*;
import com.saki.service.*;
import com.saki.ui.listener.*;
import com.setqq.free.*;
import com.setqq.free.sdk.*;
import dalvik.system.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import com.saki.client.SQApplication;


@SuppressLint("NewApi")
public class PluginManager implements IMessageListener
  {

	public void load ( NewService p0 )
	  {
		try
		  {
			loadJavaPlugin ( p0 );
		  }
		catch (Exception e)
		  {
			e.printStackTrace ( );
		  }

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
    public static PluginManager getInstance ( )
	  {
        return mana;
	  }

    private boolean buddy_repley = true;
    private boolean diccuse_replay = true;
    private static PluginManager mana = new PluginManager ( );

	private static HashMap<String,PathClassLoader> i  = new HashMap<String,PathClassLoader> ( );
    public void loadJavaPlugin ( NewService context ) throws InstantiationException, IllegalAccessException
	  {

        PackageManager packageManager = context.getPackageManager ( );


        for ( PackageInfo packageInfo : packageManager.getInstalledPackages ( 8320 ) )
		  {
            Bundle bundle = packageInfo.applicationInfo.metaData;
            if ( bundle != null )
			  {
                String string = bundle.getString ( "v9freepluginclass" );
                if ( string != null )
				  {
                    String author = bundle.getString ( "author", "佚名" );
                    String version = packageInfo.versionName;
                    String info = bundle.getString ( "info", "暂无说明" );
                    String name = (String) packageInfo.applicationInfo.loadLabel ( packageManager );
                    boolean z = bundle.getBoolean ( "jump", false );
                    try
					  {
//						if (!j.contains(packageInfo.applicationInfo.sourceDir))
//						{
//							AssetManager.class.getDeclaredMethod("addAssetPath", String.class).invoke(context.getAssets(), packageInfo.applicationInfo.sourceDir);
//							j.add(packageInfo.applicationInfo.sourceDir);
//						}
						IPlugin a = loadPluginClass ( context, packageInfo.applicationInfo.nativeLibraryDir, packageInfo.applicationInfo.sourceDir, string );
                        if ( a != null )
						  {
                            new Plugin ( context, packageInfo.packageName, author, version, info, name, z, a, packageInfo.applicationInfo.sourceDir );
						  }

					  }
					catch (Exception e)
					  {
                        StringWriter g = new StringWriter ( );
                        PrintWriter y = new PrintWriter ( g );
                        e.printStackTrace ( y );
                        SQLog.e ( "插件" + name + "加载失败!" + g.toString ( ) );
					  }
				  }
			  }
		  }

		SQApplication.getServer ( ).send ( 0x01 );//通知插件推送插件信息
	  }


	private static IPlugin loadPluginClass ( Context context, String nativeDir, String sourceDir, String string ) throws IllegalAccessException, ClassNotFoundException, InstantiationException
	  {

		PathClassLoader u  = i.get ( sourceDir );
		if ( u == null )
		  {
			u = new PathClassLoader ( sourceDir, nativeDir, context.getClassLoader ( ) );
			i.put ( sourceDir, u );
		  }
		return (IPlugin) u.loadClass ( string ).newInstance ( );


	  }


    private ArrayList<Plugin> Plugins = new ArrayList<Plugin> ( );
    private IPluginListener l;

    public Plugin findPlugin ( String packname )
	  {
        if ( packname != null )
		  {
            for ( Plugin p : Plugins )
			  {
                if ( packname.equals ( p.packageName ) )
				  return p;
			  }
		  }
        return null;
	  }
    public void addPlugin ( Plugin p )
	  {
        Plugins.remove ( p );
        if ( l != null )
		  l.onPluginAdd ( p );
        Plugins.add ( p );
	  }
    public void flushPlugin ( )
	  {
        if ( l != null )
		  l.onFlush ( );
	  }
    public void clear ( )
	  {
        for ( Plugin p : Plugins )
		  {
            p.stop ( );
		  }
        Plugins.clear ( );
        if ( l != null )
		  {
            l.onPluginClear ( );
		  }
	  }



    @Override
    public void onDisMsg ( PbPushDisMsg msg )
	  {
        if ( !diccuse_replay || msg.fromUin == 1000000 )
		  return;
        for ( Plugin p : Plugins )
		  {
            if ( !p.opration || msg.fromUin == Global.qq )
			  continue;
            Msg m = new Msg ( );
            m.type = Msg.TYPE_DIS_MSG;
            m.groupid = msg.groupId;
            m.groupName = msg.groupName;
            m.uin = msg.fromUin;
            m.uinName = msg.fromName;
            m.setData ( msg.msg );
            m.time = msg.sendTime;
            m.title = msg.sendTitle;
            p.handlerMsg ( m );
		  }
	  }

    @Override
    public void onGroupMsg ( PbPushGroupMsg msg )
	  {
        if ( msg.fromUin == 1000000 )
		  return;
        long code = TroopManager.getInstance ( ).id2code ( msg.groupId );
        for ( Plugin p : Plugins )
		  {
            if ( !p.opration || !TroopManager.getInstance ( ).isOPen ( msg.groupId )
				|| msg.fromUin == Global.qq )
			  continue;
            Msg m = new Msg ( );
            m.type = Msg.TYPE_GROUP_MSG;
            m.code = code;
            m.groupid = msg.groupId;
            m.groupName = msg.groupName;
            m.uin = msg.fromUin;
            m.uinName = msg.fromName;
            m.setData ( msg.msg );
            m.time = msg.sendTime;
            m.title = msg.sendTitle;
            p.handlerMsg ( m );
		  }
	  }

    @Override
    public void onOtherMsg ( PbGetMsg msg )
	  {
        if ( msg.msgList.size ( ) > 0 )
		  {
            // 只取用最后一条消息
            PbGetMsg.Msg ms = msg.msgList.get ( msg.msgList.size ( ) - 1 );
            //Log.i(this,"[其他消息]"+ms.type+" "+ms.groupid+" "+ms.fromUin+" "+ms.applyName+" "+ms.code+" "+ms.sendTime+" "+ms.msg);
            // 对好友消息进行处理
            if ( buddy_repley && ms.fromUin != Global.qq
				&& ms.type == PbGetMsg.T_MSG )
			  {
                Log.i ( this, ms.toString ( ) );
                for ( Plugin p : Plugins )
				  {
                    if ( !p.opration )
					  continue;
                    Msg m = new Msg ( );
                    m.type = Msg.TYPE_BUDDY_MSG;
                    m.groupid = ms.groupid;
                    m.uin = ms.fromUin;
                    m.code = ms.code;
                    m.uinName = ms.applyName;
                    m.addMsg ( "msg", ms.msg );
                    m.time = ms.sendTime;
                    p.handlerMsg ( m );
				  }
			  }
		  }
	  }

    @Override
    public void onSystemMsg ( SysMsg msg )
	  {
        Log.i ( this, "系统消息:" + msg.group + ":" + msg.msg );
        for ( Plugin p : Plugins )
		  {
            if ( !p.opration )
			  continue;
            Msg m = new Msg ( );
            m.type = Msg.TYPE_SYS_MSG;
            m.groupid = msg.group;
            m.uin = msg.uin;
            m.groupName = msg.groupName;
            m.time = msg.time;
            m.uinName = msg.uinName;
            m.code = msg.requstId;
            m.addMsg ( "msg", msg.msg );
            if ( msg.inviter != 0 && msg.inviterName != null )
			  {
                m.addMsg ( "inviter", msg.inviter + "@" + msg.inviterName );
			  }
            p.handlerMsg ( m );
		  }
	  }

    @Override
    public void onTransMsg ( PbPushTransMsg msg )
	  {
        Log.i ( this, "变换消息:" + msg.groupId + " " + msg.uin + " " + msg.type
			   + "  " + msg.b );
        for ( Plugin p : Plugins )
		  {
            if ( !p.opration || !TroopManager.getInstance ( ).isOPen ( msg.groupId ) )
			  continue;
            Msg m = new Msg ( );
            if ( msg.type == PbPushTransMsg.T_DELETE )
			  m.type = Msg.TYPE_DELETE_MEMBER;
            else if ( m.type == PbPushTransMsg.T_ADMIN )
			  m.type = Msg.TYPE_ADMIN_CHANGE;
            else
			  return;
            m.code = msg.code;
            m.groupid = msg.groupId;
            m.value = msg.b ? 0 : -1;
            m.uin = msg.uin;
            m.time = msg.time;
            p.handlerMsg ( m );
		  }
	  }

    public void setBuddyReplay ( boolean b )
	  {
        this.buddy_repley = b;
	  }

    public void setDicuseReplay ( boolean b )
	  {
        this.diccuse_replay = b;
	  }

    public void setListener ( IPluginListener l )
	  {
        this.l = l;
	  }
  }
