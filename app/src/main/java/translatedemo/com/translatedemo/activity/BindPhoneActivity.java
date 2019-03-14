package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.DeviceUuidFactory;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/4 0004.
 */

public class BindPhoneActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.bindphone_inputeditext_phone)
    EditText bindphone_inputeditext_phone;//手机号
    @BindView(R.id.bindphone_inoputeditext_code)
    EditText bindphone_inoputeditext_code;//验证码
    @BindView(R.id.bindphone_text_getcode)
    TextView bindphone_text_getcode;
    private Dialog mLoadingDialog;
    private LoginBean intentdata = null;
    public int MAX_TIME = 60;
    public boolean Thread_start = true;
    public boolean GET_CODE = true ;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_bindphone);
    }

    @Override
    protected void initData() {
        super.initData();
        title_name.setText(getResources().getString(R.string.bindphone_text_titlename));
        iv_back_activity_text.setVisibility(View.VISIBLE);
        intentdata = (LoginBean) getIntent().getSerializableExtra(Contans.INTENT_DATA);


    }


    /**
     * 获取验证码
     */
    @OnClick(R.id.bindphone_text_getcode)
    public void getcode(){
        String phone = bindphone_inputeditext_phone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.makeText(getResources().getString(R.string.login_error_notphonenumber));
            return;
        }

        if(!UIUtils.isPhoneNumber(phone)){
            ToastUtils.makeText(getResources().getString(R.string.login_error_isnotphone));
            return;
        }

        if(GET_CODE) {
            getcode(phone);
        }
    }


    /**
     * 绑定手机号
     */
    @OnClick(R.id.bindphone_btn_confirm)
    public void bandphone(){
        String phone = bindphone_inputeditext_phone.getText().toString().trim();
        String code = bindphone_inoputeditext_code.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.makeText(getResources().getString(R.string.login_error_notphonenumber));
            return;
        }
        if(!UIUtils.isPhoneNumber(phone)){
            ToastUtils.makeText(getResources().getString(R.string.login_error_isnotphone));
            return;
        }
        if(TextUtils.isEmpty(code)){
            ToastUtils.makeText(getResources().getString(R.string.login_error_notcode));
            return;
        }
        BandPhone(code,phone);
//        MainActivity.startactivity(this,0);




    }

    public static void statrtactivity(Context mContext, LoginBean userdata){
        Intent mintent = new Intent(mContext,BindPhoneActivity.class);
        mintent.putExtra(Contans.INTENT_DATA,(Serializable)userdata);
        mContext.startActivity(mintent);
    }


    private void getcode(String phone){
        Observable observable =
                ApiUtils.getApi().getCode(phone)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(BindPhoneActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(BindPhoneActivity.this) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                new LogUntil(mcontent,TAG,new Gson().toJson(stringStatusCode));
                GET_CODE = false;
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                timeer();
            }
            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);


    }

    private void timeer(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                if(Thread_start&&!GET_CODE){

                    try {
                        for (; MAX_TIME > 0; MAX_TIME--) {

                            Thread.sleep(1000);
                            Message msg = new Message();
                            msg.arg1 = MAX_TIME;
                            mhandler.sendMessage(msg);


                        }
                    }catch (Exception e){

                    }

                }

            }
        }.start();


    }


    Handler mhandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.arg1 > 1) {



                    bindphone_text_getcode.setText(msg.arg1+getResources().getString(R.string.getcode_code_startcode));
                    bindphone_text_getcode.setTextColor(getResources().getColor(R.color.c_666666));

                } else {
                    GET_CODE = true;
                    MAX_TIME = 60;
                    bindphone_text_getcode.setText(getResources().getString(R.string.login_but_getcode));
                    bindphone_text_getcode.setTextColor(getResources().getColor(R.color.c_e94950));

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    private void BandPhone(String code, String phone){

        Observable observable =
                ApiUtils.getApi().threereguest(code,
                        intentdata.nickName,
                        intentdata.weixinOpenid,
                        intentdata.qqOpenid,
                        intentdata.sex,
                        intentdata.headBigImage,
                        BaseActivity.getLanguetype(BindPhoneActivity.this)+"",
                        phone,
                        DeviceUuidFactory.getInstance(BindPhoneActivity.this).getDeviceUuid())
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(BindPhoneActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LoginBean>(BindPhoneActivity.this) {
            @Override
            protected void _onNext(StatusCode<LoginBean> stringStatusCode) {
                new LogUntil(mcontent,TAG,new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,new Gson().toJson(stringStatusCode.getData()));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                MainActivity.startactivity(BindPhoneActivity.this,0);
                finish();

            }
            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);



    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Thread_start = false;
    }
}
