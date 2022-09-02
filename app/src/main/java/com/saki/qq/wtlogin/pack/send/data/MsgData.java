package com.saki.qq.wtlogin.pack.send.data;

import com.saki.qq.wtlogin.pack.send.MessageSvc.PbSendMsg;
import com.saki.client.IClient;

public abstract class MsgData implements Comparable<MsgData> {

    public static final String KEY_TEXT = "msg";
	public static final String KEY_IMG = "img";
	public static final String KEY_XML = "xml";
	public static final String KEY_JSON = "json";
    private static MsgData getMsgData(IClient client,int size, int index, PbSendMsg pack, String key, String msg) {
        switch (key) {
            case KEY_TEXT:
                return new TextData(msg).setIndex(index);
			case KEY_XML:
                return new XMLData(msg).setIndex(index);
			case KEY_JSON:
                return new JSONData(msg).setIndex(index);
            default:
                break;
        }
        return null;
    }

    public static void pushMsgData(IClient client,PbSendMsg pack, int size, int index, String key, String msg) {
        pack.addMsg(getMsgData(client,size, index, pack, key, msg));
        if (pack.msgs.size() == size) {
            client.send(pack.toByteArray());
        }
    }

    private int index;

    @Override
    public int compareTo(MsgData another) {
        return index - another.index;
    }

    public abstract byte[] getBytes();

    public MsgData setIndex(int index) {
        this.index = index;
        return this;
    }
}
