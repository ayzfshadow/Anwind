package com.saki.qq.wtlogin.pack.jce;

import android.graphics.Bitmap;
import com.qq.taf.jce.JceInputStream;
import com.saki.ui.SwitchButton;
//import com.saki.ui.SwitchButton.OnCheckedChangeListener;
import java.util.ArrayList;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;

public class Troop extends ReadJceStruct implements OnCheckedChangeListener
{

	@Override
	public void onCheckedChanged(CompoundButton p1, boolean p2)
	{
		opration=p2;
	}


    public long code;
    public long id;
    public String name = "";
    public String info = "";
    public int memberCnt;
    public int type;
    public int count;
    public Bitmap icon;
    public boolean loadlost;
    public boolean opration;
    public ArrayList<Member> members;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Troop)
            return ((Troop) o).id == this.id;
        return false;
    }

    

    //@Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        this.opration = isChecked;
    }

    @Override
    public void readFrom(JceInputStream in) {
        code = in.read(code, 0, true);
        id = in.read(id, 1, true);
        name = in.read(name, 4, true);
        info = in.read(info, 5, true);
        memberCnt = in.read(memberCnt, 19, true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code:" + code + "\r\n");
        sb.append("Id:" + id + "\r\n");
        sb.append("Name:" + name + "\r\n");
        sb.append("Info:" + info + "\r\n");
        sb.append("MemberCnt::" + memberCnt);
        return sb.toString();
    }
}
