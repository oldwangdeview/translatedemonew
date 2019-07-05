package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.FlashBannerBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.UIUtils;

public class BannerActivity extends BaseActivity {
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.title_text)
    TextView title_text;

    public static final int MAXTINE  = 3;

    private boolean startthred = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.showFullScreen(BannerActivity.this,true);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_falsh);
        getbanner();
    }
    private String url = "";
    private void getbanner(){
        Observable observable =
                ApiUtils.getApi().getflashBanner()
                        .compose(RxHelper.getObservaleTransformer())
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<FlashBannerBean>(BannerActivity.this) {
            @Override
            protected void _onNext(StatusCode<FlashBannerBean> stringStatusCode) {
                if(stringStatusCode!=null&&stringStatusCode.getData()!=null){
                    url = stringStatusCode.getData().url;
                    if(!TextUtils.isEmpty(stringStatusCode.getData().image)){
//                        UIUtils.loadImageView(mcontent,stringStatusCode.getData().image,banner);
                        Glide.with(mcontent).load(stringStatusCode.getData().image).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(banner);

                    }
                    timmer();
                }else{
                    ChoiceLanguageActivity.startactivity(BannerActivity.this);
                    finish();
                }

            }

            @Override
            protected void _onError(String message) {
                ChoiceLanguageActivity.startactivity(BannerActivity.this);
                finish();
            }
        }, "", lifecycleSubject, false, true);

    }

    @OnClick(R.id.title_text)
    public void startactivtym(){
        startthred = false;
        mhandel = null;
        if(gotiomainactivity) {
            ChoiceLanguageActivity.startactivity(BannerActivity.this);
            finish();
        }
    }

    private boolean gotiomainactivity = true;

    Handler mhandel = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==0){
                ChoiceLanguageActivity.startactivity(BannerActivity.this);
                finish();
            }else{
                if(title_text.getVisibility()==View.GONE){
                    title_text.setVisibility(View.VISIBLE);
                }
                title_text.setText(msg.arg1+getResources().getString(R.string.falsh_text_title));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startthred = false;
    }
    private void timmer(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    if (startthred) {
                        for (int i = MAXTINE; i >= 0; i--) {

                            Thread.sleep(1000);
                            Message msg = new Message();
                            msg.arg1 = i;
                            if(mhandel!=null) {
                                mhandel.sendMessage(msg);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public static void startactivity(Context mContext){
        Intent mintent = new Intent(mContext,BannerActivity.class);
        mContext.startActivity(mintent);
    }
    @OnClick(R.id.banner)
    public void gotoweb(){
        if(!TextUtils.isEmpty(url)) {
            UIUtils.openWebUrl(this, url);
        }
    }
}
