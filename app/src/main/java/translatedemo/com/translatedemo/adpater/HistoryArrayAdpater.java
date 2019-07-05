package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.bean.HistoryBean;
import translatedemo.com.translatedemo.bean.HistoryBean2;
import translatedemo.com.translatedemo.interfice.HistoryListOnclickLister;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.interfice.TextonClickLister;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.UIUtils;

public class HistoryArrayAdpater extends BaseAdapter implements Filterable {
    List<HistoryBean.HistoryListBean> listdata = new ArrayList<>();
   public  List<HistoryBean.HistoryListBean> nowlistdata = new ArrayList<>();
    Context mContext;
    public HistoryArrayAdpater(Context mContext, List<HistoryBean.HistoryListBean> listdata){
        this.listdata = listdata;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return nowlistdata.size();
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
        TextView text = convertView.findViewById(R.id.content );
        if(!TextUtils.isEmpty(nowlistdata.get(position).content)) {
            text.setText(nowlistdata.get(position).content);
        }else if(!TextUtils.isEmpty(nowlistdata.get(position).contentOne)){
            text.setText(nowlistdata.get(position).contentOne);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.click(nowlistdata.get(position));
                }
            }
        });
        return convertView;
    }
    HistoryListOnclickLister mlister;
    public void setlistonclicklister(HistoryListOnclickLister mlister){
        this.mlister = mlister;
    }

    public TextonClickLister mtextlister;
    public void setinoutdatalister(TextonClickLister mtextlister){
        this.mtextlister = mtextlister;

    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<HistoryBean.HistoryListBean> newData = new ArrayList<>();
                if(constraint != null ){
                  if(constraint.toString().length()==1){
                      newData.addAll(listdata);
                  }
                    if(mtextlister!=null){
                        mtextlister.clickText(constraint.toString().trim());
                    }
                }else{
                    newData.addAll(listdata);
                }
                results.values = newData;
                results.count = newData.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                nowlistdata = (ArrayList)results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public void setlistedata(List<HistoryBean2> data){
        nowlistdata.clear();
        for(int i =0;i<data.size();i++){
            nowlistdata.add(new HistoryBean.HistoryListBean(data.get(i).contentOne,data.get(i).contentTwo,data.get(i).dictionaryId,data.get(i).type,data.get(i).image));
        }
        notifyDataSetChanged();
    }



}
