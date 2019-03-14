package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.bean.InputHistoryBean;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

public class HisotryAdpater extends BaseAdapter {
    public List<InputHistoryBean> listdata = new ArrayList<>();
    Context mContext;
    ListOnclickLister lister = null;
    public HisotryAdpater ( List<InputHistoryBean> listdata,Context mContext){
        this.listdata = listdata;
        this.mContext = mContext;

    }
    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = UIUtils.inflate(mContext, R.layout.item_history);
        }
        TextView content = convertView.findViewById(R.id.content);
        content.setText(listdata.get(position).inoutdata);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister!=null){
                    lister.onclick(v,position);
                }
            }
        });

        return convertView;
    }

    public void setonclicklister(ListOnclickLister lister){
        this.lister = lister;
    }
}
