package translatedemo.com.translatedemo.recever;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindArray;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.TransDataActivty;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.interfice.Translateinterfice;
import translatedemo.com.translatedemo.rxjava.Api;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

public final class TipViewController implements View.OnClickListener, View.OnTouchListener, ViewContainer.KeyEventHandler {

    private WindowManager mWindowManager;
    private Context mContext;
    private ViewContainer mWholeView;
    private View mContentView;
    private ViewDismissHandler mViewDismissHandler;
    private CharSequence mContent;
    private TextView mTextView;
    private ImageView colose;
    private LinearLayout pop_view_content_view;
    private String returndata = "";

    private TextView returndata_text;
    private Translateinterfice mlister;
    private TextView translate_title_type;

//    String[] choicedata;
    String[] choicedata1;
    public TipViewController(Context application, CharSequence content) {
        mContext = application;
        mContent = content;
        mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
    }

    public TipViewController(Context application, CharSequence content,String returndata,Translateinterfice mliste) {
        mContext = application;
        mContent = content;
        this.mlister=mliste;
        this.returndata = returndata;
//        choicedata = application.getResources().getStringArray(R.array.translate_choiceimage);
        choicedata1= application.getResources().getStringArray(R.array.translate_choiceimage1);
        mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setViewDismissHandler(ViewDismissHandler viewDismissHandler) {
        mViewDismissHandler = viewDismissHandler;
    }

    public void updateContent(CharSequence content) {
        mContent = content;
        mTextView.setText(mContent);
    }
    public void updateContent(CharSequence content,String returndata) {
        mContent = content;
        this.returndata = returndata;
        returndata_text.setText(returndata);
        translate_title_type.setText(choicedata1[PreferencesUtils.getInstance().getInt(Contans.INPUT_STRING,2)]+"->"+choicedata1[PreferencesUtils.getInstance().getInt(Contans.OUTPUT_STRING,1)]);
        mTextView.setText(mContent);
        int w = WindowManager.LayoutParams.MATCH_PARENT;
        int h = WindowManager.LayoutParams.MATCH_PARENT;

        int flags = 0;
        int type = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
//        layoutParams.gravity = Gravity.TOP;
        layoutParams.gravity = Gravity.TOP;
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(mContext)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                mContext.startActivity(intent);
                return;
            } else {
                try {
                    mWindowManager.addView(mWholeView, layoutParams);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } else {
            //Android6.0以下，不用动态声明权限
            mWindowManager.addView(mWholeView, layoutParams);
        }


    }

    public void show() {

        ViewContainer view = (ViewContainer) View.inflate(mContext, R.layout.pop_view, null);

        // display content
        mTextView = (TextView) view.findViewById(R.id.pop_view_text);
        translate_title_type = view.findViewById(R.id.translate_title_type);
        translate_title_type.setText(choicedata1[PreferencesUtils.getInstance().getInt(Contans.INPUT_STRING,2)]+"->"+choicedata1[PreferencesUtils.getInstance().getInt(Contans.OUTPUT_STRING,1)]);
        mTextView.setText(mContent);
        pop_view_content_view = view.findViewById(R.id.pop_view_content_view);
        mWholeView = view;
        mContentView = view.findViewById(R.id.pop_view_content_view);
        returndata_text = view.findViewById(R.id.returndata_text);
        returndata_text.setText(returndata);
        colose = view.findViewById(R.id.colose);
        colose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePoppedViewAndClear();
            }
        });
        view.findViewById(R.id.moredata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlister.evntbustomessage(mContent.toString(),returndata);
                removePoppedViewAndClear();
            }
        });


        // event listeners
//        mContentView.setOnClickListener(this);
        mWholeView.setOnTouchListener(this);
        mWholeView.setKeyEventHandler(this);

        int w = WindowManager.LayoutParams.MATCH_PARENT;
        int h = WindowManager.LayoutParams.MATCH_PARENT;

        int flags = 0;
        int type = 0;


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //解决Android 7.1.1起不能再用Toast的问题（先解决crash）
//            if(Build.VERSION.SDK_INT > 24){
//                type = WindowManager.LayoutParams.TYPE_PHONE;
//            }else{
//                type = WindowManager.LayoutParams.TYPE_TOAST;
//            }
//        } else {
//            type = WindowManager.LayoutParams.TYPE_PHONE;
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP;
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(mContext)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                mContext.startActivity(intent);
                return;
            } else {
                mWindowManager.addView(mWholeView, layoutParams);
            }
        } else {
            //Android6.0以下，不用动态声明权限
            mWindowManager.addView(mWholeView, layoutParams);
        }



    }

    @Override
    public void onClick(View v) {
        removePoppedViewAndClear();
//        MainActivity.startForContent(mContext, mContent.toString());

    }

    private void removePoppedViewAndClear() {

        // remove view
        if (mWindowManager != null && mWholeView != null) {
            mWindowManager.removeView(mWholeView);
        }

        if (mViewDismissHandler != null) {
            mViewDismissHandler.onViewDismiss();
        }

        // remove listeners
//        mContentView.setOnClickListener(null);
        mWholeView.setOnTouchListener(null);
        mWholeView.setKeyEventHandler(null);
    }

    /**
     * touch the outside of the content view, remove the popped view
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Rect rect = new Rect();
        mContentView.getGlobalVisibleRect(rect);
        if (!rect.contains(x, y)) {
            removePoppedViewAndClear();
        }
        return false;
    }

    private void tanslatedata(final String centent){




        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    okPost("en","zh",centent,102);
                }catch (Exception e){
                    new LogUntil(mContext,"content",e.getMessage());
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

                    View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                    TextView textView = textv.findViewById(R.id.text);
                    textView.setText(mdata);
                    pop_view_content_view.addView(textv);
                }
            }
        }
    };

    private void okPost(String input_type ,String outputtype, String data,final int type) throws IOException {
//        String path = "http://sz-nmt-1.cloudtrans.org:2201/nmt"+"?lang="+outputtype+"&src="+data;
        String path = Api.trasnslate_url+"?from="+input_type+"&to="+outputtype+"&apikey=" + Api.translate_key + "&src_text="+data;

        //   new LogUntil(mContext,TAG,"path:"+path);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        Request request = new Request.Builder()
                .url(path)
//                .post(body)
                .addHeader("Content-Type","application/x-www-form-urlencoded; charset=" + "utf-8")
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
                try {
                    Log.e("returnmedsage",string);
                    JSONObject json = new JSONObject(string);
                    if(json.has("tgt_text")){
                        string = json.getString("tgt_text");

                    }else{
                        string = "[ERR]";
                    }
                    Message msg = new Message();
                    msg.obj = string;
                    msg.arg1 = type;
                    mmhander.sendMessage(msg);
                }catch (Exception e){

                }
            }
        });
//        return response.body().string();
    }
    @Override
    public void onKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            removePoppedViewAndClear();
        }
    }

    public interface ViewDismissHandler {
        void onViewDismiss();
    }
}
