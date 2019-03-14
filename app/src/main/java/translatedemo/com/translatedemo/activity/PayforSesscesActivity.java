package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.base.BasePersonActivity;
import translatedemo.com.translatedemo.bean.DucationBean;
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

public class PayforSesscesActivity extends BaseActivity {

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.tiyan)
    Button tiyan;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;

    public double timedata = -1;
    @Override
    protected void initView() {
       setContentView(R.layout.activity_payforsessces);
    }




    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("支付成功");
        iv_back_activity_text.setVisibility(View.VISIBLE);
        timedata = getIntent().getDoubleExtra(Contans.INTENT_DATA,-1);
        if(timedata ==-1){
            finish();
        }
        getuserdata();
    }



    public static void startactivity(Context mContext,double time){
        Intent mINtent = new Intent(mContext,PayforSesscesActivity.class);
        mINtent.putExtra(Contans.INTENT_DATA,time);
        mContext.startActivity(mINtent);


    }

    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo,R.id.tiyan})
    public void finishactivity(){
        finish();
    }

    private Dialog mLoadingDialog;
    private void getuserdata(){
        Observable observable =
                ApiUtils.getApi().getuserinfo(BaseActivity.getuser().id+"",BaseActivity.getLanguetype(mcontent))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(PayforSesscesActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<List<DucationBean>>(PayforSesscesActivity.this) {
            @Override
            protected void _onNext(StatusCode<List<DucationBean>> stringStatusCode) {
                new LogUntil(PayforSesscesActivity.this,TAG+"getDucation",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,new Gson().toJson(stringStatusCode.getData()));
                BaseActivity.user = null;
                try {
                    time.setText(BaseActivity.getuser().memberBeginTime + " 至 " + BaseActivity.getuser().memberEndTime);
                }catch (Exception e){
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new UpdateUserEvent());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }

}
