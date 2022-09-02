package com.saki.qq.wtlogin.pack.send;

import android.icu.util.Calendar;
import com.saki.encode.Canvart;
import com.saki.qq.global.Global;
import com.saki.qq.wtlogin.pack.ByteWriter;
import com.saki.qq.wtlogin.pack.protobuff.ProtoBuff;
import com.saki.qq.wtlogin.pack.send.data.MsgData;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import com.saki.encode.Code;

public class MessageSvc {
    public static class PbGetMsg extends T0B01 {
        public static final String INFO = "model:HTC Evo - 4.1.1 - SendInterface 16 - 720x1280;os:16;version:v2man:Genymotionsys:vbox86p-userdebug 4.1.1 JRO03S eng.buildbot.20151117.133415 test-keys";
        public static byte[] bin = new byte[0];

        public PbGetMsg(int seq) {
            super("MessageSvc.PbGetMsg");
            setSeq(seq);
        }

        @Override
        protected byte[] getContent() {
            ByteWriter p = new ByteWriter();
            p.writeBytes(ProtoBuff.writeVarint(1, 0));
            p.writeBytes(ProtoBuff.writeLengthDelimt(2, INFO.getBytes()));
            p.writeBytes(ProtoBuff.writeVarint(3, System.currentTimeMillis()));
            p.writeBytes(ProtoBuff.writeVarint(4, System.currentTimeMillis()));
            p.writeBytes(ProtoBuff.writeVarint(5, System.currentTimeMillis()));
            p.writeBytes(ProtoBuff.writeVarint(6, System.currentTimeMillis()));
            p.rewriteBytes(Canvart.Int2Bytes(p.length() + 4));
            p.rewriteBytes(msgCookie);
            p.rewriteBytes(Canvart.Int2Bytes(8));
            p.rewriteBytes(command.getBytes());
            p.rewriteBytes(Canvart.Int2Bytes(command.length() + 4));
            return p.getDataAndDestroy();
            // return Canvart
            // .Hex2Byte("08 00 12 26 08 98 C8 C7 A5 05 10 98 C8 C7 A5 05 28 8A 8A 96 A9 06 30 B5 8B 01 38 8A 8A A6 E1 0E 40 92 8F 01 48 F3 E7 BE 80 0E 18 00 20 14 28 03 30 01 38 01");
        }

        @Override
        protected void packInfo() {
            packContent();
        }

        @Override
        public byte[] toByteArray() {
            packInfo();
            ByteWriter p = new ByteWriter();
            p.writeBytes(ProtoBuff.writeVarint(1, 0));
            p.writeBytes(ProtoBuff.writeLengthDelimt(2, Global.MSGCOOKIE));
            p.writeBytes(ProtoBuff.writeVarint(3, 0));
            p.writeBytes(ProtoBuff.writeVarint(4, 20));
            p.writeBytes(ProtoBuff.writeVarint(5, 3));
            p.writeBytes(ProtoBuff.writeVarint(6, 1));
            p.writeBytes(ProtoBuff.writeVarint(7, 1));
            p.writeBytes(ProtoBuff.writeVarint(9, 0));
            p.rewriteBytes(Canvart.Int2Bytes(p.length() + 4));
            pack.writeBytes(p.getDataAndDestroy());
            packHead();
			byte[]o =pack.getDataAndDestroy();
			System.out.println(Canvart.Bytes2Hex(o));
            return o;
        }
    }

    public static class PbSendMsg extends T0B01 {
        public static final int FRIENDMSG = 1;
        public static final int GROUPMSG = 2;
        public static final int SESSMSG = 3;
        public static final int DISMSG = 4;
        private static int msgSeq = 10000;

		private long packagelength=1;

        private int packageindex=0;

        private long msgid=Code.randomLong();
		

		public void setpackagelength(long packagelength)
        {
            this.packagelength=packagelength;
        }

        public void setpackageindex(int packageindex)
        {
            this.packageindex=packageindex;
        }

        public void setmsgid(long msgid)
        {
            this.msgid=msgid;
        }

        public static final int getMsgSeq() {
            return msgSeq++;
        }

