package translatedemo.com.translatedemo.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import translatedemo.com.translatedemo.eventbus.WeChartPayEvent;
import translatedemo.com.translatedemo.util.LogUtils;
import translatedemo.com.translatedemo.util.ToastUtils;

/**
 * Time  2018/3/2 15:11
 * Dest  微信支付
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private String Tag="WXPayEntryActivity";
    private IWXAPI mIWXAPI;
    private String mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mData = intent.getStringExtra("_wxapi_payresp_extdata");
        mIWXAPI = WXAPIFactory.createWXAPI(this, WXPayEntry.APP_ID,true);
        mIWXAPI.handleIntent(intent,this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.i(Tag,baseReq.toString());

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIWXAPI.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp resp) {
        ToastUtils.makeText("支付返回码"+resp.errCode);
        EventBus.getDefault().post(new WeChartPayEvent(resp.errCode));
//        if (resp.errCode == 0) {
////                       ToastUtils.makeText("支付成功");
//                       finish();
//                   } else if (resp.errCode == -2) {
//                       //用户取消
////                       ToastUtils.makeText("取消支付");
//                       finish();
//                   } else if (resp.errCode == -1) {
//                       //支付失败
////                       ToastUtils.makeText("支付失败");
//                       finish();
//        }

    }
}
