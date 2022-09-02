package com.saki.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ayzf.anwind.R;
import com.saki.qq.global.TroopManager;
import com.saki.qq.wtlogin.pack.jce.Member;
import com.saki.qq.wtlogin.pack.jce.Troop;
import com.saki.qq.wtlogin.unpack.ReMessageSvc.PbGetMsg;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.PbPushDisMsg;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.PbPushGroupMsg;
import com.saki.qq.wtlogin.unpack.ReOnlinePush.PbPushTransMsg;
import com.saki.qq.wtlogin.unpack.ReProfileService.Pb.ReqSystemMsgNew.Group.SysMsg;
import com.saki.service.NewConnection;
import com.saki.service.NewService;
import com.saki.ui.adapter.TroopAdapter;
import com.saki.ui.i.IB;
import com.saki.ui.listener.IMessageListener;
import com.saki.ui.listener.ITroopListener;
import com.setqq.free.Msg;
import java.util.ArrayList;

public class TroopUI extends RelativeLayout  implements IB,
        OnItemClickListener, OnRefreshListener, IMessageListener,
        ITroopListener {

    private String[] tost = {"请打开开关后重试^_^", "开关在最右边呢^_^", "不打开开关的话，你再怎么点也没有反应哦！", "你是笨蛋吗？再戳咱可报警了哦。"};
    private int index = 0;
    public TroopAdapter adapter;
    public SwipeRefreshLayout swipe;

	private LayoutInflater inflater;

	

	private NewConnection connection;

	private ListView list;
	

    public TroopUI(Context context) {
        super(context);
		inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_gruop, this);
        initList(context);
        context.bindService(new Intent(context, NewService.class),
							connection = new NewConnection(this), Context.BIND_AUTO_CREATE);
    }

    public void addMessage(long id) {
        adapter.updataCount(id);
    }

 
    @SuppressLint("InlinedApi")
    protected void initList(Context context) {
        this. list = (ListView) findViewById(R.id.group);
        adapter = new TroopAdapter(context);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipe.setOnRefreshListener(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        TroopManager.getInstance().registerListener(this);
        
    }

    
    public void onDestroy() {
        if (adapter != null)
            TroopManager.getInstance().removeListener(this);
    }

    @Override
    public void onDisMsg(PbPushDisMsg msg) {
    }

    @Override
    public void onGroupMsg(PbPushGroupMsg msg) {
        addMessage(msg.groupId);
    }

    @Override
    public void onItemClick(AdapterView<?> list, View view, int index, long id) {
        if (TroopManager.getInstance().isOPen(id)) {
            Msg m = new Msg();
//            m.addMsg("img","http://wx1.sinaimg.cn/mw690/0060lm7Tly1fs0kiykz90j30qo0w0aea.jpg");
            m.addMsg("msg", "[DB]:此消息仅为测试使用，软件正常运行中...\nBy Anwind(暗影之风)");
            getService().sendMsg(-1, id, -1, m.getData());
            Toast.makeText(getContext(), "测试消息已经发送了哦\n如果没有收到，那就重新登录吧", Toast.LENGTH_LONG).show();
            //Toast.makeText(getContext(), "点我没有任何效果", Toast.LENGTH_LONG).show();
        } else {
			if (this.index >= this.tost.length) {
                this.index = 0;
            }
            Toast.makeText(getContext(), this.tost[this.index], Toast.LENGTH_SHORT).show();
            this.index++;
            
        }

    }
	
	public NewService getService() {
        return connection.getService();
    }

    @Override
    public void onOtherMsg(PbGetMsg msg) {
    }

	
	
    @Override
    public void onRefresh() {
        getService().getGroupList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
            }
        }, 15000);
    }

    @Override
    public void onSystemMsg(SysMsg msg) {
    }

    @Override
    public void onTransMsg(PbPushTransMsg msg) {
    }

    @Override
    public void onTroop(ArrayList<Troop> list) {
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
            }
        });
        adapter.notifyChange();
    }

    @Override
    public void onTroopMember(long id, ArrayList<Member> list) {
    }

    @Override
    public void onBind(NewService service) {
        service.getGroupList();
    }
	
	
	
}
