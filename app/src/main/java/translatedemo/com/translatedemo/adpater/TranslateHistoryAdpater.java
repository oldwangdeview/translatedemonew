package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseRecycleAdapter;
import translatedemo.com.translatedemo.bean.Translatedc_Bean;
import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;

public class TranslateHistoryAdpater extends BaseRecycleAdapter<Translatedc_Bean> {

    ListOnclickLister mlister;
    TextView content;
    public TranslateHistoryAdpater(Context context, List<Translatedc_Bean> datas) {
        super(context, datas, R.layout.item_history);
    }

    @Override
    protected void setData(RecycleViewHolder holder, Translatedc_Bean translatedc_bean, final int position) {


        content = holder.getItemView(R.id.content);
        if(!TextUtils.isEmpty(translatedc_bean.inputdata)){
            content.setText(translatedc_bean.inputdata);
        }
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

    }


    public void setonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
