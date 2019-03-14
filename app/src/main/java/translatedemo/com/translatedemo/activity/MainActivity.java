package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.MainPagerAdapter;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.OverMainactivty;
import translatedemo.com.translatedemo.eventbus.TransTypeEvent;
import translatedemo.com.translatedemo.eventbus.TranslateEvent;
import translatedemo.com.translatedemo.eventbus.UpdateMainIndex;
import translatedemo.com.translatedemo.eventbus.UserDeteEvent;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.interfice.Translateinterfice;
import translatedemo.com.translatedemo.recever.ListenClipboardService;
import translatedemo.com.translatedemo.recever.TipViewController;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.DeviceUuidFactory;
import translatedemo.com.translatedemo.util.FileUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.ForbidScrollViewpager;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager_activity_main)
    public ForbidScrollViewpager mViewPager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private boolean startthred = true;
    private MainPagerAdapter mMainPagerAdapter;
    private Dialog mLoadingDialog;
    private int type = 0;
    private Intent intent;
    ClipboardManager clipboard;
    private TipViewController mTipViewController;
    private String mycontent = "";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    mViewPager.setCurrentItem(0,false);
                    UIUtils.showFullScreen(MainActivity.this,true);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1,false);
                    UIUtils.showFullScreen(MainActivity.this,true);
                    updateactionbar();
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2   ,false);
                    UIUtils.showFullScreen(MainActivity.this,true);
                    updateactionbar();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
    }



    @Override
    protected void initData() {
        super.initData();

        EventBus.getDefault().register(this);
        UIUtils.showFullScreen(MainActivity.this,true);

        type = getIntent().getIntExtra(Contans.INTENT_TYPE,0);
        mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mMainPagerAdapter);
        mViewPager.setCurrentItem(0);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(type>0&&BaseActivity.getuser()!=null){
            getuserinfo(BaseActivity.getuser().id+"");
        }

        creartFilepath();
        checklogin();

         clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

