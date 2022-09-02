package com.saki.ui.listener;

import java.util.ArrayList;

import com.saki.qq.wtlogin.pack.jce.Member;
import com.saki.qq.wtlogin.pack.jce.Troop;

public interface ITroopListener {
    public void onTroop(ArrayList<Troop> list);

    public void onTroopMember(long id, ArrayList<Member> list);
}
