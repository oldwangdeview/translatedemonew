package translatedemo.com.translatedemo.fragment;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.LoginActivity;
import translatedemo.com.translatedemo.activity.MainActivity;
import translatedemo.com.translatedemo.activity.NoticeDetailActivity;
import translatedemo.com.translatedemo.activity.PeopleTranslateActivity;
import translatedemo.com.translatedemo.activity.UserinfoActivity;
import translatedemo.com.translatedemo.adpater.ChoiceLangvageAdpater;
import translatedemo.com.translatedemo.adpater.HisotryAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.base.BaseFragment;
import translatedemo.com.translatedemo.bean.CollectionListbean;
import translatedemo.com.translatedemo.bean.HistoryBean;
import translatedemo.com.translatedemo.bean.InformationBean;
import translatedemo.com.translatedemo.bean.InputHistoryBean;
import translatedemo.com.translatedemo.bean.ListBean_information;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.TranslateEvent;
import translatedemo.com.translatedemo.eventbus.UpdateMainIndex;
import translatedemo.com.translatedemo.eventbus.UpdateUserEvent;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.interfice.TextonClickLister;
import translatedemo.com.translatedemo.rxjava.Api;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.rxjava.TranslateApi;
import translatedemo.com.translatedemo.rxjava.TranslateApiUtils;
import translatedemo.com.translatedemo.util.CustomClickableSpan;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.ChoiceDucationDialog;
import translatedemo.com.translatedemo.view.ChoiceLangageDialog;

/**
 * Created by oldwang on 2018/12/30 0030.
 */

public class InformationFragment extends BaseFragment {

    @BindView(R.id.id_cb)
    ConvenientBanner mConvenientBanner;
    @BindView(R.id.choice_text1)
    TextView Choice_text1;
    @BindView(R.id.choice_text2)
    TextView Choice_text2;

    @BindView(R.id.input_editet)
    EditText input_editet;
    @BindArray(R.array.main_translate)
    String[] myOrderTitles;
    @BindView(R.id.data)
    LinearLayout data;
    @BindView(R.id.history_linearlayout)
    LinearLayout history_linearlayout;
    @BindView(R.id.translatedata)
    RelativeLayout translatedata;
    @BindView(R.id.history_data)
    GridView history_data;

    @BindArray(R.array.translate_choiceimage1)
    String[] choicedata1;

    HisotryAdpater historyadpater;
    private Dialog mLoadingDialog;
    private ChoiceLangageDialog choicelangage1,choicelangage2;
    private ChoiceLangvageAdpater chiceadpater1,chiceadpater2;
    private View tanslaterequest;
    private String inoputtexttype = "";
    private String outputexttype = "" ;
    private TextView content;
    private ImageView shoucangimage;



    String yiwen = "";

