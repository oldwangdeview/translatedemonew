package translatedemo.com.translatedemo.recever;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import translatedemo.com.translatedemo.activity.MainActivity;
import translatedemo.com.translatedemo.activity.TransDataActivty;
import translatedemo.com.translatedemo.application.BaseApplication;
import translatedemo.com.translatedemo.clipboard.ClipboardManagerCompat;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.TransTypeEvent;
import translatedemo.com.translatedemo.eventbus.TranslateEvent;
import translatedemo.com.translatedemo.interfice.Translateinterfice;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.SystemUtil;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;


public final class ListenClipboardService extends Service implements TipViewController.ViewDismissHandler {

    private static final String KEY_FOR_WEAK_LOCK = "weak-lock";
    private static final String KEY_FOR_CMD = "cmd";
    private static final String KEY_FOR_CONTENT = "content";
    private static final String CMD_TEST = "test";
    public static int F_y = -1;
    Context mContext = this;
    private static CharSequence sLastContent = null;
    private ClipboardManagerCompat mClipboardWatcher;
    private TipViewController mTipViewController;
    private ClipboardManagerCompat.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener = new ClipboardManagerCompat.OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            performClipboardCheck();
        }
    };

    public static Translateinterfice mlister = null;
    public static Context mcontext;
    public static void start(Context context) {
        mcontext = context;
        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        context.startService(serviceIntent);
    }

    /**
     * for dev
     */
    public static void startForTest(Context context, String content) {

        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        serviceIntent.putExtra(KEY_FOR_CMD, CMD_TEST);
        serviceIntent.putExtra(KEY_FOR_CONTENT, content);
        context.startService(serviceIntent);
    }

    public static void startForWeakLock(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        context.startService(serviceIntent);

        intent.putExtra(ListenClipboardService.KEY_FOR_WEAK_LOCK, true);
        Intent myIntent = new Intent(context, ListenClipboardService.class);

        // using wake lock to start service
        WakefulBroadcastReceiver.startWakefulService(context, myIntent);
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        mClipboardWatcher = ClipboardManagerCompat.create(this);
        mClipboardWatcher.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new LogUntil(mContext,"booln","ondestory");
        EventBus.getDefault().unregister(this);
        mClipboardWatcher.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener);

        sLastContent = null;
        if (mTipViewController != null) {
            mTipViewController.setViewDismissHandler(null);
            mTipViewController = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Utils.printIntent("onStartCommand", intent);

        if (intent != null) {
            // remove wake lock
            if (intent.getBooleanExtra(KEY_FOR_WEAK_LOCK, false)) {
                BootCompletedReceiver.completeWakefulIntent(intent);
            }
            String cmd = intent.getStringExtra(KEY_FOR_CMD);
            if (!TextUtils.isEmpty(cmd)) {
                if (cmd.equals(CMD_TEST)) {
                    String content = intent.getStringExtra(KEY_FOR_CONTENT);
                    showContent(content);
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void performClipboardCheck() {
        CharSequence content = mClipboardWatcher.getText();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        showContent(content);
    }

    private void showContent(CharSequence content) {
        if (sLastContent != null && sLastContent.equals(content) || content == null) {
            return;
        }
        sLastContent = content;

        Log.e("booln",fy_(mContext)+"");
        if(fy_(mContext)==1) {
            String mdata = content.toString() .replace("'","")
                    .replace(" ","")
                    .replace("<","")
                    .replace(">","")
                    .replace("-","");;
            if(UIUtils.isEnglish(mdata)) {
//                if (Build.MANUFACTURER.equals("Xiaomi")) {
                    Intent mIntent = new Intent(mContext,TransDataActivty.class);
                    mIntent.putExtra(TransDataActivty.DATA,content.toString());
                    mIntent.putExtra(TransDataActivty.TYPE,1);
                    mContext.startActivity(mIntent);
//                } else {
//                    if (mTipViewController != null) {
//                        mTipViewController.updateContent(content);
//                    } else {
//                        mTipViewController = new TipViewController(getApplication(), content);
//                        mTipViewController.setViewDismissHandler(this);
//                        mTipViewController.show();
//                    }
//                }
            }
        }else{
            return;
        }
    }
    public static int fy_(Context mContext){
        if(F_y<0){
            F_y = PreferencesUtils.getInstance().getBoolean(Contans.TANSLATEFORKRJ,false)?1:2;
         }
        return F_y;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatetranstype(TransTypeEvent event){
F_y=event.mtype;
    }

    @Override
    public void onViewDismiss() {
        sLastContent = null;
        mTipViewController = null;
    }
}