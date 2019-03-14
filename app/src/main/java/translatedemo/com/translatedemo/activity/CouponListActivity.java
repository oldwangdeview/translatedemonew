package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.MyCuponAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.GetCouponListBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.LoadingPagerHead;
import translatedemo.com.translatedemo.view.YRecycleview;

public class CouponListActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.yrecycleview_)
    YRecycleview yrecycleview_;
    @BindView(R.id.relativelayout)
    RelativeLayout relativelayout;
    private LoadingPagerHead mLoadingPagerHead;
    private Dialog mLoadingDialog;
    public static final int mType = 0;
    private List<GetCouponListBean> listdata = new ArrayList<>();
    private MyCuponAdpater madpater;
    private String amount = "";
    @Override
    protected void initView() {
        mLoadingPagerHead = new LoadingPagerHead(this) {
            @Override
            protected void close() {

            }

            @Override
            protected View createSuccessView() {
                return UIUtils.inflate(CouponListActivity.this, R.layout.fragment_coupon);
            }

            @Override
            protected void reLoading() {
                retry();

            }
        };
       setContentView(R.layout.fragment_coupon);
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(getResources().getString(R.string.order_text_xzyhq));
        findViewById(R.id.layout_include).setVisibility(View.VISIBLE);
        madpater = new MyCuponAdpater(this,listdata,mType);
        yrecycleview_.setLayoutManager(new LinearLayoutManager(this));
        yrecycleview_.setItemAnimator(new DefaultItemAnimator());
        yrecycleview_.setAdapter(madpater);
        amount = getIntent().getStringExtra(Contans.INTENT_DATA);
        madpater.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                EventBus.getDefault().post(listdata.get(position));
                finish();
            }
        });
        yrecycleview_.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                yrecycleview_.setReFreshComplete();
                retry();
            }

            @Override
            public void onLoadMore() {
                yrecycleview_.setloadMoreComplete();
                retry();
            }
        });
        retry();
    }
    public void  retry(){

        Observable observable =
                ApiUtils.getApi().findPayCouponList(BaseActivity.getuser().id+"",amount,BaseActivity.getLanguetype(CouponListActivity.this)+"")
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(CouponListActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<List<GetCouponListBean>>(CouponListActivity.this) {
            @Override
            protected void _onNext(StatusCode<List<GetCouponListBean>> stringStatusCode) {
                new LogUntil(CouponListActivity.this,TAG+"getCouponList",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);

                if(stringStatusCode.getCode()==0&&stringStatusCode.getData()!=null&&stringStatusCode.getData().size()>0) {
                    mLoadingPagerHead.showPagerView(Contans.STATE_SUCCEED);
                    listdata.clear();
                    listdata.addAll(stringStatusCode.getData());
                    madpater.notifyDataSetChanged();
                }else{
                   relativelayout.setVisibility(View.GONE);
                   findViewById(R.id.layoutnodata).setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                relativelayout.setVisibility(View.GONE);
                findViewById(R.id.layoutnodata).setVisibility(View.VISIBLE);
            }
        }, "", lifecycleSubject, false, true);
    }


    public static void statrtactivity(Context mContext,String amount){
        Intent mIntent = new Intent(mContext,CouponListActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,amount);
        mContext.startActivity(mIntent);

    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
