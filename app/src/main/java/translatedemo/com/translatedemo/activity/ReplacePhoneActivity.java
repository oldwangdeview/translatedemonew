package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.NoticeBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.UpdateUserEvent;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/16 0016.
 */

public class ReplacePhoneActivity  extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.oldphone)
    EditText oldphone;
    @BindView(R.id.newphone)
    EditText newphone;
    @BindView(R.id.code)
    EditText code;

    @BindView(R.id.login_text_getcode)
    TextView bindphone_text_getcode;
    public int MAX_TIME = 60;
    public boolean Thread_start = true;
    public boolean GET_CODE = true ;

    private Dialog mLoadingDialog;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_replacephone);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @OnClick(R.id.login_text_getcode)
    public void getcode(){
        String oldphone_t =oldphone.getText().toString().trim();
        String newpgone_t = newphone.getText().toString().trim();
        if(TextUtils.isEmpty(oldphone_t)){
            ToastUtils.makeText(getResources().getString(R.string.login_error_notphonenumber));
            return;
        }
        if(!UIUtils.isPhoneNumber(oldphone_t)){
            ToastUtils.makeText(this.getResources().getString(R.string.login_error_isnotphone));
            return;
        }

        if(TextUtils.isEmpty(newpgone_t)){
            ToastUtils.makeText(this.getResources().getString(R.string.replacephone_text_newphone));
            return;
        }
        if(!UIUtils.isPhoneNumber(newpgone_t)){
            ToastUtils.makeText(this.getResources().getString(R.string.login_error_isnotphone));
            return;
        }

        if(GET_CODE) {
            getcode(oldphone_t,newpgone_t);
        }

    }
    @OnClick(R.id.uplode)
    public void uploaddata(){
        String oldphone_t =oldphone.getText().toString().trim();
        String newpgone_t = newphone.getText().toString().trim();
        String code_t = code.getText().toString().trim();
        if(TextUtils.isEmpty(oldphone_t)){
            ToastUtils.makeText(this.getResources().getString(R.string.replacephone_text_oldphone));
            return;
        }
        if(!UIUtils.isPhoneNumber(oldphone_t)){
            ToastUtils.makeText(this.getResources().getString(R.string.login_error_isnotphone));
            return;
        }
        if(TextUtils.isEmpty(newpgone_t)){
            ToastUtils.makeText(this.getResources().getString(R.string.replacephone_text_newphone));
            return;
        }
        if(!UIUtils.isPhoneNumber(newpgone_t)){
            ToastUtils.makeText(this.getResources().getString(R.string.login_error_isnotphone));
            return;
        }
        if(TextUtils.isEmpty(code_t)){
            ToastUtils.makeText(this.getResources().getString(R.string.replacephone_text_code));
            return;
        }

        Observable observable =
                ApiUtils.getApi().changePhone(BaseActivity.getuser().id+"", oldphone_t,newpgone_t,code_t,BaseActivity.getLanguetype(ReplacePhoneActivity.this))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(ReplacePhoneActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LoginBean>(ReplacePhoneActivity.this) {
            @Override
            protected void _onNext(StatusCode<LoginBean> stringStatusCode) {
                new LogUntil(ReplacePhoneActivity.this,TAG+"changePhone",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode!=null&&stringStatusCode.getData()!=null){
                    PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,new Gson().toJson(stringStatusCode.getData()));
                    BaseActivity.user=null;
                    EventBus.getDefault().post(new UpdateUserEvent());
                    ToastUtils.makeText(ReplacePhoneActivity.this.getResources().getString(R.string.update_seccese));
                    finish();
                }
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);






    }




    private void getcode(String phone,String newpgone_t){
        Observable observable =
                ApiUtils.getApi().getCode(phone,1,newpgone_t)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(ReplacePhoneActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(ReplacePhoneActivity.this) {
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
    protected void onDestroy() {
        super.onDestroy();
        Thread_start =false;
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.replacephone_text_titlename));
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,ReplacePhoneActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
}