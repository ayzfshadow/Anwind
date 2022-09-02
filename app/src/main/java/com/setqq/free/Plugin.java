package com.setqq.free;

import android.content.*;
import android.content.SharedPreferences.*;
import android.content.res.*;
import android.graphics.drawable.*;
import com.ayzf.anwind.*;
import com.saki.log.*;
import com.saki.qq.global.*;
import com.saki.service.*;
import com.saki.tools.*;
import com.saki.ui.*;
import com.saki.ui.SwitchButton.*;
import com.setqq.free.sdk.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import com.saki.ui.adapter.*;
import com.saki.client.SQApplication;

public class Plugin implements OnCheckedChangeListener
{
	public String[] sinfo;



	private Context plugincontext;

	private String path;

	public Intent getimpActivity()
    {
		return instance.getActivity(plugincontext);
    }
    public static SharedPreferences getAppConfig(Context context)
    {
        return context.getSharedPreferences("app_config",Context.MODE_PRIVATE);
    }

    public String name;
    public String packageName;
    public String author;
    public String version;
    public String info;
    public Drawable icon;
    public IPlugin instance;
    public boolean opration = false;
    public boolean jump = false;
    public int type;
    public String update;
    //private NewService context;




	public Plugin(String[] s)
    {
		this.sinfo=s;
		this.packageName=s[4];
		PluginManager.getInstance().addPlugin(this);
    }



    public Plugin(NewService context,String packageName,String author,String version,String info,String name,boolean z,IPlugin a,String path) throws  Exception
    {
        //this.context = context;
        this.packageName=packageName;
        this.name=name;
        this.author=author;
        this.version=version;
        this.info=info;
		this.path=path;
		this.plugincontext=context.createPackageContext(this.packageName,Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		AssetManager.class.getDeclaredMethod("addAssetPath",String.class).invoke(this.plugincontext.getAssets(),this.path);
		Class<?> clazz=Class.forName("android.app.ContextImpl");
		Method method=clazz.getDeclaredMethod("getImpl",Context.class);
		method.setAccessible(true);
		Object mContextImpl=method.invoke(null,plugincontext);
		//获取ContextImpl的实例
		//Log.d("csdn","mContextImpl="+mContextImpl);
		Field mPreferencesDir=clazz.getDeclaredField("mPreferencesDir");
		mPreferencesDir.setAccessible(true);
		// 获取mBase变量
		mPreferencesDir.set(mContextImpl,new File(plugincontext.getFilesDir(),"../shared_prefs"));


        this.instance=a;
		this.icon=instance.getIcon(this.plugincontext);
        this.jump=z;
        this.opration=getAppConfig(context).getBoolean(packageName,false);
        PluginManager.getInstance().addPlugin(this);
        onLoad(context);
    }
    public void setUpdate(String url)
    {
        this.type=1;
        this.update=url;
    }
    public void delete(Context context)
    {
        if (packageName != null)
        {
            FileUtil.deleteFile(new File(FileUtil.BASE_PATH + "Script/" + packageName));
        }
    }

    public boolean handlerMsg(Msg msg)
    {
		if (this.sinfo != null)
        {
			SQApplication.getServer().send(msg);//推送到插件
			return true;
        }
		else
        {
			try
            {
				if (instance != null)
                {
					instance.onMessageHandler(msg);
					return true;
                }
            }
			catch (Exception e)
            {
				e.printStackTrace();
				SQLog.e("[" + name + "]插件发生异常:" + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void onCheckedChanged(SwitchButton view,boolean isChecked)
    {
		if (this.sinfo != null)
        {
			ScriptAdapter.state.put(this.sinfo[4],isChecked);
        }

        opration=isChecked;
        Editor e = getAppConfig(NewService.getService()).edit();
        e.putBoolean(this.sinfo == null ? packageName: this.sinfo[4],opration);
        e.commit();
    }


    public void onLoad(NewService context)
    {
		if (this.sinfo != null)
        {
			return;
        }

        if (instance != null)
        {
            try
            {
                instance.onLoad(plugincontext,new com.setqq.free.sdk.PluginApi());
            }
			catch (Exception e)
            {
                e.printStackTrace();
                SQLog.w("插件初始化异常:" + e.toString());
            }
        }
    }

    public void stop()
    {
		if (this.sinfo != null)
        {
			return;
        }

        try
        {
            instance.onDestroy();
        }
		catch (Exception e)
        {
            e.printStackTrace();
            SQLog.e("脚本停止时异常:" + e.toString());
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Plugin)
        {
			/*if ( ((Plugin)obj).sinfo != null )
			 {
			 return this.sinfo [ 4 ].equals ( ( (Plugin)obj ).sinfo [ 4 ] );
			 }
			 */

            return this.packageName.equals(((Plugin) obj).packageName);
        }

        return super.equals(obj);
    }
}
