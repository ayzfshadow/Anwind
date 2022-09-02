package com.setqq.free.sdk;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import com.setqq.free.Msg;

public interface IPlugin {
    void onLoad(Context context, PluginApiInterface api);

    void onMessageHandler(Msg pluginMsg);

    Intent getActivity(Context q);

    Drawable getIcon(Context a);
	
	void onDestroy();
	
}
