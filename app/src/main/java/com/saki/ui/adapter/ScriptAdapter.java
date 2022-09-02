package com.saki.ui.adapter;

import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.text.method.*;
import android.view.*;
import android.widget.*;
import com.ayzf.anwind.*;
import com.saki.log.*;
import com.saki.ui.*;
import com.saki.ui.listener.*;
import com.saki.ui.view.*;
import com.setqq.free.*;
import java.util.*;
import android.graphics.*;

public class ScriptAdapter extends BaseAdapter implements IPluginListener
  {

    class ItemHolder
	  {
        public ImageView icon;
        public TextView name;
        public TextView author;
        public TextView version;
        public TextView info;
        public SwitchButton opration;
        public DownloadButton download;
	  }

    class PluginAdapterHandler extends Handler
	  {
        public void add ( Plugin p )
		  {
            Message m = Message.obtain ( );
            m.obj = p;
            m.what = 0;
            sendMessage ( m );
		  }

        @Override
        public void handleMessage ( Message msg )
		  {
            super.handleMessage ( msg );
            if ( msg.what == 0 )
			  {
                Plugin p = (Plugin) msg.obj;
                replacePlugin ( p );
                notifyDataSetChanged ( );
			  }
			else if ( msg.what == 1 )
			  {
                notifyDataSetChanged ( );
			  }
			else
			  {
                Plugin p = (Plugin) msg.obj;
                Iterator<Plugin> it = script.iterator ( );
                while ( it.hasNext ( ) )
				  {
                    if ( it.next ( ).equals ( p ) )
					  {
                        it.remove ( );
                        notifyDataSetChanged ( );
                        break;
					  }
				  }
			  }
		  }

        public void remove ( Plugin pn )
		  {
            Message m = Message.obtain ( );
            m.obj = pn;
            m.what = 2;
            sendMessage ( m );
		  }

        private void replacePlugin ( Plugin p )
		  {
            Iterator<Plugin> it = script.iterator ( );
            while ( it.hasNext ( ) )
			  {
                Plugin op = it.next ( );

				if ( op.sinfo != null && p.sinfo != null )//新的插件
				  {
					if ( op.sinfo [ 4 ].equals ( p.sinfo [ 4 ] ) )
					  {
						it.remove ( );
						break;
					  }
				  }
				else if ( op.packageName.equals ( p.packageName ) )
				  {
                    it.remove ( );
                    break;
				  }
			  }
            script.add ( p );
		  }

        public void update ( )
		  {
            Message m = Message.obtain ( );
            m.what = 1;
            sendMessage ( m );
		  }
	  }

    private final ArrayList<Plugin> script = new ArrayList<Plugin> ( );

    private final LayoutInflater inflater;

    private final PluginAdapterHandler handler = new PluginAdapterHandler ( );

    private final PluginUI ui;

    public ScriptAdapter ( Context c, PluginUI ui )
	  {
        inflater = LayoutInflater.from ( c );
        this.ui = ui;
	  }

    public void add ( Plugin p )
	  {
        ui.swipe.setRefreshing ( false );
        handler.add ( p );
	  }

    @Override
    public int getCount ( )
	  {
        return script.size ( );
	  }

    @Override
    public Plugin getItem ( int arg0 )
	  {
        return script.get ( arg0 );
	  }

    @Override
    public long getItemId ( int arg0 )
	  {
        return arg0;
	  }



	public static HashMap<String,Boolean> state = new HashMap<> ( ); 


    @Override
    public View getView ( int position, View convertView, ViewGroup parent )
	  {
        final ItemHolder holder;

        if ( convertView == null )
		  {
            holder = new ItemHolder ( );
            convertView = inflater.inflate ( R.layout.item_plugin, null );
            holder.icon = convertView.findViewById ( R.id.icon );
            holder.name = convertView.findViewById ( R.id.name );
            holder.author = convertView.findViewById ( R.id.author );
            holder.version = convertView.findViewById ( R.id.version );
            holder.info = convertView.findViewById ( R.id.info );
            holder.info.setMovementMethod ( ScrollingMovementMethod.getInstance ( ) );
            holder.opration = convertView.findViewById ( R.id.opration );
            holder.download = convertView.findViewById ( R.id.down );
            convertView.setTag ( holder );
		  }
		else
		  {
            holder = (ItemHolder) convertView.getTag ( );
		  }

        final Plugin p = getItem ( position );

		if ( p.sinfo == null )//这个是你原来的插件
		  {
			if ( p.icon == null )
			  {
				holder.icon.setImageResource ( R.drawable.ic_plugin_face_default );
			  }
			else
			  {
				holder.icon.setImageDrawable ( p.icon );
			  }

			holder.name.setText ( p.name );
			holder.author.setText ( p.author );
			holder.version.setText ( p.version );
			holder.info.setText ( p.info );

			holder.opration.setChecked ( p.opration );
			holder.opration.setOnCheckedChangeListener ( p );
		  }
		else//新的插件
		  {
			try
			  {
				PackageManager packageManager = convertView.getContext ( ).getPackageManager ( );  
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo ( p.sinfo [ 4 ], 0 );
				holder.icon.setImageDrawable ( packageManager.getApplicationIcon ( applicationInfo ) );
			  }
			catch (Exception e)
			  {
				e.printStackTrace ( );
				holder.icon.setImageResource ( R.drawable.ic_launcher );
			  }

			holder.version.setBackgroundResource(R.drawable.bg_green_boll);
			
			holder.name.setText ( p.sinfo [ 0 ] );
			holder.version.setText ( p.sinfo [ 1 ] );
			holder.author.setText ( p.sinfo [ 2 ] );
			holder.info.setText ( p.sinfo [ 3 ] );

			holder.opration.setChecked ( p.opration = state.getOrDefault ( p.sinfo [ 4 ], false ) );
			holder.opration.setOnCheckedChangeListener ( p );
		  }

        if ( p.type == 0 )
		  {
            holder.download.setVisibility ( View.GONE );//隐藏更新
            holder.opration.setVisibility ( View.VISIBLE );//显示开关
		  }
		else if ( p.type == 1 )
		  {
            holder.download.setVisibility ( View.VISIBLE );//显示下载
            holder.opration.setVisibility ( View.GONE );//隐藏开关
		  }
		else if ( p.type == 2 )
		  {
            holder.download.setVisibility ( View.VISIBLE );//显示更新
            holder.download.setTitle ( "更新" ); //更新标签
		  }
        holder.download.setOnClickListener ( new View.OnClickListener ( ) {
			  @Override
			  public void onClick ( View view )
				{
				  holder.download.startDownload ( p.update, new DownloadButton.onDownload ( ) {
						@Override
						public void complet ( byte[] data )
						  {
							try
							  {

							  }
							catch (Exception e)
							  {
								SQLog.w ( "更新失败:" + p.update );
							  }
						  }
					  } );
				}
			} );
        return convertView;
	  }

    @Override
    public void onPluginAdd ( Plugin p )
	  {
        add ( p );
	  }

    @Override
    public void onPluginClear ( )
	  {
        script.clear ( );
        handler.update ( );
	  }

    @Override
    public void onFlush ( )
	  {
        handler.update ( );
	  }

    public void remove ( Plugin p )
	  {
        handler.remove ( p );
	  }

  }
