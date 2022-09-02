package com.saki.ui;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.view.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.ayzf.anwind.*;
import com.mcsqnxa.common.*;
import com.saki.qq.global.*;
import com.saki.service.*;
import com.saki.tools.*;
import com.saki.ui.i.*;
import com.setqq.free.*;
import com.setqq.free.sdk.*;
import java.util.*;
import com.saki.client.SQApplication;

public class PagerActivity extends Activity implements OnClickListener, IB,LocalServer.IReceive
{
    class Pager extends PagerAdapter
    {
        @Override
        public void destroyItem(ViewGroup container,int position,Object object)
        {
            container.removeView(list.get(position));
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return title[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container,int position)
        {
            View v = list.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public boolean isViewFromObject(View arg0,Object arg1)
        {
            return arg0 == arg1;
        }
    }

    public TroopUI troop;
    public PluginUI appView;
    public RuntimeLogUI set;

    private final LinkedList<View> list = new LinkedList<View>();
    private NewConnection connection;
    private final String[] title = new String[]{"群列表", "插件列表","运行日志"};
    private long mExitTime;
    private ToastHandler toast = new ToastHandler(this);


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
		if (requestCode == FileUtil.SCRIPT_SELECT_CODE)
        {

        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onClick(View v)
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        troop=new TroopUI(this);
        MsgManager.getInstance().registerMsgListener(troop);
        appView=new PluginUI(this);
        set=new RuntimeLogUI(this);

        list.add(troop);
        list.add(appView);

        list.add(set);
        setContentView(R.layout.activity_pager);
        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new Pager());
        bindService(new Intent(this,NewService.class),
                    connection = new NewConnection(this),Context.BIND_AUTO_CREATE);

		if (SQApplication.getServer() == null)
        {
			new Thread(new Runnable(){
                    @Override
                    public void run()
					{
                        try
						{
                            SQApplication.setServer(LocalServer.bind(29932));
                            SQApplication.getServer().startListener(PagerActivity.this);
						}
                        catch (Exception e)
						{
                            e.printStackTrace();
						}
					}
				}).start();
        }

    }

	@Override
	public void onReceive(LocalServer server,final Object data)
    {
		try
        {
			/*new Handler ( Looper.getMainLooper ( ) ).post ( new Runnable ( ){
			 @Override
			 public void run ( )
			 {
			 Toast.makeText ( PagerActivity.this, data.toString ( ), 0 ).show ( );
			 }
			 } );*/

			if (data instanceof Integer)
            {
				if ((int)data == 0x00)//插件与主程序握手
                {
					server.send(0x01);//获取插件信息
                }
				else if ((int)data == 0xFFFF)//心跳包,不需要处理
                {
                    server.send(0x0000);
                    return;
                }
            }
			else if (data instanceof String[])
            {
				new Plugin((String[])data);
            }
			else if (data instanceof Msg)
            {
				new PluginApi().send((Msg)data);
            }
        }
		catch (Exception e)
        {
			e.printStackTrace();
        }
    }







    @Override
    protected void onDestroy()
    {
        troop.onDestroy();
        appView.onDestroy();
        set.onDestroy();
        unbindService(connection);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if ((System.currentTimeMillis() - mExitTime) > 2000)
            {
                Toast t = Toast.makeText(this,"再按一次，退出程序",Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
                mExitTime=System.currentTimeMillis();
            }
			else
            {
                stopService(new Intent(this,NewService.class));
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public void onBind(NewService service)
    {
    }

}