// 添加剪贴板数据改变监听器
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                // 剪贴板中的数据被改变，此方法将被回调
                new LogUntil(MainActivity.this,TAG,"onPrimaryClipChanged");
                ClipData clipData = clipboard.getPrimaryClip();

                if (clipData != null && clipData.getItemCount() > 0) {
                    // 从数据集中获取（粘贴）第一条文本数据
                    CharSequence text = clipData.getItemAt(0).getText();
                    if(!TextUtils.isEmpty(text)){
                        new LogUntil(MainActivity.this,TAG,text.toString());

                        String content = text.toString();
                        if(PreferencesUtils.getInstance().getBoolean(Contans.TANSLATEFORKRJ,false)){
                            String mdata = content.toString() .replace("'","")
                                    .replace(" ","")
                                    .replace("<","")
                                    .replace(">","")
                                    .replace("-","");;
                            if(UIUtils.isEnglish(mdata)) {
//                if (Build.MANUFACTURER.equals("Xiaomi")) {
//                                Intent mIntent = new Intent(mcontent,TransDataActivty.class);
//                                mIntent.putExtra(TransDataActivty.DATA,content.toString());
//                                mIntent.putExtra(TransDataActivty.TYPE,1);
//                                mcontent.startActivity(mIntent);
//                } else {
                    mycontent = content;
                    tanslatedata(mycontent);
//                    if (mTipViewController != null) {
//                        mTipViewController.updateContent(content);
//                    } else {
//                        mTipViewController = new TipViewController(getApplication(), content);
//                        mTipViewController.setViewDismissHandler(new TipViewController.ViewDismissHandler() {
//                            @Override
//                            public void onViewDismiss() {
//
//                            }
//                        });
//                        mTipViewController.show();
//                    }
//                }
                            }
                        }
                    }
                }

            }
        });



    }

    public static void startactivity(Context mcontent,int type){
        Intent mIntent = new Intent(mcontent,MainActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mcontent.startActivity(mIntent);
    }




    public void getuserinfo(String id){


        Observable observable =
                ApiUtils.getApi().getuserinfo(id,BaseActivity.getLanguetype(this))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(MainActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LoginBean>(MainActivity.this) {
            @Override
            protected void _onNext(StatusCode<LoginBean> stringStatusCode) {
                PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,new Gson().toJson(stringStatusCode.getData()));
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
//
    }

    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private void creartFilepath(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileUtils.APP_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/translate";
        } else {
            FileUtils.APP_DIR = this.getFilesDir().getAbsolutePath() + "/translate";
        }
        FileUtils.APP_LOG = FileUtils.APP_DIR + "/log";
        FileUtils.APP_CRASH = FileUtils.APP_DIR + "/crash";
        FileUtils.APK_DIR = FileUtils.APP_DIR + "/apks";
        FileUtils.IMAGE_DIR = FileUtils.APP_DIR + "/imag";

        File appDir = new File(FileUtils.APP_DIR);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File logDir = new File(FileUtils.APP_LOG);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        File crashDir = new File(FileUtils.APP_CRASH);
        if (!crashDir.exists()) {
            crashDir.mkdirs();
        }
        File apkDir = new File(FileUtils.APK_DIR);
        if (!apkDir.exists()) {
            apkDir.mkdirs();
        }
        File imageDir = new File(FileUtils.IMAGE_DIR);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
    }
    private int mindex = 0;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void steindex(UpdateMainIndex index){

        if(index.index!=0) {
            Log.e("index",index.index+"");
            mindex = index.index;
            mViewPager.setCurrentItem(index.index,false);

            UIUtils.showFullScreen(MainActivity.this, false);
            updateactionbar();
        }else{
            navigation.setSelectedItemId(navigation.getMenu().getItem(index.index).getItemId());
            mOnNavigationItemSelectedListener.onNavigationItemSelected(navigation.getMenu().getItem(index.index));
            mViewPager.setCurrentItem(index.index,false);

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_DEL&& event.getAction() == KeyEvent.ACTION_DOWN){
             return true;
         }

        if (keyCode == event.KEYCODE_BACK) {
             if(mindex!=0){
                 navigation.setSelectedItemId(navigation.getMenu().getItem(0).getItemId());
                 mOnNavigationItemSelectedListener.onNavigationItemSelected(navigation.getMenu().getItem(0));
             }else {
                 exit();
                 return false;
             }
            return true;
        }
        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishMainactivity(OverMainactivty over){
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void totranslate(TranslateEvent event){
        mViewPager.setCurrentItem(1,false);
        mOnNavigationItemSelectedListener.onNavigationItemSelected(navigation.getMenu().getItem(1));
        UIUtils.showFullScreen(MainActivity.this,true);
        updateactionbar();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        startthred = false;
        EventBus.getDefault().unregister(this);
    }
    private void exit() {
        if (!isExit) {
            isExit = true;
             ToastUtils.makeText(mcontent.getResources().getString(R.string.exite_dialog));
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            showdialogtc();

        }
    }



    private void checklogin(){

        Observable observable =
                ApiUtils.getApi().checkLogin(BaseActivity.getLanguetype(MainActivity.this)+"",BaseActivity.getuser().id+"",DeviceUuidFactory.getInstance(MainActivity.this).getDeviceUuid())
                        .compose(RxHelper.getObservaleTransformer())
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Integer>(MainActivity.this) {
            @Override
            protected void _onNext(StatusCode<Integer> stringStatusCode) {
            if(stringStatusCode!=null&&stringStatusCode.getData()==0){
                timer();
            }else{
                EventBus.getDefault().post(new UserDeteEvent());
                showdialog();
            }

            }

            @Override
            protected void _onError(String message) {
                new LogUntil(MainActivity.this,TAG,message);
                timer();
            }
        }, "", lifecycleSubject, false, true);
    }

    private void showdialog(){
        AlertView alertView = new AlertView("提示", "您的账号已在其他地方登录,是否重新登录～", null, null, new String[]{"取消", "确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position==1){
                    PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER, "");
                    BaseActivity.user = null;
                    LoginActivity.startactivity(MainActivity.this);
                    finish();
                }else{
                    finish();
                }

            }
        });
        alertView.show();
    }

    private void showdialogtc(){
        AlertView alertView = new AlertView("提示", "确定退出翻译宝～", null, null, new String[]{"取消", "确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position==1){
                    finish();
                    System.exit(0);
                }else{
                    isExit = false;
                }

            }
        });
        alertView.show();
    }

    private void timer(){

        new Thread(){
            @Override
            public void run() {
                super.run();
                if(startthred){
                    try{
                        Thread.sleep(1000*60);
                        checklogin();
                    }catch (Exception e){

                    }
                }
            }
        }.start();
    }


    private void tanslatedata(final String centent){




        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    okPost("en_zh-cn",centent,102);
                }catch (Exception e){
                    new LogUntil(mcontent,"content",e.getMessage());
                }

            }
        }.start();


    }
    private String mdata = "";
    Handler mmhander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==102){
                mdata = msg.obj+"";
                if(mdata.indexOf("[ERR]")>=0){
                    ToastUtils.makeText("翻译失败");
                }else{

                    if (mTipViewController != null) {
                        mTipViewController.updateContent(mycontent,mdata);
                    } else {
                        mTipViewController = new TipViewController(getApplication(), mycontent,mdata,mlister);
                        mTipViewController.setViewDismissHandler(new TipViewController.ViewDismissHandler() {
                            @Override
                            public void onViewDismiss() {

                            }
                        });
                        mTipViewController.show();
                    }
                }
            }
        }
    };

    Translateinterfice mlister = new Translateinterfice() {
        @Override
        public void evntbustomessage(String data) {
            Intent mIntent = new Intent(mcontent,TransDataActivty.class);
            mIntent.putExtra(TransDataActivty.DATA,data.toString());
            mIntent.putExtra(TransDataActivty.TYPE,1);
            mcontent.startActivity(mIntent);
        }
    };

    private void okPost(String outputtype, String data,final int type) throws IOException {
        String path = "https://nmt.xmu.edu.cn/nmt"+"?lang="+outputtype+"&src="+data;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message msg = new Message();
                msg.obj = string;
                msg.arg1 = type;
                mmhander.sendMessage(msg);
            }
        });
//        return response.body().string();
    }


}