    private boolean updateinput = true;
    private boolean settxetsize = false;
    private List<InputHistoryBean> historylistdata = new ArrayList<>();
    private List<InputHistoryBean> adpaterdata = new ArrayList<>();
    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_information);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        chiceadpater1 = new ChoiceLangvageAdpater(mContext,choicedata1,UIUtils.image1);
        chiceadpater2 = new  ChoiceLangvageAdpater(mContext,choicedata1,UIUtils.image1);
        tanslaterequest = UIUtils.inflate(mContext,R.layout.layout_translate_request);
        shoucangimage = tanslaterequest.findViewById(R.id.shouc_image);
        content = tanslaterequest.findViewById(R.id.text);
        tanslaterequest.findViewById(R.id.shouc_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionDictionary(input_editet.getText().toString().trim());
            }
        });
        Choice_text1.setText(choicedata1[1]);
        Choice_text2.setText(choicedata1[0]);
        tanslaterequest.findViewById(R.id.fuzhi_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
                    String text = content.getText().toString().trim();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
// 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtils.makeText("已复制");
                }catch (Exception e){

                }
            }
        });


        gethistorydata();
        tanslaterequest.findViewById(R.id.pople_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeopleTranslateActivity.startactivity(mContext);
            }
        });

        chiceadpater1.steonitemclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                Choice_text1.setText(choicedata1[position]);
                if(choicelangage1!=null){
                    choicelangage1.dismiss();
                }
                String content = input_editet.getText().toString().trim();
                try {
                    content = new String(content.getBytes(), "utf-8");
                }catch (Exception e){

                }
                String inputdata = Choice_text1.getText().toString().trim();
                String outputdata = Choice_text2.getText().toString().trim();
                if(!TextUtils.isEmpty(content)&&!TextUtils.isEmpty(inputdata)&&!TextUtils.isEmpty(outputdata)){
                    if(history_linearlayout.getVisibility()==View.VISIBLE){
                        history_linearlayout.setVisibility(View.GONE);
                        translatedata.setVisibility(View.VISIBLE);
                    }
                    translatedata(URLEncoder.encode(content),inputdata,outputdata);
                }else{
                    data.removeAllViews();
                }

            }
        });
        chiceadpater2.steonitemclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                Choice_text2.setText(choicedata1[position]);
                if(choicelangage2!=null){
                    choicelangage2.dismiss();
                }
                String content = input_editet.getText().toString().trim();
                try {
                    content = new String(content.getBytes(), "utf-8");
                }catch (Exception e){

                }
                String inputdata = Choice_text1.getText().toString().trim();
                String outputdata = Choice_text2.getText().toString().trim();
                if(!TextUtils.isEmpty(content)&&!TextUtils.isEmpty(inputdata)&&!TextUtils.isEmpty(outputdata)){
                    if(history_linearlayout.getVisibility()==View.VISIBLE){
                        history_linearlayout.setVisibility(View.GONE);
                        translatedata.setVisibility(View.VISIBLE);
                    }
                    translatedata(URLEncoder.encode(content),inputdata,outputdata);

                }else{
                    data.removeAllViews();
                }

            }
        });

        if(BaseActivity.getuser().isMember==0) {
            getbannerdata();
        }


        input_editet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputdata = input_editet.getText().toString().trim();
                if(translatedata.getVisibility()==View.VISIBLE){
                    translatedata.setVisibility(View.GONE);
                    history_linearlayout.setVisibility(View.VISIBLE);
                }
                if(inputdata.length()>=50){
                    if(!settxetsize) {
                        input_editet.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelOffset(R.dimen.sp_18));
                        settxetsize = !settxetsize;
                    }
                }else{
                    if(settxetsize) {
                        input_editet.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelOffset(R.dimen.sp_23));
                        settxetsize = !settxetsize;
                    }
                }
                if(updateinput){
                    if(historylistdata.size()>0) {
                        adpaterdata.clear();
                        if (!TextUtils.isEmpty(inputdata)) {
                            for (InputHistoryBean mdatas : historylistdata) {

                                if(mdatas.inoutdata.indexOf(inputdata)>=0){
                                    adpaterdata.add(mdatas);
                                }
                            }
                            if(adpaterdata.size()==0){
                                adpaterdata.addAll(historylistdata);
                            }

                        } else {

                            adpaterdata.addAll(historylistdata);
                        }
                        if(historyadpater!=null) {
                            historyadpater.notifyDataSetChanged();
                        }
                    }
                }else{
                    updateinput = true;
                }
            }
        });

