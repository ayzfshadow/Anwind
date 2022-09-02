package com.setqq.free.sdk;

import com.saki.log.SQLog;
import com.saki.qq.global.Global;
import com.saki.qq.global.TroopManager;
import com.saki.qq.wtlogin.pack.jce.Member;
import com.saki.qq.wtlogin.pack.jce.Troop;
import com.saki.service.NewService;
import com.setqq.free.Msg;
import java.util.ArrayList;
import java.util.HashMap;

public class PluginApi implements com.setqq.free.sdk.PluginApiInterface
{

    @Override
    public void log(int tag,String data)
    {
        switch (tag)
        {
            case 0:
                SQLog.i(data);
                break;
            case 1:
                SQLog.w(data);
                break;
            case 2:
                SQLog.e(data);
                break;
        }

    }



	@Override
    public Msg send(Msg msg)
    {
        NewService context=NewService.getService();
        // System.out.println(msg.data.toString());

        switch (msg.type)
        {
            case Msg.TYPE_GROUP_MSG:
                context.sendMsg(-1,msg.groupid,-1,msg.getData());
                break;
            case Msg.TYPE_BUDDY_MSG:
                context.sendMsg(-1,-1,msg.uin,msg.getData());
                break;
            case Msg.TYPE_DIS_MSG:
                context.sendMsg(msg.groupid,-1,-1,msg.getData());
                break;
            case Msg.TYPE_SESS_MSG:
                if (msg.uin < 9999 || msg.code < 9999)
                    return null;
                context.sendMsg(-1,msg.code,msg.uin,msg.getData());
                break;
            case Msg.TYPE_SET_GROUP_SHUTUP:
                context.setShutup(msg.groupid,msg.value == 0);
                break;
            case Msg.TYPE_SET_MEMBER_SHUTUP:
                context.setShutup(msg.groupid,msg.uin,msg.value);
                break;
            case Msg.TYPE_FAVORITE:
                context.putFavorite(msg.uin);
                break;
            case Msg.TYPE_SET_MEMBER_CARD:
                context.setMemberCard(msg.groupid,msg.uin,msg.title);
                break;
            case Msg.TYPE_DELETE_MEMBER:
                context.deleteGroupMember(msg.groupid,msg.uin);
                break;
            case Msg.TYPE_GET_GROUP_LIST:
                ArrayList<Troop> list = TroopManager.getInstance().getTroopInfo();
                HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
                ArrayList<String> troop = new ArrayList<String>();
                for (Troop i : list)
                {
                    troop.add(i.id + "");
                }
                map.put("troop",troop);
                msg.setData(map);
                return msg;
            case Msg.TYPE_GET_GROUP_MEBMER:
                ArrayList<Member> m = TroopManager.getInstance().getMemberInfo(
					msg.groupid);
                context.getGroupMember(msg.groupid);
                if (m != null)
                {
                    map=new HashMap<String, ArrayList<String>>();
                    ArrayList<String> member = new ArrayList<String>();

                    for (Member i : m)
                    {
                        member.add(i.uin + "");
                    }
                    map.put("member",member);
                    msg.setData(map);
                    return msg;
                }
                return null;
            case Msg.TYPE_GET_LOGIN_ACCOUNT:
                msg.uin=Global.qq;
                return msg;
            case Msg.TYPE_AGREE_JOIN:
                context.agreeJoin(msg.groupid,msg.code,msg.uin,msg.value == 0);
                break;
            default:
                SQLog.i(msg.getTextMsg());
                break;
        }
        return null;
    }
}

