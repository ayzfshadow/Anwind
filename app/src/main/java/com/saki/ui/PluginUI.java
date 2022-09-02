package com.saki.ui;

import android.annotation.*;
import android.app.AlertDialog.*;
import android.content.*;
import android.net.*;
import android.support.v4.widget.*;
import android.support.v4.widget.SwipeRefreshLayout.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.ayzf.anwind.*;
import com.saki.qq.global.*;
import com.saki.service.*;
import com.saki.ui.adapter.*;
import com.saki.ui.i.*;
import com.setqq.free.*;

public class PluginUI extends RelativeLayout  implements OnItemClickListener,IB,
OnItemLongClickListener, OnRefreshListener
  {

    public ScriptAdapter adapter;
    public SwipeRefreshLayout swipe;

	private LayoutInflater inflater;

	private NewConnection connection;

	private ListView list;

    public PluginUI ( Context context )
	  {
        super ( context );
		inflater = LayoutInflater.from ( context );
        inflater.inflate ( R.layout.view_app, this );
		initList ( context );
        context.bindService ( new Intent ( context, NewService.class ),
							 connection = new NewConnection ( this ), Context.BIND_AUTO_CREATE );
	  }


    @SuppressLint("InlinedApi")
    protected void initList ( Context c )
	  {
        this. list = findViewById ( R.id.applist );
        adapter = new ScriptAdapter ( c, this );

        swipe = findViewById ( R.id.swipe );
        swipe.setColorSchemeResources ( android.R.color.holo_blue_light,
									   android.R.color.holo_red_light,
									   android.R.color.holo_orange_light,
									   android.R.color.holo_green_light );
        swipe.setOnRefreshListener ( this );
        list.setAdapter ( adapter );
        list.setOnItemClickListener ( this );
        list.setOnItemLongClickListener ( this );
        PluginManager.getInstance ( ).setListener ( adapter );

	  }

    private void showUI ( Plugin p )
	  {
		if ( p.sinfo != null )
		  {
			Intent intent = getContext ( ).getPackageManager ( ).getLaunchIntentForPackage ( p.packageName );
			intent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );

			try
			  {
				getContext ( ).startActivity ( intent );
			  }
			catch (Exception noFound)
			  {
				Toast.makeText ( getContext ( ), "Package not found!", Toast.LENGTH_SHORT ).show ( );
			  }

			return;
		  }

        if ( p.jump )
		  {
            if ( p.packageName == null || p.packageName.equals ( "" ) )
			  return;
            if ( p.instance != null )
			  {
                //弹出窗口
                Intent intent = p.getimpActivity ( );
				intent.setPackage ( p.packageName );
				getContext ( ).startActivity ( intent );
			  }
		  }
	  }

    @Override
    public void onItemClick ( AdapterView<?> list, View view, int postion, long id )
	  {
        Plugin p = adapter.getItem ( postion );
        showUI ( p );
	  }

    @Override
    public boolean onItemLongClick ( AdapterView<?> list, View view, int postion,
									long id )
	  {
        final Plugin i = adapter.getItem ( postion );

		if ( i.sinfo != null )
		  {
			Uri uri = Uri.fromParts ( "package", i.packageName, null );
			Intent intent = new Intent ( Intent.ACTION_DELETE, uri );
			getContext ( ).startActivity ( intent );

			return true;
		  }

        if ( i.packageName == null || i.packageName.equals ( "" ) )
		  {
            Toast.makeText ( getContext ( ), "此插件无法卸载", Toast.LENGTH_SHORT ).show ( );
            return false;
		  }
        Builder b = new Builder ( getContext ( ) );
        b.setTitle ( "卸载提示" );
        b.setMessage ( "确定要卸载[" + i.name + "]吗？" );
        b.setNegativeButton ( "取消", null );
        b.setPositiveButton ( "卸载/删除", new DialogInterface.OnClickListener ( ) {
			  @Override
			  public void onClick ( DialogInterface dialog, int which )
				{
				  i.delete ( getContext ( ) );
				  getService ( ).loadPlugin ( );
				}
			} );
        b.show ( );
        return true;
	  }


	public NewService getService ( )
	  {
        return connection.getService ( );
	  }

    @Override
    public void onBind ( NewService service )
	  {
        service.loadPlugin ( );
	  }

    @Override
    public void onRefresh ( )
	  {
        getService ( ).loadPlugin ( );
		swipe.setRefreshing ( false );
	  }

	public void onDestroy ( )
	  {

	  }

  }
