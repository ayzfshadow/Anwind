package com.saki.service;

import com.saki.client.ServiceNotification;

/**
 * Created by Saki on 2018/7/30.
 */

public class KeepTimeCountThread extends Thread {
    ServiceNotification sn;
    public KeepTimeCountThread(ServiceNotification sn){
        this.sn = sn;
    }
    @Override
    public void run() {
        int cnt = 0;
        if(sn!=null)
            sn.pushMessage("后台运行中");
        while(sn != null){
            try {
                Thread.sleep(60000);
                cnt++;
                sn.pushMessage("已运行:"+cnt+"分钟");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
