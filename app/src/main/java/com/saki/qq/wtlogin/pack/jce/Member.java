package com.saki.qq.wtlogin.pack.jce;

import com.qq.taf.jce.JceInputStream;

public class Member extends ReadJceStruct {

    public long uin;
    public String nick;
    public String card;
    public String title;

    @Override
    public void readFrom(JceInputStream in) {
        uin = in.read(Long.MAX_VALUE, 0, true);
        nick = in.read("", 4, true);
        card = in.read("", 8, true);
        title = in.read("", 23, true);
    }
}
