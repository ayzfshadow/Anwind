package com.saki.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.saki.log.SQLog;
import com.ayzf.anwind.R;
import java.util.ArrayList;

public class RuntimeLogUI extends RelativeLayout implements OnClickListener {
    private static class Log {
        int type;
        String context;

        public Log(int type, String contenxt) {
            this.type = type;
            this.context = contenxt;
        }
    }

    class LogAdapter extends BaseAdapter implements OnItemLongClickListener {
        class H {
            TextView title;
            TextView tv;
        }

        private ArrayList<Log> list = new ArrayList<Log>();
        private final LayoutInflater inflater;
        public LogAdapter(Context c){
            inflater = LayoutInflater.from(c);
        }

        public void addLog(Log s) {
            list.add(s);
            notifyDataSetChanged();
        }

        public void clear() {
            list.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Log getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            H h = null;
            if (convertView == null) {
                h = new H();
                convertView = inflater.inflate(R.layout.item_log, null);
                h.title = convertView.findViewById(R.id.title);
                h.tv = convertView.findViewById(R.id.content);
                convertView.setTag(h);
            } else {
                h = (H) convertView.getTag();
            }
            Log log = getItem(position);
            setLog(h.title,h.tv,log);
            return convertView;
        }
        void setLog(TextView v,TextView c, Log l){
            switch (l.type){
                case 0:
                    v.setBackgroundColor(Color.GREEN);
                    v.setText("信息");
                    c.setBackgroundResource(R.drawable.log_green);
                    break;
                case 1:
                    v.setBackgroundColor(Color.YELLOW);
                    v.setText("警告");
                    c.setBackgroundResource(R.drawable.log_yellow);
                    break;
                case 2:
                    v.setBackgroundColor(Color.RED);
                    v.setText("错误");
                    c.setBackgroundResource(R.drawable.log_red);
                    break;
            }
            c.setText(l.context);
        }
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int index, long arg3) {
            Log log = getItem(index);
            ClipboardManager myClipboard = (ClipboardManager) pa
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData myClip;
            myClip = ClipData.newPlainText("msg", log.context);
            myClipboard.setPrimaryClip(myClip);
            Toast.makeText(pa, "复制完成", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private ListView list;
    PagerActivity pa;
    LogAdapter adapter;
    Button clear;
    Button info;
    PopHit hit;
    LogHandler h = new LogHandler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.addLog((Log) msg.obj);
        }

        @Override
        public void Log(int type, Object obj) {
            sendMessage(Message.obtain(this, 0, new Log(type, obj.toString())));
        }
    };

    public RuntimeLogUI(PagerActivity c) {
        super(c);
        pa = c;
        adapter = new LogAdapter(c);
        LayoutInflater.from(c).inflate(R.layout.activity_log, this);
        list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(adapter);
        clear = (Button) findViewById(R.id.logclear);
        info = (Button) findViewById(R.id.publicinfo);
        clear.setOnClickListener(this);
        info.setOnClickListener(this);
        for (String s : SQLog.log) {
            h.sendMessage(Message.obtain(h, 0, s));
        }
        SQLog.setLogListener(h);
    }

    @Override
    public void onClick(View v) {
        if (v == clear) {
            adapter.clear();
        } else if (v == info) {
            if (hit != null) {
                hit.dismiss();
                hit = null;
            }
            hit = new PopHit(pa, pa.getWindow().getDecorView());
        }
    }

    public void onDestroy() {
        SQLog.remove();
    }

}
