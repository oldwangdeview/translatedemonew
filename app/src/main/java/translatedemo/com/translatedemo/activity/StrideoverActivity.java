package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.application.BaseApplication;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.TransTypeEvent;
import translatedemo.com.translatedemo.recever.ListenClipboardService;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;

public class StrideoverActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.switchview)
    SwitchView switchview;


    public boolean settype = false;
    @Override
    protected void initView() {

        setContentView(R.layout.activity_strideover);
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(getResources().getString(R.string.strideo_text_titlename));
        switchview.setColor(getResources().getColor(R.color.c_e94950),getResources().getColor(R.color.c_e94950));
        switchview.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                Log.e("booln____1",1+"");
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(StrideoverActivity.this)) {
                        ToastUtils.makeText("请设置悬浮窗权限");
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent);
                        settype = true;
                        return;
                    }
                }
                PreferencesUtils.getInstance().putBoolean(Contans.TANSLATEFORKRJ,true);
                switchview.setOpened(true);
                ListenClipboardService.F_y = 1;

                EventBus.getDefault().post(new TransTypeEvent(1));
            }

            @Override
            public void toggleToOff(SwitchView view) {
                Log.e("booln____1",2+"");
                PreferencesUtils.getInstance().putBoolean(Contans.TANSLATEFORKRJ,false);
                switchview.setOpened(false);
                ListenClipboardService.F_y = 2;
                EventBus.getDefault().post(new TransTypeEvent(2));

            }
        });
        if(PreferencesUtils.getInstance().getBoolean(Contans.TANSLATEFORKRJ,false)){
            switchview.setOpened(true);
        }
    }

    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context context){
        Intent mIntent = new Intent(context,StrideoverActivity.class);
        context.startActivity(mIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!PreferencesUtils.getInstance().getBoolean(Contans.TANSLATEFORKRJ,false)&&settype)
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(StrideoverActivity.this)) {
               ToastUtils.makeText("设置失败");
            }else{
                PreferencesUtils.getInstance().putBoolean(Contans.TANSLATEFORKRJ,true);
                switchview.setOpened(true);
                ListenClipboardService.F_y = 1;

                EventBus.getDefault().post(new TransTypeEvent(1));
            }
        }
    }
}
