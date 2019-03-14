package translatedemo.com.translatedemo.recever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import org.greenrobot.eventbus.EventBus;

import translatedemo.com.translatedemo.util.NetUtil;

public class NetBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            EventBus.getDefault().post(NetUtil.getNetWorkState(context));
        }
        }
}
