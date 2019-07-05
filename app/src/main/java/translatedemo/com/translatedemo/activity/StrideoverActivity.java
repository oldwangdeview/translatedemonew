package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.net.URLEncoder;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.ChoiceLangvageAdpater;
import translatedemo.com.translatedemo.application.BaseApplication;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.TransTypeEvent;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.recever.ListenClipboardService;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.ChoiceLangageDialog;

public class StrideoverActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.switchview)
    SwitchView switchview;
    @BindView(R.id.choice_imagelayout)
    LinearLayout choice_imagelayout;

    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.text2)
    TextView text2;

    @BindArray(R.array.translate_choiceimage1)
    String[] choicedata1;




    private ChoiceLangageDialog choicelangage1,choicelangage2;
    private ChoiceLangvageAdpater chiceadpater1,chiceadpater2;

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
        chiceadpater1 = new ChoiceLangvageAdpater(this,choicedata1,UIUtils.image1);
        chiceadpater2 = new  ChoiceLangvageAdpater(this,choicedata1,UIUtils.image1);
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
                choice_imagelayout.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new TransTypeEvent(1));
            }

            @Override
            public void toggleToOff(SwitchView view) {
                Log.e("booln____1",2+"");
                PreferencesUtils.getInstance().putBoolean(Contans.TANSLATEFORKRJ,false);
                switchview.setOpened(false);
                ListenClipboardService.F_y = 2;
                choice_imagelayout.setVisibility(View.GONE);
                EventBus.getDefault().post(new TransTypeEvent(2));

            }
        });




        chiceadpater1.steonitemclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                text1.setText(choicedata1[position]);
                image1.setImageResource(UIUtils.image1[position]);
                PreferencesUtils.getInstance().putInt(Contans.INPUT_STRING,position);
                if(choicelangage1!=null){
                    choicelangage1.dismiss();
                }
                image1.setImageResource(UIUtils.image1[position]);
                text1.setText(choicedata1[position ]);


            }
        });
        chiceadpater2.steonitemclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                text2.setText(choicedata1[position]);
                image2.setImageResource(UIUtils.image1[position]);
                if(choicelangage2!=null){
                    choicelangage2.dismiss();
                }
                PreferencesUtils.getInstance().putInt(Contans.OUTPUT_STRING,position);
                image2.setImageResource(UIUtils.image1[position]);
                text2.setText(choicedata1[position]);
            }
        });

        if(PreferencesUtils.getInstance().getBoolean(Contans.TANSLATEFORKRJ,false)){
            switchview.setOpened(true);
            choice_imagelayout.setVisibility(View.VISIBLE);
        }else{
            choice_imagelayout.setVisibility(View.GONE);
        }
        updateview();
    }

    private void updateview(){
        int input_index = PreferencesUtils.getInstance().getInt(Contans.INPUT_STRING,2);
        image1.setImageResource(UIUtils.image1[input_index]);
        text1.setText(choicedata1[input_index ]);

        int out_index = PreferencesUtils.getInstance().getInt(Contans.OUTPUT_STRING,1);
        image2.setImageResource(UIUtils.image1[out_index]);
        text2.setText(choicedata1[out_index]);
    }

    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context context){
        Intent mIntent = new Intent(context,StrideoverActivity.class);
        context.startActivity(mIntent);
    }

    @OnClick(R.id.choice_layout1)
    public  void choicelangvage(){

        if (choicelangage1==null&&chiceadpater1!=null) {

            choicelangage1 = new ChoiceLangageDialog.Builder(StrideoverActivity.this).setCanCancel(chiceadpater1).create();
        }
        if (!choicelangage1.isShowing()){

            choicelangage1.show();
        }
    }

    @OnClick(R.id.choice_layout2)
    public  void choicelangvage2(){
        if (choicelangage2==null&&chiceadpater2!=null) {

            choicelangage2 = new ChoiceLangageDialog.Builder(StrideoverActivity.this).setCanCancel(chiceadpater2).create();
        }
        if (!choicelangage2.isShowing()){

            choicelangage2.show();
        }

    }



    @OnClick(R.id.qiehuan_langvage)
    public void qiehuanlangvage(){
        int inoutposition = PreferencesUtils.getInstance().getInt(Contans.INPUT_STRING,2);

        int outputposition = PreferencesUtils.getInstance().getInt(Contans.OUTPUT_STRING,1);

        if(inoutposition==4||inoutposition==15){
            ToastUtils.makeText("目标语言不支持");
            return;
        }
        text2.setText(choicedata1[inoutposition]);
        image2.setImageResource(UIUtils.image1[inoutposition]);
        text1.setText(choicedata1[outputposition]);
        image1.setImageResource(UIUtils.image1[outputposition]);

        PreferencesUtils.getInstance().putInt(Contans.INPUT_STRING,getposition(choicedata1[outputposition],choicedata1,outputposition));
        PreferencesUtils.getInstance().putInt(Contans.OUTPUT_STRING,getposition(choicedata1[inoutposition],choicedata1,inoutposition));


    }

    private int getposition(String data,String[] list,int position){
        for(int i =0;i<list.length;i++){

            if(data.equals(list[i])){
                return i;
            }

        }
        return position;
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
                choice_imagelayout.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new TransTypeEvent(1));
            }
        }
    }
}
