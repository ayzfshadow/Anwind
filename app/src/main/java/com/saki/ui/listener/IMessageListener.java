package com.saki.ui.listener;

import com.saki.qq.wtlogin.unpack.ReMessageSvc.PbGetMsg;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.PbPushDisMsg;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.PbPushGroupMsg;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.PbPushTransMsg;
import com.saki.qq.wtlogin.unpack.ReProfileService.Pb.ReqSystemMsgNew.Group.SysMsg;

public interface IMessageListener {
    // public void onTroopList(TroopList list);

    public void onDisMsg(PbPushDisMsg msg);

    public void onGroupMsg(PbPushGroupMsg msg);

    public void onOtherMsg(PbGetMsg msg);

    // public void onFriendGroupList();
    public void onSystemMsg(SysMsg msg);

    public void onTransMsg(PbPushTransMsg msg);
}
