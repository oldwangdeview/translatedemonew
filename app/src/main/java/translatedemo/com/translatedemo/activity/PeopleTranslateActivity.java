package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;
import me.wcy.htmltext.OnTagClickListener;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.ConfigBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.ToastUtils;

/**
 * Created by oldwang on 2019/1/24 0024.
 */

public class PeopleTranslateActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.qq_data)
    TextView qq_data;
    @BindView(R.id.weixin_data)
    TextView weixindata;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_peopletranslate);

    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.pople_translate_text_titlenam));
        getconfig(1);
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
            String url = "mqqwpa://im/chat?chat_type=wpa&uin="+mqqdata;
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

    private Dialog mLoadingDialog;
    String mqqdata = "2675223279";
    public void getconfig(int type){


        Observable observable =
                ApiUtils.getApi().getconfig(type,BaseActivity.getLanguetype(this))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(PeopleTranslateActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<ConfigBean>(PeopleTranslateActivity.this) {
            @Override
            protected void _onNext(StatusCode<ConfigBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                new LogUntil(PeopleTranslateActivity.this,"content",stringStatusCode.getData().content);

                if(stringStatusCode.getData()!=null){

                    if(!TextUtils.isEmpty(stringStatusCode.getData().qq)){
                        mqqdata = stringStatusCode.getData().qq;
                        qq_data.setText("QQ "+stringStatusCode.getData().qq);
                    }

                    if(!TextUtils.isEmpty(stringStatusCode.getData().weixin)){

                        weixindata.setText("微信 "+stringStatusCode.getData().weixin);
                    }
                }



            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
//
    }

}
