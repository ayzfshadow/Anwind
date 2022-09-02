package com.mcsqnxa.common;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class LocalServer implements Runnable
  {
    private IReceive receive;
    private final ServerSocket server;
    private final HashSet<Socket> client = new HashSet<> ( );
    private static final ExecutorService threadPool = Executors.newCachedThreadPool ( );


    public LocalServer ( int port ) throws IOException
	  {
        this.server = new ServerSocket ( port );
	  }

    /**
     * 绑定端口
     */
    public static LocalServer bind ( int port ) throws IOException
	  {
        return new LocalServer ( port );
	  }

    /**
     * 启动监听
     */
    public LocalServer startListener ( IReceive receive )
	  {
        this.receive = receive;
        threadPool.submit ( this );
        return this;
	  }

    /**
     * 监听客户端请求
     */
    @Override
    public void run ( )
	  {
        while ( !this.server.isClosed ( ) )
		  {
            try
			  {
                final Socket client = this.server.accept ( );
                client.setKeepAlive ( true );//保持长连

                this.client.add ( client );

                this.threadPool.submit ( new Runnable ( ) {//监听客户端请求
					  @Override
					  public void run ( )
						{
						  try
							{
							  InputStream is = client.getInputStream ( );

							  while ( true )
								{
								  byte[] head = new byte[4];//包头

								  if ( is.read ( head ) == -1 )
									{
									  continue;
									}

								  int len = LocalServer.this.getInt ( head );//包体长度

								  if ( len < 1 )
									{
									  continue;
									}

								  byte[] body = new byte[len];

								  if ( is.read ( body ) == -1 )
									{
									  continue;
									}

								  try
									{
									  ObjectInputStream ois = new ObjectInputStream ( new ByteArrayInputStream ( body ) );
									  LocalServer.this.receive.onReceive ( LocalServer.this, ois.readObject ( ) );
									}
								  catch (Exception e)
									{
									  e.printStackTrace ( );
									}
								}
							}
						  catch (Exception e)
							{
							  try
								{
								  client.close ( );LocalServer.this.client.remove ( client );
								}
							  catch (Exception ec)
								{
								  e.printStackTrace ( );
								}
							}
						}
					} );
			  }
			catch (Exception e)
			  {
                e.printStackTrace ( );
			  }
		  }
	  }

    /**
     * 关闭服务
     */
    public void closeServer ( )
	  {
        if ( this.server != null )
		  {
            try
			  {
                this.server.close ( );
			  }
			catch (IOException e)
			  {
                e.printStackTrace ( );
			  }
		  }
	  }

    /**
     * 发送数据
     */
    public LocalServer send ( final Object data )
	  {
        if ( data != null && this.client.size ( ) > 0 )
		  {
			this.threadPool.submit ( new Runnable ( ){
				  @Override
				  public void run ( )
					{
					  synchronized ( Object.class )
						{
						  try
							{
							  ByteArrayOutputStream bahs = new ByteArrayOutputStream ( );

							  ObjectOutputStream oos = new ObjectOutputStream ( bahs );
							  oos.writeObject ( data );

							  byte[] send = bahs.toByteArray ( );
							  Socket[] clients = LocalServer.this.client.toArray ( new Socket[]{} );

							  for ( Socket client:clients )
								{
								  try
									{
									  OutputStream os = client.getOutputStream ( );
									  os.write ( LocalServer.this.getBytes ( send.length ) );//长连关键,包头4字节
									  os.write ( send );//包头
									  os.flush ( );
									}
								  catch (Exception e)
									{
									  client.close ( );LocalServer.this.client.remove ( client );
									}
								}
							}
						  catch (Exception e)
							{
							  e.printStackTrace ( );
							}
						}
					}
				} );
		  }

        return this;
	  }

    public static interface IReceive
	  {
        void onReceive ( LocalServer server, Object data );
	  }

	public byte[] getBytes ( int i )
	  {
        byte[] result = new byte[4];
        result [ 0 ] = (byte)( ( i >> 24 ) & 0xFF );
        result [ 1 ] = (byte)( ( i >> 16 ) & 0xFF );
        result [ 2 ] = (byte)( ( i >> 8 ) & 0xFF );
        result [ 3 ] = (byte)( i & 0xFF );

        return result;
	  }

    public int getInt ( byte[] bytes )
	  {
        int value = 0;

        for ( int i = 0; i < 4; i++ )
		  {
            value += ( bytes [ i ] & 0xFF ) << ( ( 3 - i ) * 8 );
		  }

        return value;
	  }
  }
