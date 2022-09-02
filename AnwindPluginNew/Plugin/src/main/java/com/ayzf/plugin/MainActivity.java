package com.ayzf.plugin;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;


public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.main_activity);
    }

	@Override
	protected void onRestart()//推送插件信息
    {
		super.onRestart();

		if (Demo.getClient() != null)
        {
			Demo.getClient().send(0x00);
        }
    }

	public void send(View v)
    {
		if (Demo.getClient() != null)
        {
			Demo.getClient().send(0x00);
        }
		Toast.makeText(this,"send",0).show();
    }

	public void rec(View v)
    {
		new Thread(Demo.getDemo()).start();
		Toast.makeText(this,"正在重新连接主程序",0).show();
    }
}
