package com.saki.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public abstract class PromoteListUI {
    private View contentView;
    private ViewPager promote;
    private ListView list;
    protected LayoutInflater inflater;
    protected Context mContext;

    public PromoteListUI(Context context, int id) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(id, null);
        promote = initPromote(context);
        list = initList(context);
    }

    public View getContentView() {
        return contentView;
    }

    public ListView getListView() {
        return list;
    }

    public ViewPager getPromote() {
        return promote;
    }

    protected abstract ListView initList(Context c);

    protected abstract ViewPager initPromote(Context c);
}
