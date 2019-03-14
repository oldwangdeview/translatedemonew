package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.application.BaseApplication;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.StatusCode;
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

import static translatedemo.com.translatedemo.application.BaseApplication.mContext;

/**
 * Created by Administrator on 2018/12/27 0027.
 */

public class LoginActivity  extends BaseActivity {
    @BindView(R.id.login_inputeditext_phone)
    EditText inoutphone;

    @BindView(R.id.login_inoputeditext_code)
    EditText inputcode;

    @BindView(R.id.login_text_getcode)
    TextView bindphone_text_getcode;
    public int MAX_TIME = 60;
    public boolean Thread_start = true;
    public boolean GET_CODE = true ;

    private Dialog mLoadingDialog;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initData() {
        super.initData();
        if(BaseActivity.getuser()!=null){
            MainActivity.startactivity(LoginActivity.this,1);
            finish();
        }
    }


    @OnClick({R.id.login_text_getcode,R.id.login_button_reture})
    public void mOnclick(View v){
        String phone = inoutphone.getText().toString().trim();
        switch (v.getId()){
            case R.id.login_text_getcode:
                if(TextUtils.isEmpty(phone)){
                    ToastUtils.makeText(getResources().getString(R.string.login_error_notphonenumber));
                    return;
                }

                if(GET_CODE) {
                    getcode(phone);
                }


                break;
            case R.id.login_button_reture:

                String code = inputcode.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    ToastUtils.makeText(getResources().getString(R.string.login_error_notphonenumber));
                    return;
                }

                if(TextUtils.isEmpty(code)){
                    ToastUtils.makeText(getResources().getString(R.string.login_error_notcode));
                    return;
                }
                login(phone,code);


                break;
        }
    }

    private void getcode(String phone){
        Observable observable =
                ApiUtils.getApi().getCode(phone)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(LoginActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(LoginActivity.this) {
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
    protected void onResume() {
        super.onResume();
        updateactionbar();
    }

    /**
     * 查看用户协议
     */
    @OnClick(R.id.login_text_useragremeet)
    public void gotouseragreement (){
        UserAgreementActivity.statrtactivity(this);
    }

    /**
     * QQ登陆
     */

    @OnClick(R.id.login_image_qq)
    public void logininQQ(){
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
//        BindPhoneActivity.statrtactivity(this);
//        finish();
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
         //   Toast.makeText(mContext, "成功了", Toast.LENGTH_LONG).show();
            String uid = data.get("uid");
            String name = data.get("name");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");
            LoginBean loginuser = new LoginBean();
            loginuser.nickName = name;
            loginuser.headBigImage = iconurl;
            loginuser.headSmallImage = iconurl;
            loginuser.sex = TextUtils.isEmpty(gender)?"0":gender.equals("男")?"1":"2";

            if(platform==SHARE_MEDIA.QQ){
                loginuser.qqOpenid = uid;
            }
            if(platform==SHARE_MEDIA.WEIXIN){
                loginuser.weixinOpenid = uid;
            }
            haveuser(platform,uid,loginuser);
//            BindPhoneActivity.statrtactivity(LoginActivity.this,loginuser);

        }
        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }
        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
        }
    };
    /**
     * 微信登陆
     */
    @OnClick(R.id.login_image_wechart)
    public void loginwechart(){
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);

    }


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,LoginActivity.class);
        mContext.startActivity(mIntent);

    }

    public void login(String phone,String code){


        Observable observable =
                ApiUtils.getApi().loginforphone(phone,code,BaseActivity.getLanguetype(this), DeviceUuidFactory.getInstance(LoginActivity.this).getDeviceUuid())
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(LoginActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LoginBean>(LoginActivity.this) {
            @Override
            protected void _onNext(StatusCode<LoginBean> stringStatusCode) {
                new LogUntil(LoginActivity.this,TAG+"loginreturn",new Gson().toJson(stringStatusCode));

                PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,new Gson().toJson(stringStatusCode.getData()));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                MainActivity.startactivity(LoginActivity.this,0);
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
//
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Thread_start = false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private void haveuser(SHARE_MEDIA platform,String uid,final LoginBean user){
        Observable observable = null;
        if(platform==SHARE_MEDIA.WEIXIN){
            observable =
                    ApiUtils.getApi().thridLogin(uid, BaseActivity.getLanguetype(this)+"", DeviceUuidFactory.getInstance(LoginActivity.this).getDeviceUuid())
                            .compose(RxHelper.getObservaleTransformer())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(LoginActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());

        }else {
            observable =
                    ApiUtils.getApi().thridLogin1(uid, BaseActivity.getLanguetype(this)+"", DeviceUuidFactory.getInstance(LoginActivity.this).getDeviceUuid())
                            .compose(RxHelper.getObservaleTransformer())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(LoginActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());
        }
        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LoginBean>(LoginActivity.this) {
            @Override
            protected void _onNext(StatusCode<LoginBean> stringStatusCode) {
                new LogUntil(LoginActivity.this,TAG+"loginreturn",new Gson().toJson(stringStatusCode));

                if(stringStatusCode!=null&&stringStatusCode.getData()!=null) {
                    PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER, new Gson().toJson(stringStatusCode.getData()));
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                    MainActivity.startactivity(LoginActivity.this, 0);
                    finish();
                }else{
                    BindPhoneActivity.statrtactivity(LoginActivity.this,user);
                }
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                BindPhoneActivity.statrtactivity(LoginActivity.this,user);
            }
        }, "", lifecycleSubject, false, true);
    }
}
