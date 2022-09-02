package com.saki.qq.global;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.saki.log.Log;
import com.saki.qq.wtlogin.unpack.ReMessageSvc.PbGetMsg;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.PbPushDisMsg;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.PbPushGroupMsg;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.PbPushTransMsg;
import com.saki.qq.wtlogin.unpack.ReProfileService.Pb.ReqSystemMsgNew.Group.SysMsg;
import com.saki.ui.listener.IMessageListener;

public class MsgManager implements IMessageListener {
    private static final MsgManager manag = new MsgManager();

    public static MsgManager getInstance() {
        return manag;
    }

    private List<IMessageListener> list = new CopyOnWriteArrayList<IMessageListener>();

    public MsgManager() {
        registerMsgListener(PluginManager.getInstance());
    }

    @Override
    public void onDisMsg(PbPushDisMsg msg) {
        for (IMessageListener m : this.list)
            m.onDisMsg(msg);
        Log.i(this, "来自讨论组:[" + msg.groupName + "](" + msg.groupId + ")[" + "【"
                + msg.sendTitle + "】" + msg.fromName + "](" + msg.fromUin
                + "):" + msg.msg);
    }

    @Override
    public void onGroupMsg(PbPushGroupMsg msg) {
        for (IMessageListener m : this.list)
            m.onGroupMsg(msg);
        Log.i(this, "来自群:[" + msg.groupName + "](" + msg.groupId + ")[" + "【"
                + msg.sendTitle + "】" + msg.fromName + "](" + msg.fromUin
                + "):" + msg.msg);
    }

    @Override
    public void onOtherMsg(PbGetMsg msgs) {
        // Msg m=null;
        // for(Msg m2:msgs.msgList)
        // {
        // //Log.i(MsgManager.getInstance(), m2.toString());
        // if(m2.type==PbGetMsg.T_MSG||m2.type==PbGetMsg.T_SESS&&m2.sendTime>time)
        // {
        // m=m2;
        // time=m2.sendTime;
        // }
        // }
        // if(m!=null){
        // msgs.msgList.clear();
        // msgs.msgList.add(m);
        // }
        Log.i(this,"其他消息");
        for (IMessageListener m1 : this.list)
            m1.onOtherMsg(msgs);
    }

    @Override
    public void onSystemMsg(SysMsg msg) {
        for (IMessageListener m : this.list)
            m.onSystemMsg(msg);
    }

    @Override
    public void onTransMsg(PbPushTransMsg msg) {
        for (IMessageListener m : this.list)
            m.onTransMsg(msg);
    }

    public void registerMsgListener(IMessageListener m) {
        if (!list.contains(m))
            list.add(m);
    }

    public void removeMsgListener(IMessageListener m) {
        list.remove(m);
    }
}
