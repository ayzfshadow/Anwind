package com.saki.qq.wtlogin.pack.jce;

import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceOutputStream;
import com.qq.taf.jce.JceStruct;
import java.util.ArrayList;

public class TroopList extends JceStruct {
    long id;
    int listCnt;
    public ArrayList<Troop> list = new ArrayList<Troop>();

    @Override
    public void readFrom(JceInputStream in) {
        id = in.read(id, 0, true);
        listCnt = in.read(listCnt, 1, true);
        list.add(new Troop());
        list = (ArrayList<Troop>) in.readobj(list, 5, true);
    }

    @Override
    public void writeTo(JceOutputStream out) {
    }
}
