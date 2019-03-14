package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

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
import translatedemo.com.translatedemo.eventbus.UpdateUserEvent;
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
 * Created by oldwang on 2019/1/11 0011.
 * 安全中心
 */

public class SafetyCenterActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.phonenum)
    TextView phonenum;
    @BindView(R.id.bangdingqq)
    TextView bangdingqq;
    @BindView(R.id.bangdingwechart)
    TextView bangdingwechat;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_safecenter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(SafetyCenterActivity.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.safecenter_text_titlename));
        getuserdata();
    }
    private void getuserdata(){
        LoginBean user = BaseActivity.getuser();
        if(!TextUtils.isEmpty(user.phone)){
            phonenum.setText(UIUtils.getphone(user.phone));
        }else{
            phonenum.setText(mcontent.getResources().getString(R.string.userinfo_text_nobading));
        }
        //userinfo_text_bading
        if(!TextUtils.isEmpty(user.qqOpenid)){
            bangdingqq.setText(mcontent.getResources().getString(R.string.userinfo_text_bading));
            bangdingqq.setTextColor(mcontent.getResources().getColor(R.color.c_6495ED));
//  c_6495ED
        }else{
            bangdingqq.setText(mcontent.getResources().getString(R.string.userinfo_text_nobading));
            bangdingqq.setTextColor(mcontent.getResources().getColor(R.color.c_999999));
        }
        if(!TextUtils.isEmpty(user.weixinOpenid)){
            bangdingwechat.setText(mcontent.getResources().getString(R.string.userinfo_text_bading));
            bangdingwechat.setTextColor(mcontent.getResources().getColor(R.color.c_6495ED));
        }else{
            bangdingwechat.setText(mcontent.getResources().getString(R.string.userinfo_text_nobading));
            bangdingwechat.setTextColor(mcontent.getResources().getColor(R.color.c_999999));

        }

    }

    @OnClick(R.id.bandingqq)
    public void bangdingqqq(){
        if(TextUtils.isEmpty(user.qqOpenid)){

            UMShareAPI.get(SafetyCenterActivity.this).getPlatformInfo(SafetyCenterActivity.this, SHARE_MEDIA.QQ, authListener);

        }else{
            showdialog(SHARE_MEDIA.QQ);

        }
    }



    @OnClick(R.id.bangdingwechartlin)
    public void bangdingwechart(){
        if(TextUtils.isEmpty(user.weixinOpenid)){
            UMShareAPI.get(SafetyCenterActivity.this).getPlatformInfo(SafetyCenterActivity.this, SHARE_MEDIA.WEIXIN, authListener);
        }else{
            showdialog(SHARE_MEDIA.WEIXIN);

        }
    }

    private void showdialog(final SHARE_MEDIA palateform){
        AlertView alertView = new AlertView("提示", palateform==SHARE_MEDIA.QQ?"确定解绑QQ?":"确定解绑微信", null, null, new String[]{"取消", "确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position==1){
                    bandinguser("",palateform,false);
                }else{

                }

            }
        });
        alertView.show();
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
            new LogUntil(mcontent,TAG,"uid"+uid);
            bandinguser(uid,platform,true);

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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uodateuser( UpdateUserEvent uodate){
        getuserdata();
    }


    @OnClick(R.id.replace_phone_layout)
    public void gotoreplacephone(){
        ReplacePhoneActivity.startactivity(this);
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,SafetyCenterActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private Dialog mLoadingDialog;

    private void bandinguser(String uid,SHARE_MEDIA platform,final boolean type){

        Observable observable = null;

        if(platform==SHARE_MEDIA.WEIXIN) {
            observable = ApiUtils.getApi().bindwechart(
                   BaseActivity.getLanguetype(this),
                    BaseActivity.getuser().id+"",
                    type?uid:"",!TextUtils.isEmpty(uid)?1:0)
                    .compose(RxHelper.getObservaleTransformer())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            if (mLoadingDialog == null) {
                                mLoadingDialog = LoadingDialogUtils.createLoadingDialog(SafetyCenterActivity.this, "");
                            }
                            LoadingDialogUtils.show(mLoadingDialog);
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread());
        }else if(platform == SHARE_MEDIA.QQ){
            observable = ApiUtils.getApi().bindUqq(
                    BaseActivity.getLanguetype(this),
                    BaseActivity.getuser().id+"",
                    type?uid:"",!TextUtils.isEmpty(uid)?1:0)
                    .compose(RxHelper.getObservaleTransformer())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            if (mLoadingDialog == null) {
                                mLoadingDialog = LoadingDialogUtils.createLoadingDialog(SafetyCenterActivity.this, "");
                            }
                            LoadingDialogUtils.show(mLoadingDialog);
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread());
        }

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LoginBean>(SafetyCenterActivity.this) {
            @Override
            protected void _onNext(StatusCode<LoginBean> stringStatusCode) {
                new LogUntil(SafetyCenterActivity.this,TAG+"loginreturn",new Gson().toJson(stringStatusCode));
                PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,new Gson().toJson(stringStatusCode.getData()));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                BaseActivity.user = null;
                getuserdata();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
