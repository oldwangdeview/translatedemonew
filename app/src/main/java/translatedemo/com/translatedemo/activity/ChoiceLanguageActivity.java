package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.OverMainactivty;
import translatedemo.com.translatedemo.util.PreferencesUtils;

/**
 * Created by Administrator on 2018/12/26 0026.
 */

public class ChoiceLanguageActivity extends BaseActivity{

    private int type = -1;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_choicelanguage);
    }

    @OnClick({R.id.chunaese,R.id.zangwen})
    public void onclick(View view){
        SharedPreferences preferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
      switch (view.getId()){
          case R.id.chunaese:
              editor.putString("language", "zh");

              break;
          case R.id.zangwen:
              editor.putString("language", "zh-hant");
              break;
      }
        editor.apply();
      if(type>0) {
          EventBus.getDefault().post(new OverMainactivty());
      }
      if(BaseActivity.getuser()!=null){
          MainActivity.startactivity(this,1);
      }else {
          LoginActivity.startactivity(this);
      }
        finish();
    }

    @Override
    protected void initData() {
        super.initData();
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,-1);
        if(type<=0) {
            SharedPreferences preferences = this.getSharedPreferences("language", Context.MODE_PRIVATE);
            String selectedLanguage = preferences.getString("language", "");
            if (!TextUtils.isEmpty(selectedLanguage)) {
                if (!TextUtils.isEmpty(PreferencesUtils.getInstance().getString(BaseActivity.LOGINUSER, ""))) {
                    MainActivity.startactivity(this, 1);
                    finish();
                } else {
                    LoginActivity.startactivity(this);
                    finish();
                }
            }else{
                SharedPreferences preferences1 = getSharedPreferences("language", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences1.edit();
                editor.putString("language", "zh");
                editor.apply();
                if(BaseActivity.getuser()!=null){
                    MainActivity.startactivity(this,1);
                }else {
                    LoginActivity.startactivity(this);
                }
            }
        }

    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent( mContext,ChoiceLanguageActivity.class);
        mContext.startActivity(mIntent);
    }
    public static void startactivity(Context mContext,int type){
        Intent mIntent = new Intent( mContext,ChoiceLanguageActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mContext.startActivity(mIntent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateactionbar();
    }

}
