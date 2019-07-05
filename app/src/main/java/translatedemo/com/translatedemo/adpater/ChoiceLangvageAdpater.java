package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/18 0018.
 */

public class ChoiceLangvageAdpater extends BaseAdapter {
    String[]data;
    int[]images;
    Context mContext;
    TextView name;
    ImageView image;
    ImageView choice_image;
    View view_view;

    static HashMap<Integer,Boolean> clickdata = new HashMap<>();
    public ChoiceLangvageAdpater(Context mContext,String[] listdat,int[]images){
        this.mContext = mContext;
        this.data = listdat;
        this.images = images;
        setalldatafalse();
    }
    @Override
    public int getCount() {
        return data.length;
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
            convertView = UIUtils.inflate(mContext, R.layout.item_choicelangvage);
        }
        name = convertView.findViewById(R.id.name);
        image = convertView.findViewById(R.id.image);
        choice_image = convertView.findViewById(R.id.choice_image);
        view_view = convertView.findViewById(R.id.view_view);
        if(clickdata.get(position)){
            choice_image.setVisibility(View.VISIBLE);
        }else{
            choice_image.setVisibility(View.GONE);
        }
        if(position==data.length-1){
            view_view.setVisibility(View.GONE);
        }
        if(position<images.length) {
            image.setImageResource(images[position]);
        }else{
            image.setImageResource(R.mipmap.buffer);
        }

        name.setText(data[position]);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mlister!=null){
                        setalldatafalse();
                        clickdata.put(position,true);
                        mlister.onclick(v,position);
                        notifyDataSetChanged();
                    }
                }
            });


        return convertView;
    }
    ListOnclickLister mlister;
    public void steonitemclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

    private void setalldatafalse(){
        for(int i =0;i<data.length;i++){
            clickdata.put(i,false);
        }
    }


    public void setclick(String mdata){

        for(int i =0;i<data.length;i++){

            if(data[i].equals(mdata)){
                clickdata.put(i,true);

            }else{
                clickdata.put(i,false);
            }
        }
        notifyDataSetChanged();

    }

}
