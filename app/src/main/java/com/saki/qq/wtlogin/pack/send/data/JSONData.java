package com.saki.qq.wtlogin.pack.send.data;

import com.saki.log.SQLog;
import com.saki.qq.wtlogin.pack.ByteWriter;
import com.saki.qq.wtlogin.pack.protobuff.ProtoBuff;
import com.saki.tools.Zip;

public class JSONData extends MsgData
 {
    private byte[] a;

    public JSONData(String str) {
		SQLog.w("json为特别增加的体验功能，更多功能请使用付费版v9或者vx");
		
        this.a = new ByteWriter().writePb(2, ProtoBuff.writeLengthDelimt(
											  51, new ByteWriter()
											  .writePb(1, new ByteWriter()
												  .writeByte((byte)1)
												  .writeBytes(Zip.inflate(str.getBytes()))
												  .getDataAndDestroy())
										 .writePb(2, 2).getDataAndDestroy())).getDataAndDestroy();
    }

    public byte[] getBytes() {
        return this.a;
    }
}

