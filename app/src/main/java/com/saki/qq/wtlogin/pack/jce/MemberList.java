package com.saki.qq.wtlogin.pack.jce;

import com.qq.taf.jce.JceInputStream;
import java.util.ArrayList;

public class MemberList extends ReadJceStruct {

    public long groupid;
    public long code;
    public ArrayList<Member> list = new ArrayList<Member>();

    @Override
    public void readFrom(JceInputStream in) {
        groupid = in.read(Long.MAX_VALUE, 1, true);
        code = in.read(Long.MAX_VALUE, 2, true);
        list.add(new Member());
        list = (ArrayList<Member>) in.readobj(list, 3, true);
    }

}