//        input_editet.setInputType(EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
//        input_editet.setImeOptions(EditorInfo.IME_ACTION_GO);
        input_editet.setOnEditorActionListener(mlister);

        tanslaterequest.findViewById(R.id.shared_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(mContext, R.mipmap.shared_image_4);//资源文件
                UMWeb web = new UMWeb(Api.Shared_LODURL);
                web.setTitle("雅鲁翻译通"+"——"+getResources().getString(R.string.shared_message1));//标题
                web.setThumb(image);  //缩略图
                web.setDescription(TextUtils.isEmpty(mdata)?getResources().getString(R.string.share_text_content):getResources().getString(R.string.yuanwen)+input_editet.getText().toString()+" "+getResources().getString(R.string.yiwen)+mdata);//描述
                new ShareAction(InformationFragment.this.getActivity())
                        .withMedia(web)
                        .setCallback(shareListener)
                        .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                        .open();

            }
        });
        historyadpater = new HisotryAdpater(adpaterdata,mContext);
        historyadpater.setonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {

                Message msg = new Message();
                msg.arg1 = 101;
                msg.obj = adpaterdata.get(position).outputdata;
                updateinput = false;
                input_editet.setText(adpaterdata.get(position).inoutdata);
                mhandler.sendMessage(msg);
            }
        });
        history_data.setAdapter(historyadpater);
    }

    private void showdialog(String data){
        AlertView alertView = new AlertView("快捷翻译", data, null, null, new String[]{"取消"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {


            }
        });
        alertView.show();
    }

    private int clickindex(String input,String outpout){
           String data = input.replace("语","")+outpout.replace("语","");
           for(int i =0;i<myOrderTitles.length;i++){
               if(data.equals(myOrderTitles[i])){
                   return i+1;
               }
           }
           return -1;
    }



    @OnClick({R.id.choice_text1,R.id.choice_text2})
    public void choice_text(View v){

        switch (v.getId()){
            case R.id.choice_text1:
                if (choicelangage1!=null&&choicelangage1.isShowing()){
                    choicelangage1.dismiss();
                }
                    choicelangage1 = new ChoiceLangageDialog.Builder(mContext).setCanCancel(chiceadpater1).create();
                    choicelangage1.show();

                break;
            case R.id.choice_text2:
                if (choicelangage2!=null&&choicelangage2.isShowing()){
                    choicelangage2.dismiss();
                }
                    choicelangage2 = new ChoiceLangageDialog.Builder(mContext).setCanCancel(chiceadpater2).create();
                    choicelangage2.show();


                break;
        }
    }


    private void getbannerdata(){

        Observable observable =
                ApiUtils.getApi().getinformationlist(BaseActivity.getLanguetype(mContext),1, Contans.cow,BaseActivity.getuser().id+"",1,"3")
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<InformationBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<InformationBean> stringStatusCode) {
                new LogUntil(mContext,TAG+"zixunmessage",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getData()!=null&&stringStatusCode.getData().list.size()>0) {
                    List<ListBean_information> adsBeans = stringStatusCode.getData().list;
                    if (adsBeans != null && adsBeans.size() > 0) {
                        mConvenientBanner.setPages(new CBViewHolderCreator<InformationFragment.MImageViewHolder>() {
                            @Override
                            public InformationFragment.MImageViewHolder createHolder() {
                                return new InformationFragment.MImageViewHolder();
                            }
                        }, adsBeans)
                                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL); //设置指示器的方向水平  居中
                    }
                    if (adsBeans.size() == 1) {
                        mConvenientBanner.setPointViewVisible(false);
                    } else {
                        mConvenientBanner.setPointViewVisible(true);
                    }
                }

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);


    }

     private void translatedata(final String content,final String inoutdara,final String outputdata){
         new LogUntil(mContext,TAG+"content",content);
         new LogUntil(mContext,TAG+"outputexttype",outputexttype);
         getcollectionstats(content);
//         if (mLoadingDialog == null) {
//             mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
//         }
//         LoadingDialogUtils.show(mLoadingDialog);
         new Thread(){
             @Override
             public void run() {
                 super.run();
                 try {
                     okPost(UIUtils.getlangvagetype(inoutdara,choicedata1),UIUtils.getlangvagetype(outputdata,choicedata1),content,101);
                 }catch (Exception e){
                     new LogUntil(mContext,TAG+"content",e.getMessage());
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
                     okPost("en","zh",centent,102);
                 }catch (Exception e){
                     new LogUntil(mContext,TAG+"content",e.getMessage());
                 }

             }
         }.start();


     }


    public class MImageViewHolder implements Holder<ListBean_information> {
        private View mhandeview;
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            mhandeview = UIUtils.inflate(mContext,R.layout.translate_banner_item);
            return mhandeview;
        }

        @Override
        public void UpdateUI(Context context, int position, ListBean_information data) {
            imageView = mhandeview.findViewById(R.id.image);

            final  String weburl = data.url;
            UIUtils.loadImageView(mContext,data.image,imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.openWebUrl(mContext,weburl);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mConvenientBanner.startTurning(3000);
    }

    private String mdata = "";
    Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==101){
                 mdata = msg.obj+"";
                data.removeAllViews();

                new LogUntil(mContext,TAG+"zixunmessage_xxx",mdata);
                if(mdata.indexOf("[ERR]")>=0){

                }else{
                    if(history_linearlayout.getVisibility()==View.VISIBLE){
                        history_linearlayout.setVisibility(View.GONE);
                        translatedata.setVisibility(View.VISIBLE);
                    }
                    layoutContent(content," "+mdata+" ");
                    data.addView(tanslaterequest);
                }
            }

            if(msg.arg1==102){
                mdata = msg.obj+"";
                if(mdata.indexOf("[ERR]")>=0){
                    showdialog(mdata);
                }else{
                    showdialog(mdata);
                }
            }

        }
    };
    @Override
    public void onPause() {
        super.onPause();
        mConvenientBanner.stopTurning();
    }
     private void okPost(String input_type ,String outputtype, String data,final int type) throws IOException {
//        String path = "http://sz-nmt-1.cloudtrans.org:2201/nmt"+"?lang="+outputtype+"&src="+data;
          String path = Api.trasnslate_url+"?from="+input_type+"&to="+outputtype+"&apikey=" + Api.translate_key + "&src_text="+data;

     //   new LogUntil(mContext,TAG,"path:"+path);
      OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
//         RequestBody body = new FormBody.Builder()
//                 .add("Content-Type","application/x-www-form-urlencoded; charset=" + "utf-8")
//                 .add("from", input_type)
//                 .add("to", outputtype)
//                 .add("apikey", Api.translate_key)
//                 .add("src_text",data)
//                 .build();
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
                if(type==101){
                    addhistorydata(new InputHistoryBean(input_editet.getText().toString().trim(),mdata));
                }
                mhandler.sendMessage(msg);
                }catch (Exception e){

                }
            }
        });

