package com.saki.service;

import com.saki.client.IClient;
import com.saki.encode.Canvart;
import com.saki.log.Log;
import com.saki.qq.global.Key;
import com.saki.qq.wtlogin.pack.send.StatSvc;

public class SimpleGetThread implements Runnable {
    private IClient a;

    public SimpleGetThread(IClient classc) {
        this.a = classc;
    }

    public void run() {

		this.a.send(new StatSvc.SimpleGet(this.a.getRequstionID()).toByteArray());

	}

}

