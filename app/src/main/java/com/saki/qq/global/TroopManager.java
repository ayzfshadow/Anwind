package com.saki.qq.global;

import java.util.ArrayList;

import com.saki.log.Log;
import com.saki.qq.wtlogin.pack.jce.Member;
import com.saki.qq.wtlogin.pack.jce.Troop;
import com.saki.ui.listener.ITroopListener;

public class TroopManager implements ITroopListener {
    private static TroopManager manage = new TroopManager();
    public ArrayList<Troop> troop = new ArrayList<Troop>();
    private static ArrayList<ITroopListener> l = new ArrayList<ITroopListener>();

    public static TroopManager getInstance() {
        return manage;
    }

    public ArrayList<Member> getMemberInfo(long id) {
        for (Troop t : troop) {
            if (t.id == id)
                return t.members;
        }
        return null;
    }

    public ArrayList<Troop> getTroopInfo() {
        return troop;
    }

    public long id2code(long id) {
        for (Troop info : troop) {
            if (info.id == id)
                return info.code;
        }
        return id;
    }

    public boolean isOPen(long id) {
        for (Troop info : troop) {
            if (info.id == id)
                return info.opration;
        }
        return false;
    }

    @Override
    public void onTroop(ArrayList<Troop> list) {
        for (Troop info : troop) {
            for (Troop info2 : list) {
                if (info.equals(info2)) {
                    info2.opration = info.opration;
                    info2.count = info.count;
                }
            }
        }
        troop = list;
        for (ITroopListener i : l) {
            i.onTroop(troop);
        }
    }

    @Override
    public void onTroopMember(long id, ArrayList<Member> list) {
        Log.i(this, "id=" + id);
        for (Member m : list) {
            Log.i(this, m.uin + "");
        }
        for (Troop t : getTroopInfo()) {
            if (t.id == id)
                t.members = list;
        }
    }

    public void registerListener(ITroopListener t) {
        if (!l.contains(t))
            l.add(t);
    }

    public void removeListener(ITroopListener t) {
        l.remove(t);
    }
}
