package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.GetCouponListBean;
import translatedemo.com.translatedemo.bean.MemberListBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.bean.WeChartPayben;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.WeChartPayEvent;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.WXPayUtils;

public class OrderDetailActivity extends BaseActivity {


    private MemberListBean mdata;
    @BindView(R.id.order_number)
    TextView order_number;//订单编号
    @BindView(R.id.order_type)
    TextView order_type;//类型
    @BindView(R.id.order_money)
    TextView order_money;//价格


    @BindView(R.id.order_xzyhq)
    TextView order_xzyhq;//优惠价格
    @BindView(R.id.order_ddmoney)
    TextView order_ddmoney;//订单金额


    @BindView(R.id.choice_wechart)
    ImageView choice_wechart;
    @BindView(R.id.choice_image_ali)
    ImageView choice_ali;

    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;

    //支付类型，默认1。微信支付
    private int PAY_FOR_TYPE = 1;


    private String yhqid = "";
    @Override
    protected void initView() {
        setContentView(R.layout.activity_orderdetail);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(getResources().getString(R.string.order_text_titlename));
        mdata = (MemberListBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if(mdata!=null){

            if(!TextUtils.isEmpty(mdata.month+"")){
                order_type.setText(mdata.month+"月");
            }


            if(!TextUtils.isEmpty(mdata.amount+"")&&mdata.amount>0){

                order_money.setText("¥"+mdata.amount+"");
                order_ddmoney.setText("¥"+mdata.amount);
            }

            if(!TextUtils.isEmpty(mdata.id+"")){
                order_number.setText(mdata.id+"");
            }

        }

    }


    /**
     * 选择优惠券
     */
    @OnClick(R.id.choice_yhq)
    public void choice_yhq(){
      CouponListActivity.statrtactivity(OrderDetailActivity.this,mdata.amount+"");
    }

    /**
     * 选择微信支付
     */
    @OnClick(R.id.payfor_wechart)
    public void choice_wechartpay(){
        choice_wechart.setImageResource(R.mipmap.xuanzhong);
        choice_ali.setImageResource(R.drawable.choice_order_payfortype);
        PAY_FOR_TYPE = 1;
    }

    /**
     * 选择微信支付
     */

    @OnClick({R.id.payfor_ali})
    public void choice_alipay(){
        choice_ali.setImageResource(R.mipmap.xuanzhong);
        choice_wechart.setImageResource(R.drawable.choice_order_payfortype);
        PAY_FOR_TYPE = 2;
    }

    @OnClick(R.id.payfor_now)
    public void payfornow(){
        payfor(yhqid);
    }

    public static void startactivity(Context mcontext,MemberListBean data){
        Intent intent = new Intent(mcontext,OrderDetailActivity.class);
        intent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mcontext.startActivity(intent);
    }

    private Dialog mLoadingDialog;
    public void payfor(String id){
        Observable observable =null;
        if(TextUtils.isEmpty(id)) {
            observable =
                    ApiUtils.getApi().createOrder(BaseActivity.getuser().id + "", mdata.id + "", PAY_FOR_TYPE==1?2+"":1+ "")
                            .compose(RxHelper.getObservaleTransformer())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    try {


                                        if (mLoadingDialog == null) {
                                            mLoadingDialog = LoadingDialogUtils.createLoadingDialog(OrderDetailActivity.this, "");
                                        }
                                        LoadingDialogUtils.show(mLoadingDialog);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());
        }else{
            observable =
                    ApiUtils.getApi().createOrder(BaseActivity.getuser().id + "", mdata.id + "", id,PAY_FOR_TYPE==1?2+"":1+ "")
                            .compose(RxHelper.getObservaleTransformer())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    try {


                                        if (mLoadingDialog == null) {
                                            mLoadingDialog = LoadingDialogUtils.createLoadingDialog(OrderDetailActivity.this, "");
                                        }
                                        LoadingDialogUtils.show(mLoadingDialog);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());

        }

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<String>(OrderDetailActivity.this) {
            @Override
            protected void _onNext(StatusCode<String> stringStatusCode) {

                new LogUntil(mcontent,TAG,new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);

                if(PAY_FOR_TYPE==1){

                    String jsonobject = stringStatusCode.getData();
                    if(!TextUtils.isEmpty(stringStatusCode.getData())){
                        WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();

                        WeChartPayben wechartbean = new Gson().fromJson(jsonobject,WeChartPayben.class);
                        builder.setAppId(wechartbean.appid)
                                .setPartnerId(wechartbean.partnerid)
                                .setPrepayId(wechartbean.prepayid)
                                .setPackageValue("Sign=WXPay")
                                .setNonceStr(wechartbean.noncestr)
                                .setTimeStamp(wechartbean.timestamp)
                                .setSign(wechartbean.sign)
                                .build().toWXPayNotSign(OrderDetailActivity.this);
                    }
                }else{
                    payforali(stringStatusCode.getData());
                }



            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void choice_coupon(GetCouponListBean event){
        if(event!=null) {
            order_xzyhq.setText("¥"+event.reducePrice+"");
            yhqid = event.id+"";
            order_ddmoney.setText("¥"+(mdata.amount-event.reducePrice));
        }
    }
    private void payforali(final String orderinfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderDetailActivity.this);
                Map<String, String> result = alipay.payV2(orderinfo, true);

                Message msg = new Message();
                msg.what = 101;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Map<String, String> result = ((Map<String, String>) msg.obj);
            new LogUntil(mcontent, TAG, "result_" + new Gson().toJson(result));
            String requestcode = result.get("resultStatus");
            if (!TextUtils.isEmpty(requestcode)) {
                try {

                    switch (Integer.parseInt(requestcode)) {
                        case 9000:

                            ToastUtils.makeText("支付宝支付成功");
                            PayforSesscesActivity.startactivity(OrderDetailActivity.this,mdata.month);
                            break;
                        default:
                            ToastUtils.makeText("支付失败");
                            break;
                    }

                } catch (Exception e) {

                }
            }
        }

        ;
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payforreturn(WeChartPayEvent event){
        switch (event.code){
            case 0:
                ToastUtils.makeText("支付成功");
                PayforSesscesActivity.startactivity(OrderDetailActivity.this,mdata.month);
                break;
            case -2:
                ToastUtils.makeText("支付取消");
                break;
            case -1:
                ToastUtils.makeText("支付失败");
                break;
        }
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(this);
    }
}
