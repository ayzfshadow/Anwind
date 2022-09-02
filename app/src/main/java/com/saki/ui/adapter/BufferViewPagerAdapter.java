package com.saki.ui.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class BufferViewPagerAdapter extends PagerAdapter {
    private final View[] bufferView = new View[4];
    
    protected Context mContext;

    public BufferViewPagerAdapter(Context context) {
     
        this.mContext = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(bufferView[position % 4]);
    }

    
	

    protected abstract View getBufferView(View v, int position);

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int index = position % 4;
        View v = getBufferView(bufferView[index], position);
        container.addView(v);
        return v;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void notifyDataSetChanged() {
        
        super.notifyDataSetChanged();
    }
}
