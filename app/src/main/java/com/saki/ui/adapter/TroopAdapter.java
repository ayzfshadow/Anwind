package com.saki.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.saki.qq.global.TroopManager;
import com.saki.qq.wtlogin.pack.jce.Troop;
import com.ayzf.anwind.R;
import com.saki.tools.BitmapCallback;
	import com.saki.tools.BitmapUtil;
	import java.util.Iterator;

public class TroopAdapter extends BaseAdapter
{
    class ItemHolder
	{
        public ImageView icon;
        public TextView title;
        public TextView id;
        public TextView count;
        public Switch opration;


		}

    class TroopAdapterHandler extends Handler
	{
        class TroopMessage
		{
            public long id;
            public Bitmap icon;

            public TroopMessage(long id, Bitmap icon)
			{
                this.id = id;
                this.icon = icon;
            }
        }

        @Override
        public void handleMessage(Message msg)
		{
            super.handleMessage(msg);
            switch (msg.what)
			{
                case 0:
                    TroopAdapter.this.notifyDataSetChanged();
                    break;
                case 1:
                    TroopAdapter.this.updateTroopCount((long) msg.obj);
                    break;
                case 2:
                    TroopMessage tm = (TroopMessage) msg.obj;
                    TroopAdapter.this.updateTroopIcon(tm.id, getCircleBitmap(tm.icon,14));
                    break;
                default:
                    break;
            }
        }

        public void notifyDataSetChange()
		{
            sendEmptyMessage(0);
        }

        public void updateTroopCount(long id)
		{
            Message m = Message.obtain();
            m.what = 1;
            m.obj = id;
            sendMessage(m);
        }

        public void updateTroopIcon(long id, Bitmap map)
		{
            Message m = Message.obtain();
            m.what = 2;
            m.obj = new TroopMessage(id, map);
            sendMessage(m);
        }
    }

    private static final int LAYOUT_TROOP = R.layout.item_troop;
    private static final int ID_ICON = R.id.icon;
    private static final int ID_TITLE = R.id.title;
    private static final int ID_NUMBER = R.id.number;
    private static final int ID_OPRATION = R.id.opration;
    private static final int ID_COUNT = R.id.count;
    private final LayoutInflater inflater;

    private final TroopAdapterHandler handler = new TroopAdapterHandler();

    public TroopAdapter(Context context)
	{
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
	{
        return TroopManager.getInstance().troop.size();
    }

    @Override
    public Troop getItem(int arg0)
	{
        return TroopManager.getInstance().troop.get(arg0);
    }

    @Override
    public long getItemId(int position)
	{
        Troop i = getItem(position);
        return i == null ? 0 : i.id;
    }

    @Override
    public int getItemViewType(int position)
	{
        return getItem(position).type;
    }

    private Troop getTroop(long id)
	{
        Iterator<Troop> it = TroopManager.getInstance().troop.iterator();
        while (it.hasNext())
		{
            Troop t = it.next();
            if (t.id == id)
                return t;
        }
        return null;
    }

	
	public Bitmap getCircleBitmap(Bitmap bitmap, int n) {
        Bitmap bitmap2 = Bitmap.createBitmap((int)bitmap.getWidth(), (int)bitmap.getHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        n = bitmap.getWidth();
        canvas.drawCircle((float)(n / 2), (float)(n / 2), (float)(n / 2), paint);
        paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return bitmap2;
	}
	
	
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
	{
		System.out.println(position+" "+getItem(position).opration);
		ItemHolder holder = null;
        if (convertView == null)
		{
            TroopAdapter.ItemHolder holder2 = new ItemHolder();
            convertView = inflater.inflate(LAYOUT_TROOP, null);
            holder2.icon = (ImageView) convertView.findViewById(ID_ICON);
            holder2.count = (TextView) convertView.findViewById(ID_COUNT);
            holder2.id = (TextView) convertView.findViewById(ID_NUMBER);
            holder2.title = (TextView) convertView.findViewById(ID_TITLE);
            holder2.opration = (Switch) convertView.findViewById(ID_OPRATION);
            convertView.setTag(holder2);
			holder = holder2;
        }
		else
		{
            holder = (ItemHolder) convertView.getTag();
        }
		//holder.opration.removeCheckChangeListener();
        final Troop item = getItem(position);
        if (item != null)
		{
            holder.count.setText(String.valueOf(item.count));
            if (item.icon != null)
			{
                holder.icon.setImageBitmap(item.icon);
            }
			else
			{
                holder.icon.setImageResource(R.drawable.face_troop_default);
                if (!item.loadlost)
				{
                    item.loadlost = true;
                    BitmapUtil.load(item.id + "", getItemViewType(position), new BitmapCallback() {
							public void callBack(Bitmap bit)
							{
								if (bit != null)
								{
									updateIcon(item.id, bit);
								}
							}
						});
                }
            }
            holder.id.setText(String.valueOf(item.id));
            holder.title.setText(item.name);
			
			holder.opration.setOnCheckedChangeListener(item);
			
            holder.opration.setChecked(item.opration);
            
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount()
	{
        return 2;
    }

    public void notifyChange()
	{
        handler.notifyDataSetChange();
    }

    public void updataCount(long id)
	{
        handler.updateTroopCount(id);
    }

    public void updateIcon(long id, Bitmap bit)
	{
        handler.updateTroopIcon(id, bit);
    }

    private boolean updateTroopCount(long id)
	{
        Troop item = getTroop(id);
        if (item == null)
            return false;
        item.count += 1;
        notifyDataSetChanged();
        return true;
    }

    private boolean updateTroopIcon(long id, Bitmap icon)
	{
        Troop item = getTroop(id);
        if (item == null)
            return false;
        item.icon = icon;
        notifyDataSetChanged();
        return true;
    }

}
