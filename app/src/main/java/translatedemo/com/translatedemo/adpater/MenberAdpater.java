package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseRecycleAdapter;
import translatedemo.com.translatedemo.bean.DictionaryBean;
import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.util.UIUtils;

public class MenberAdpater extends BaseAdapter {

    List<DictionaryBean> datas = new ArrayList<>();

    Context mContext;
    public MenberAdpater(Context context, List<DictionaryBean> datas) {

        this.datas = datas;
        this.mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView=UIUtils.inflate(mContext,R.layout.item_menbercidian);
        }
        ImageView image = convertView.findViewById(R.id.image);
        TextView content = convertView.findViewById(R.id.content);
        DictionaryBean s = datas.get(position);
        if(!TextUtils.isEmpty(s.image)){

            UIUtils.loadImageViewRoud(mContext,s.image,image,UIUtils.dip2px(5));

        }

        if(!TextUtils.isEmpty(s.name)){
            content.setText(s.name);
        }

        return convertView;
    }
}

