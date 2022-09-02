package com.anwind.plugin;
import com.setqq.free.sdk.IPlugin;
import com.setqq.free.Msg;
import com.setqq.free.sdk.PluginApiInterface;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.content.Intent;

public class Demo implements IPlugin
{

	private Context context;

	private PluginApiInterface api;

	@Override
	public Intent getActivity(Context p1)
	{
		return new Intent(p1,MainActivity.class);
	}

	@Override
	public Drawable getIcon(Context p1)
	{
		return  p1.getDrawable(R.mipmap.ic_launcher);
	}

	@Override
	public void onDestroy()
	{
	}

	@Override
	public void onLoad(Context p1, PluginApiInterface p2)
	{
		this.context=p1;
		this.api=p2;
	}

	@Override
	public void onMessageHandler(Msg msg)
	{
		if(msg.getTextMsg().equals("测试")){
			msg.clearMsg();
			msg.addMsg("msg","欢迎使用Anwind主程序");
			api.send(msg);
		}
	}
	
}
