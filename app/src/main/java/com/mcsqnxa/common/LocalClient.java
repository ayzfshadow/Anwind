package com.mcsqnxa.common;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;


public class LocalClient extends Socket
  {
	private static final ExecutorService threadPool = Executors.newCachedThreadPool ( );

	public LocalClient ( String address, int port ) throws Exception
	  {
		super ( address, port );
		super.setKeepAlive ( true );
	  }

	public static LocalClient connect ( int port ) throws Exception
	  {
		return new LocalClient ( "127.0.0.1", port );
	  }

	public static LocalClient connect ( String address, int port ) throws Exception
	  {
		return new LocalClient ( address, port );
	  }

	/**
     * 启动监听
     */
    public LocalClient startListener ( final IReceive receive )
	  {
		this.threadPool.submit ( new Runnable ( ){
			  @Override
			  public void run ( )
				{
				  try
					{
					  InputStream is = LocalClient.super.getInputStream ( );

					  while ( true )
						{
						  byte[] head = new byte[4];//包头

						  if ( is.read ( head ) == -1 )
							{
							  continue;
							}

						  int len = LocalClient.this.getInt ( head );//包体长度

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
							  receive.onReceive ( LocalClient.this, ois.readObject ( ) );
							}
						  catch (Exception e)
							{
							  e.printStackTrace ( );
							}
						}
					}
				  catch (Exception e)
					{
					  e.printStackTrace ( );
					}
				}
			} );


        return this;
	  }

	/**
	 * 发送数据
	 */
	public synchronized LocalClient send ( final Object data )
	  {
		this.threadPool.submit ( new Runnable ( ){
			  @Override
			  public void run ( )
				{
				  if ( data != null && LocalClient.super.isConnected ( ) )
					{
					  synchronized ( Object.class )
						{
						  try
							{
							  ByteArrayOutputStream baos = new ByteArrayOutputStream ( );

							  ObjectOutputStream oos = new ObjectOutputStream ( baos );
							  oos.writeObject ( data );

							  byte[] send = baos.toByteArray ( );

							  OutputStream os = LocalClient.super.getOutputStream ( );
							  os.write ( LocalClient.this.getBytes ( send.length ) );//包头
							  os.write ( send );
							  os.flush ( );
							}
						  catch (SocketException e)
							{
							  try
								{
								  LocalClient.this.close ( );
								}
							  catch (IOException n)
								{
								  e.printStackTrace ( );
								}
							}
						  catch (IOException e)
							{
							  try
								{
								  LocalClient.this.close ( );
								}
							  catch (IOException n)
								{
								  e.printStackTrace ( );
								}
							}
						  catch (Exception e)
							{
							  e.printStackTrace ( );
							}
						}
					}
				}
			} );

		return this;
	  }

	/**
	 * 关闭与服务端的连接
	 */
	@Override
	public void close ( ) throws IOException
	  {
		super.close ( );
	  }

	public static interface IReceive
	  {
        void onReceive ( LocalClient client, Object data );
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
