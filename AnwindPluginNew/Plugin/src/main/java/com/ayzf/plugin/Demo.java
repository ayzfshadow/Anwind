package com.ayzf.plugin;

import android.app.*;
import android.content.*;
import com.mcsqnxa.common.*;
import com.setqq.free.*;


public class Demo extends Application implements Runnable,LocalClient.IReceive
{
	private static Demo demo;
	private static LocalClient client;

	@Override
	protected void attachBaseContext(Context base)
    {
		super.attachBaseContext(base);
		this.demo=this;
		new Thread(this).start();
    }

	@Override
	public void run()
    {
		if (this.client != null && !this.client.isClosed())
        {
			return;
        }
		try//连接主程序
        {
			this.client=LocalClient.connect(29932);
			this.client.startListener(this);
			this.client.send(0x00);//与主程序握手
			while (!this.client.isClosed())
            {
				Thread.sleep(1000 * 30);
				this.client.send(0xFFFF);//心跳包
            }
        }
		catch (Exception e)
        {
			e.printStackTrace();
        }
    }

	/**
	 * Demo
	 */
	public static Demo getDemo()
    {
		return Demo.demo;
    }

	/**
	 * 跨进程句柄
	 */
	public static LocalClient getClient()
    {
		return Demo.client;
    }

	/**
	 * 接收主程序发来的信息
	 */
	@Override
	public void onReceive(LocalClient client,Object data)
    {
		if (data instanceof Integer)
        {
			if ((int)data == 0x01)
            {
				client.send(
                    new String[]{
                        "Demo",
                        "1.0.0",
                        "MCSQNXA",
                        "插件Demo",
                        super.getPackageName()
					});return;
            }
        }

		try
        {
			if (!(data instanceof Msg))
            {
				return;
            }
			Msg msg = (Msg)data;
			String text = ((Msg)data).getTextMsg();
			//client.send ( msg );不可以这种写法
			Msg p = new Msg();
			p.type=0;
			p.groupid=msg.groupid;
			p.addMsg("msg",text);
			client.send(p);
        }
		catch (Exception e)
        {
			e.printStackTrace();
        }
    }
}
