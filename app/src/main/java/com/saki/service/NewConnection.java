package com.saki.service;

import com.saki.ui.i.IB;
import com.saki.ui.i.IUI;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class NewConnection implements ServiceConnection {
    private NewService service;
    private IUI ui;
    private IB b;

    public NewConnection(IUI ui) {
        this.ui = ui;
    }

    public NewConnection(IB b) {
        this.b = b;
    }

    public NewService getService() {
        return service;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.service = ((NewService.NewBinder) service).getService();
        this.service.setUpdataListener(ui);
        if (b != null)
            b.onBind(getService());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.service.setUpdataListener(null);
        this.ui = null;
        this.service = null;
    }
}