//        return response.body().string();
    }

    TextView.OnEditorActionListener mlister = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            new LogUntil(mContext,TAG,actionId+"");
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                //处理事件
                String content = input_editet.getText().toString().trim();
                try {
                    content = new String(content.getBytes(), "utf-8");
                }catch (Exception e){

                }
                String inputdata = Choice_text1.getText().toString().trim();
                String outputdata = Choice_text2.getText().toString().trim();
                if(history_linearlayout.getVisibility()==View.VISIBLE){
                    history_linearlayout.setVisibility(View.GONE);
                    translatedata.setVisibility(View.VISIBLE);
                }
                if(!TextUtils.isEmpty(content)&&!TextUtils.isEmpty(inputdata)&&!TextUtils.isEmpty(outputdata)){
                    translatedata(URLEncoder.encode(content),inputdata,outputdata);
                }else{
                    data.removeAllViews();
                }
            }
            return true;
        }
    };

    /**
     * 收藏
     */

   private String minputdata = "";
   private boolean sctype = false;
    private void collectionDictionary(String inoutdata){
        if(minputdata.equals(inoutdata)){

        }else{
            minputdata = inoutdata;
            sctype = false;
        }
        Observable observable =
                ApiUtils.getApi().collectionDictionary(BaseActivity.getLanguetype(mContext),BaseActivity.getuser().id+"",clickindex(Choice_text1.getText().toString().trim(),Choice_text1.getText().toString().trim()),inoutdata,mdata,"","0",sctype?1:0)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mContext) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                new LogUntil(mContext,TAG+"zixunmessage",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                sctype = !sctype;
                if(sctype) {
                    shoucangimage.setImageResource(R.mipmap.shoucang2);
                }else{
                    shoucangimage.setImageResource(R.mipmap.shoucang_hui);
                }

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }

    @OnClick(R.id.qieh)
    public void qieh(){
        String text1 = Choice_text1.getText().toString().trim();
        String text2 = Choice_text2.getText().toString().trim();
        Choice_text1.setText(text2);
        Choice_text2.setText(text1);
        input_editet.setText("");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setdata(CollectionListbean mdata) {

        input_editet.setText(mdata.content);
        Message msg = new Message();
        msg.arg1 = 101;
        if(!TextUtils.isEmpty(mdata.translateContent)) {
            msg.obj = mdata.translateContent;
        }
        updateinput = false;
        mhandler.sendMessage(msg);
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @param platform 平台类型
         * @descrption 分享开始的回调
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

            ToastUtils.makeText("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mContext).onActivityResult(requestCode,resultCode,data);

    }

    private void layoutContent(TextView textView, String content) {
        if (!content.contains(" ")) {
            textView.setText(content);
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(content);

        int index = 0;
//        Today's weather
        String datam = content.trim().replace("'","")
                .replace(" ","")
                .replace("<","")
                .replace(">","")
                .replace("-","")
                .replace(",","")
                .replace("?","")
                .replace("!","")
                .replace(".","")
                .replace("。","");

        if(UIUtils.isEnglish((datam))) {
            content = content.replace("'","")
                    .replace("<","")
                    .replace(">","")
                    .replace("-","")
                    .replace(",","")
                    .replace("?","")
                    .replace("!","")
                    .replace(".","")
                    .replace("。","");
            if(content.indexOf(" ")>=0||content.indexOf(",")>=0){



                String[] contentlist = null;
                if(content.indexOf(" ")>=0){
                    contentlist  = content.split(" ");
                }
//                for(int )

                for (int i = 0; i < contentlist.length; i++) {
                    int atIndex = content.indexOf(contentlist[i]);
                    int sIndex = atIndex + contentlist[i].length();
                    index = sIndex;
                    new LogUntil(mContext, TAG, content.substring(atIndex, sIndex));
                    if (sIndex < atIndex) { //格式不符合
                        continue;
                    }
                    CustomClickableSpan clickableSpan = new CustomClickableSpan(mContext, atIndex, new TextonClickLister() {
                        @Override
                        public void clickText(String data) {
                            new LogUntil(mContext, TAG, "点击单词：" + data);
                            tanslatedata(data);
                        }
                    },sIndex);
                    builder.setSpan(clickableSpan, atIndex, sIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }else{
                int atIndex = 0;
                int sIndex = content.length();
                index = sIndex;
                new LogUntil(mContext, TAG, content.substring(atIndex, sIndex));
                if (sIndex > atIndex) { //格式不符合

                CustomClickableSpan clickableSpan = new CustomClickableSpan(mContext, atIndex, new TextonClickLister() {
                    @Override
                    public void clickText(String data) {
                        new LogUntil(mContext, TAG, "点击单词：" + data);
                        tanslatedata(data);
                    }
                },sIndex);
                builder.setSpan(clickableSpan, atIndex, sIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                }
            }
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(builder);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uodateuser( UpdateUserEvent uodate){
        mConvenientBanner.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void totranslate(TranslateEvent event){
        input_editet.setText(event.data);

            Choice_text1.setText(choicedata1[PreferencesUtils.getInstance().getInt(Contans.INPUT_STRING,2)]);
            Choice_text2.setText(choicedata1[PreferencesUtils.getInstance().getInt(Contans.OUTPUT_STRING,1)]);

        String mcontent = input_editet.getText().toString().trim();
        if(TextUtils.isEmpty(event.requestdata)) {
            try {
                mcontent = new String(mcontent.getBytes(), "utf-8");
            } catch (Exception e) {

            }
            String inputdata = Choice_text1.getText().toString().trim();
            String outputdata = Choice_text2.getText().toString().trim();
            if (!TextUtils.isEmpty(mcontent) && !TextUtils.isEmpty(inputdata) && !TextUtils.isEmpty(outputdata)) {
                if (history_linearlayout.getVisibility() == View.VISIBLE) {
                    history_linearlayout.setVisibility(View.GONE);
                    translatedata.setVisibility(View.VISIBLE);
                }
                translatedata(URLEncoder.encode(mcontent), inputdata, outputdata);
            } else {
                data.removeAllViews();
            }

        }else{
            if(history_linearlayout.getVisibility()==View.VISIBLE){
                history_linearlayout.setVisibility(View.GONE);
                translatedata.setVisibility(View.VISIBLE);
            }
            layoutContent(content," "+event.requestdata+" ");
            data.addView(tanslaterequest);
        }
    }


    private void getcollectionstats(String content){
        Observable observable =
                ApiUtils.getApi().getCollectionStatus(BaseActivity.getLanguetype(mContext),BaseActivity.getuser().id+"",content,0)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Integer>(mContext) {
            @Override
            protected void _onNext(StatusCode<Integer> stringStatusCode) {
                new LogUntil(mContext,TAG+"zixunmessage",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);

                if(stringStatusCode.getData()!=null&&stringStatusCode.getData()>=0){
                    if(stringStatusCode.getData()==1){

                        sctype = false;
                        shoucangimage.setImageResource(R.mipmap.shoucang2);


                    }else{
                        sctype = false;
                        shoucangimage.setImageResource(R.mipmap.shoucang_hui);
                    }
                }

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }

    public void addhistorydata(InputHistoryBean data){
        historylistdata.add(0,data);
        savedata(data.inoutdata,data.outputdata); }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getupdateindex(UpdateMainIndex event){
        if(event.index==3){
            input_editet.setText("");
        }
    }



    private void savedata(String inputdata,String outputdata){
        Observable observable =
                ApiUtils.getApi().addTranslateRecord(BaseActivity.getLanguetype(mContext),BaseActivity.getuser().id+"",1,inputdata,outputdata)
                        .compose(RxHelper.getObservaleTransformer())
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mContext) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {



            }

            @Override
            protected void _onError(String message) {

            }
        }, "", lifecycleSubject, false, true);
    }


    private void gethistorydata(){
        Observable observable =
                ApiUtils.getApi().getTranslateRecord(BaseActivity.getLanguetype(mContext),BaseActivity.getuser().id+"",1,10000,0)
                        .compose(RxHelper.getObservaleTransformer())
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<HistoryBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<HistoryBean> stringStatusCode) {


                if(stringStatusCode.getData()!=null&&stringStatusCode.getData().list.size()>0){
                    new LogUntil(mContext,TAG,"historydarta"+new Gson().toJson(stringStatusCode));

                 for(int i = 0;i<stringStatusCode.getData().list.size();i++){
                     InputHistoryBean data = new InputHistoryBean(stringStatusCode.getData().list.get(i).content,stringStatusCode.getData().list.get(i).translateContent);
                 }
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }, "", lifecycleSubject, false, true);
    }




}