        public long id = -1;
        public long uin = -1;
        public long code = -1;
        public int msgType = 1;

        public ArrayList<MsgData> msgs = new ArrayList<MsgData>();

        public PbSendMsg(int packseq, long id, long code, long uin) {
            super("MessageSvc.PbSendMsg");
            setSeq(getMsgSeq());
            this.id = id;
            this.code = code;
            this.uin = uin;
            if (uin != -1) {
                if (code != -1)
                    msgType = SESSMSG;
                else
                    msgType = FRIENDMSG;
            } else if (id == -1)
                msgType = GROUPMSG;
            else
                msgType = DISMSG;
        }

        public void addMsg(int index, MsgData data) {
            if (data != null && index >= 0) {
                msgs.add(index, data);
            }
        }

        public void addMsg(MsgData data) {
            if (data != null)
                msgs.add(data);
        }

        @Override
        protected byte[] getContent() {
			long time = new Date().getTime() / 1000;
            ByteWriter p = new ByteWriter();
            p.writeBytes(getTarget());
            p.writeBytes(getStyle());
            ByteWriter msg = new ByteWriter();
            for (MsgData data : msgs) {
                msg.writeBytes(data.getBytes());
            }
            byte[] bin = ProtoBuff.writeLengthDelimt(1, msg.getDataAndDestroy());
            bin = ProtoBuff.writeLengthDelimt(3, bin);
            p.writeBytes(bin);
            p.writeBytes(ProtoBuff.writeVarint(4, getMsgSeq()));
            p.writeBytes(ProtoBuff.writeVarint(5,Code.randomLong(
                    )));
            
			p.writeBytes(ProtoBuff.writeLengthDelimt(6, new ByteWriter().writePb(1, time)
													 .writePb(2, time)
													 .writePb(5, Code.randomLong())
													 .writePb(9, Code.randomLong())
													 .writePb(11, Code.randomLong())
													 .writePb(13, time)
													 .writePb(14, 0).getDataAndDestroy()));
            
            int i = 3 - msgType;
            if (i < 0) {
                i = 0;
            }
            p.writeBytes(ProtoBuff.writeVarint(8, 0));
            byte[]o =p.getDataAndDestroy();
			//System.out.println(Canvart.Bytes2Hex(o));
			return o;
        }

        private byte[] getStyle() {
            ByteWriter p = new ByteWriter();
            int i = msgType == FRIENDMSG ? 0 : 1;
			p.writePb(1, this.packagelength);
            p.writePb(2, this.packageindex);
            p.writePb(3, this.msgid);
            return ProtoBuff.writeLengthDelimt(2, p.getDataAndDestroy());
        }

        private byte[] getTarget() {
            if (msgType == SESSMSG) {
                ByteWriter p = new ByteWriter();
                p.writeBytes(ProtoBuff.writeVarint(1, code));
                p.writeBytes(ProtoBuff.writeVarint(2, uin));
                return ProtoBuff.writeLengthDelimt(1,
												   ProtoBuff.writeLengthDelimt(SESSMSG, p.getDataAndDestroy()));
            } else if (msgType == FRIENDMSG) {
                return ProtoBuff.writeLengthDelimt(
                        1,
                        ProtoBuff.writeLengthDelimt(FRIENDMSG,
                                ProtoBuff.writeVarint(1, uin)));
            } else if (msgType == GROUPMSG) {
                return ProtoBuff.writeLengthDelimt(
                        1,
                        ProtoBuff.writeLengthDelimt(GROUPMSG,
                                ProtoBuff.writeVarint(1, code)));
            } else {
                return ProtoBuff.writeLengthDelimt(
                        1,
                        ProtoBuff.writeLengthDelimt(DISMSG,
                                ProtoBuff.writeVarint(1, id)));
            }
        }
		
		public static long getGreenwichTime()
		{
			Date nowTime = new Date(); // 要转换的时间

			Calendar cal = Calendar.getInstance();

			cal.setTimeInMillis(nowTime.getTime());

			cal.add(Calendar.HOUR, -8);

			return cal.getTime().getTime() / 1000;
		}
		
    }
	
	
}
