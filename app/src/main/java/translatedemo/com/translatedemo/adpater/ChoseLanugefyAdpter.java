package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.bean.LanuageListBean;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

public class ChoseLanugefyAdpter extends BaseAdapter {

    private List<LanuageListBean> datas;
    private Context mContext;
    private TextView name;
    public static HashMap<Integer,Boolean> click = new HashMap<>();
    View view;
    public ChoseLanugefyAdpter(Context mCOntext,List<LanuageListBean> datas){
        this.mContext = mCOntext;
        this.datas = datas;
    }
    @Override
    public int getCount() {
        return datas.size();
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
            convertView = UIUtils.inflate(mContext, R.layout.item_ducation);
        }
        name = convertView.findViewById(R.id.name);
        view = convertView.findViewById(R.id.view);
        name.setText(datas.get(position).name);

        if(click.get(position)){
            name.setTextColor(mContext.getResources().getColor(R.color.c_e94950));
        }else{
            name.setTextColor(mContext.getResources().getColor(R.color.c_000000));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        if(position==datas.size()-1){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
    ListOnclickLister mlister;
    public void steonitemclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

    public void setclickallfalse(){
        for(int i =0;i<datas.size();i++){
            click.put(i,false);
        }
    }
    public void setclickd(String data){

        for(int i =0;i<datas.size();i++){
            if(datas.get(i).name.equals(data)){
                click.put(i,true);
            }else{
                click.put(i,false);
            }
        }
        notifyDataSetChanged();


    }
}


