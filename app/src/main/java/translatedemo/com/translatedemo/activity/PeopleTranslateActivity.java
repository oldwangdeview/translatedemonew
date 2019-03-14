package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.util.ToastUtils;

/**
 * Created by oldwang on 2019/1/24 0024.
 */

public class PeopleTranslateActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_peopletranslate);

    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.pople_translate_text_titlenam));
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context mContext){
        Intent mintent = new Intent(mContext,PeopleTranslateActivity.class);
        mContext.startActivity(mintent);
    }
    @OnClick(R.id.open_qq)
    public void openqq(){
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=42556986";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }catch (Exception e){
            ToastUtils.makeText("打开失败请检查是否安装QQ");
        }
    }

    @OnClick(R.id.open_wechart)
    public void openwechart(){
        try {
            String url = "weixin://";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }catch (Exception e){
            ToastUtils.makeText("打开失败请检查是否安装微信");
        }

    }

}
