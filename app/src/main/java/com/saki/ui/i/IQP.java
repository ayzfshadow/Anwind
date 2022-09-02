package com.saki.ui.i;

import java.util.ArrayList;
import java.util.HashMap;

public interface IQP {
    public void agreeJoin(long group, long uin, long reqid, boolean agree);

    public void deleteGroupMember(long group, long uin);

    public void getFriendList();

    public void getGroupList();

    public void getGroupMember(long group);

    public void login(String id, String password, int version);

    public void putFavorite(long uin);

    public void sendMsg(long id, long group, long uin,
                        HashMap<String, ArrayList<String>> data);

    public void setMemberCard(long group, long uin, String card);

    public void setShutup(long group, boolean open);

    public void setShutup(long group, long uin, int time);

    public void verifyCode(String value);
}
