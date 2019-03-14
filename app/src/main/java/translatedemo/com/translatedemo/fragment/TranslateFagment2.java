package translatedemo.com.translatedemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.auth.APAuthInfo;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.FeedBackActivity;
import translatedemo.com.translatedemo.activity.Look_imageActivity;
import translatedemo.com.translatedemo.activity.MenberCenterActivity;
import translatedemo.com.translatedemo.activity.NoticeDetailActivity;
import translatedemo.com.translatedemo.activity.OffLineActivity;
import translatedemo.com.translatedemo.adpater.TanslateTitleAdpater;
import translatedemo.com.translatedemo.adpater.TranslateBottomAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.base.BaseFragment;
import translatedemo.com.translatedemo.bean.CollectionListbean;
import translatedemo.com.translatedemo.bean.DictionaryBean;
import translatedemo.com.translatedemo.bean.InformationBean;
import translatedemo.com.translatedemo.bean.LanguageBean;
import translatedemo.com.translatedemo.bean.LanuageListBean;
import translatedemo.com.translatedemo.bean.ListBean_information;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.bean.ThreeTranslateBean;
import translatedemo.com.translatedemo.bean.TranslateBean;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.NetEvent;
import translatedemo.com.translatedemo.eventbus.UpdateMainIndex;
import translatedemo.com.translatedemo.eventbus.UpdateUserEvent;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.interfice.TextonClickLister;
import translatedemo.com.translatedemo.rxjava.Api;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.CustomClickableSpan;
import translatedemo.com.translatedemo.util.FileUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/16 0016.
 */

public class TranslateFagment2  extends BaseFragment {


    @BindView(R.id.titlerecyclerview)
    RecyclerView titlerecyclerview;
    @BindView(R.id.bottom_recyclerview)
    RecyclerView bottom_recyclerview;
    @BindView(R.id.id_cb)
    ConvenientBanner mConvenientBanner;
    @BindView(R.id.translate_linyout)
    LinearLayout translate_linyout;
    @BindView(R.id.input_editext_titl)
    EditText input_editext_titl;
    @BindView(R.id.title_btn)
    TextView title_btn;
    @BindView(R.id.input_text)
    EditText input_text;
    @BindView(R.id.translate_iamge)
    ImageView translate_iamge;
    @BindView(R.id.shouc_image)
    ImageView shouc_image;

    @BindView(R.id.shared_image)
    ImageView shared_image;
    private List<LanuageListBean> languagelist  = new ArrayList<>();
    TanslateTitleAdpater madpater;
    private int clickindex = 0;
    private Dialog mLoadingDialog;
    private List<DictionaryBean> listdata = new ArrayList<>();
    private TranslateBottomAdpater translatebottomadpater;
    private View becomeview;
    private DictionaryBean choicecd = null;
    private View translate_requestdata;
    private View momessage_view;
    private View threretranslaterequestdata = null;
    private LinearLayout data;
    private boolean isshouc = false;
    private boolean haveenglish = false;
    private List<String> filelist = UIUtils.getFilesAllName( FileUtils.getSDRoot()+"/translate");
    private String filename = "";
    private TextView title;
    private TextView datadata;
    private LinearLayout mlinlayout;
    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.activity_translate);
    }


    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(TranslateFagment2.this);
        madpater = new TanslateTitleAdpater(mContext,languagelist,clickindex);
        madpater.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {

                new LogUntil(mContext,TAG,"clickindex"+languagelist.get(position).id+"");
                clickindex = languagelist.get(position).id;
                PreferencesUtils.getInstance().putInt(Contans.PERFICE_TRANSLATE_TITLE_CLICK,clickindex);
                haveenglish = false;
                if(languagelist.get(position).name.indexOf("英")>=0){
                    haveenglish = true;
                }
                dictionaryId = 0;
                getdictionary(clickindex);
                translate_linyout.removeAllViews();

            }
        });
        LinearLayoutManager mg = new LinearLayoutManager(mContext);
        mg.setOrientation(LinearLayoutManager.HORIZONTAL);
        titlerecyclerview.setLayoutManager(mg);
        titlerecyclerview.setItemAnimator(new DefaultItemAnimator());
        titlerecyclerview.setAdapter(madpater);
        threretranslaterequestdata = UIUtils.inflate(mContext,R.layout.item_threretranslate);

        title = threretranslaterequestdata.findViewById(R.id.tutle);
        datadata = threretranslaterequestdata.findViewById(R.id.data);
        mlinlayout = threretranslaterequestdata.findViewById(R.id.linlayout);

        LinearLayoutManager mg1 = new LinearLayoutManager(mContext);
        mg1.setOrientation(LinearLayoutManager.HORIZONTAL);
        translatebottomadpater = new TranslateBottomAdpater(mContext,listdata);
        bottom_recyclerview.setLayoutManager(mg1);
        bottom_recyclerview.setItemAnimator(new DefaultItemAnimator());
        bottom_recyclerview.setAdapter(translatebottomadpater);
        becomeview = UIUtils.inflate(mContext,R.layout.translate_becomevip_layout);
        becomeview.findViewById(R.id.become_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenberCenterActivity.startactivity(mContext);
            }
        });
        translate_requestdata = UIUtils.inflate(mContext,R.layout.translate_tansrequest_layout);
        momessage_view = UIUtils.inflate(mContext,R.layout.translate_nomessagedata);
        data = translate_requestdata.findViewById(R.id.data);
        translatebottomadpater.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                translate_linyout.removeAllViews();
                choicecd = listdata.get(position);
//                if(havecidian(listdata.get(position).name)){
                    filename =  FileUtils.getSDRoot()+"/translate/"+listdata.get(position).id+".json";
//                }
                if(listdata.get(position).isMemberVisible==1&&BaseActivity.getuser().isMember==0){
                    translate_linyout.addView(becomeview);
                }else{
                    if(translatetype) {
                        String inputString = input_editext_titl.getText().toString().trim();
                        if (!TextUtils.isEmpty(inputString) && choicecd.id > 0) {
                            translatecontent(inputString, clickindex, choicecd.id);
                        } else if (!TextUtils.isEmpty(inputString)) {

                            translateforniujin(inputString, choicecd.counts);

                        }
                    }else{
                        if(havecidian(listdata.get(position).name)){
                            filename = FileUtils.getSDRoot()+"/translate/"+listdata.get(position).id+".json";
                          TranslateBean returndata =   offlinetranlate(input_editext_titl.getText().toString().trim());
                          if(returndata!=null){
                              translate_linyout.removeAllViews();
                              data.removeAllViews();
                              String translateResult = returndata.contentTwo;
                              if(!TextUtils.isEmpty(translateResult)) {
                                  if (translateResult.indexOf(";") > 0) {
                                      String[] line = translateResult.split(";");
                                      for (int i = 0; i < line.length; i++) {
                                          View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                                          TextView textView = textv.findViewById(R.id.text);
                                          textView.setText(line[i]+"\n");
                                          final String cotent = line[i];
                                          if(!TextUtils.isEmpty(cotent)&&UIUtils.isEnglish(cotent)){
                                              textView.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      tanslatedata(cotent);
                                                  }
                                              });
                                          }
                                          data.addView(textv);
                                      }
                                  }else{
                                      View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                                      TextView textView = textv.findViewById(R.id.text);
                                      textView.setText(translateResult);
                                      data.addView(textv);
                                  }
                                  translate_linyout.addView(translate_requestdata);
                                  if(!TextUtils.isEmpty(requst_data.image)){
                                      translate_iamge.setVisibility(View.VISIBLE);
                                      requst_dataimage = requst_data.image;
                                      UIUtils.loadImageView(mContext,requst_data.image,translate_iamge);
                                  }
                              }
                          }
                        }else{
                            ToastUtils.makeText("本地未离线当前词典");
                        }
                    }
                }
            }
        });

        translate_requestdata.findViewById(R.id.bottom_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedBackActivity.startactivity(mContext);
            }
        });

        momessage_view.findViewById(R.id.fanhui_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedBackActivity.startactivity(mContext);
            }
        });

        input_editext_titl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shouc_image.setImageResource(R.mipmap.shoucang3);
                isshouc = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                   String text = input_editext_titl.getText().toString().trim();
                   if(TextUtils.isEmpty(text)){
                       input_text.setText("");

                       shared_image.setVisibility(View.INVISIBLE);
                       shouc_image.setVisibility(View.INVISIBLE);

                   }else{
                       input_text.setText(text);
                       shared_image.setVisibility(View.VISIBLE);
                       shouc_image.setVisibility(View.VISIBLE);
                   }
            }
        });

        title_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title_btn.getText().equals(mContext.getResources().getString(R.string.translate_text_qued))){

                    String inputString = input_editext_titl.getText().toString().trim();
                    if(choicecd!=null){
                        input_text.setText(inputString);
//                        translatecontent(inputString,clickindex,choicecd.id);
                        if(!TextUtils.isEmpty(inputString)&&choicecd.id>0) {
                            translatecontent(inputString,clickindex,choicecd.id);
                        }else if(!TextUtils.isEmpty(inputString)){

                            translateforniujin(inputString,choicecd.counts);

                        }
                    }

                }else{
                    EventBus.getDefault().post(new UpdateMainIndex(0));
                    return;
                }
            }
        });
        if(BaseActivity.getuser().isMember==0) {
            getbannerdata();
        }
        input_editext_titl.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        input_editext_titl.setImeOptions(EditorInfo.IME_ACTION_GO);
        input_editext_titl.setOnEditorActionListener(mlister);
        getlanguagelist();
    }


    TextView.OnEditorActionListener mlister = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            new LogUntil(mContext,TAG,actionId+"");
            switch(actionId){
                case EditorInfo.IME_ACTION_GO:

                    String inputString = input_editext_titl.getText().toString().trim();
                    try {
                        input_text.setText(inputString);
                        if(translatetype) {

//                        translatecontent(inputString,clickindex,choicecd.id);
                            if (!TextUtils.isEmpty(inputString) && choicecd.id > 0) {
                                translatecontent(inputString, clickindex, choicecd.id);
                            } else if (!TextUtils.isEmpty(inputString)) {

                                translateforniujin(inputString, choicecd.counts);

                            }
                        }else{
                            TranslateBean returndata =   offlinetranlate(input_editext_titl.getText().toString().trim());
                            if(returndata!=null){
                                translate_linyout.removeAllViews();
                                data.removeAllViews();
                                String translateResult = returndata.contentTwo;
                                if(!TextUtils.isEmpty(translateResult)) {
                                    if (translateResult.indexOf(";") > 0) {
                                        String[] line = translateResult.split(";");
                                        for (int i = 0; i < line.length; i++) {
                                            View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                                            TextView textView = textv.findViewById(R.id.text);
                                            textView.setText(line[i]+"\n");
                                            final String cotent = line[i];
                                            if(!TextUtils.isEmpty(cotent)&&UIUtils.isEnglish(cotent)){
                                                textView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        tanslatedata(cotent);
                                                    }
                                                });
                                            }
                                            data.addView(textv);
                                        }
                                    }else{
                                        View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                                        TextView textView = textv.findViewById(R.id.text);
                                        textView.setText(translateResult);
                                        data.addView(textv);
                                    }
                                    translate_linyout.addView(translate_requestdata);
                                    if(!TextUtils.isEmpty(requst_data.image)){
                                        translate_iamge.setVisibility(View.VISIBLE);
                                        requst_dataimage = requst_data.image;
                                        UIUtils.loadImageView(mContext,requst_data.image,translate_iamge);
                                    }
                                }
                            }



                        }
                    }catch (Exception e){

                    }

                    break;
            }
            return true;
        }
    };

    private void getdictionary(int type){
        Observable observable =
                ApiUtils.getApi().getAllDictionary(BaseActivity.getuser().id+"",BaseActivity.getLanguetype(mContext),type)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<List<DictionaryBean>>(mContext) {
            @Override
            protected void _onNext(StatusCode<List<DictionaryBean>> stringStatusCode) {
                new LogUntil(mContext,TAG+"getAllDictionary",new Gson().toJson(stringStatusCode));
                 LoadingDialogUtils.closeDialog(mLoadingDialog);
                listdata.clear();
                if(stringStatusCode.getData()!=null&&stringStatusCode.getData().size()>0){

                    choicecd = stringStatusCode.getData().get(0);
                    listdata.addAll(stringStatusCode.getData());
                    dictionaryId = listdata.get(0).id;
                }
                if(haveenglish){
                    DictionaryBean data1 = new DictionaryBean();
                    data1.name = getResources().getString(R.string.cidian_niujin);
                    data1.id = -1;
                    data1.counts = 1;

                    DictionaryBean data2 = new DictionaryBean();
                    data2.name = getResources().getString(R.string.cidian_weishi);
                    data2.id = -2;
                    data2.counts = 0;
                    listdata.add(data1);
                    listdata.add(data2);
                }
                if(listdata.size()>0){
                    filename = FileUtils.getSDRoot()+"/translate/"+listdata.get(0).id+".json";
                }
                translatebottomadpater.notifyDataSetChanged();
                translatebottomadpater.updatecliclk();
                if(dictionaryId!=0){
                    translatebottomadpater.setclick(dictionaryId);
                }
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }

    private void getnewcidian(){

    }


    private void getbannerdata(){

        Observable observable =
                ApiUtils.getApi().getinformationlist(BaseActivity.getLanguetype(mContext),1,Contans.cow,BaseActivity.getuser().id+"",1,"2")
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
                        mConvenientBanner.setPages(new CBViewHolderCreator<MImageViewHolder>() {
                            @Override
                            public MImageViewHolder createHolder() {
                                return new MImageViewHolder();
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


    /**
     * 翻译单词
     * @param contengt
     * @param type
     */

    private TranslateBean requst_data;
    private void translatecontent(final String contengt, int type, int daviceid){
        getcollectionstats(contengt);
        Observable observable =
                ApiUtils.getApi().translateconttent(BaseActivity.getuser().id+"",BaseActivity.getLanguetype(mContext),type,contengt,daviceid)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                    }
//                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<TranslateBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<TranslateBean> stringStatusCode) {
                new LogUntil(mContext,TAG+"translateconttent",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode!=null&&stringStatusCode.getData()!=null){
                    if(requst_data!=null&&requst_data.id==stringStatusCode.getData().id){
                        if(isshouc){
                            shouc_image.setImageResource(R.mipmap.shoucang2);
                        }
                    }else{
                        shouc_image.setImageResource(R.mipmap.shoucang3);
                        isshouc = false;
                    }
                    requst_data = stringStatusCode.getData();
                    translate_linyout.removeAllViews();
                    data.removeAllViews();
                    String translateResult = stringStatusCode.getData().translateResult;
                    if(!TextUtils.isEmpty(translateResult)) {
                        if (translateResult.indexOf(";") > 0) {
                            String[] line = translateResult.split(";");
                            for (int i = 0; i < line.length; i++) {
                               View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                               TextView textView = textv.findViewById(R.id.text);
                               textView.setText(line[i]+"\n");
                               final String cotent = line[i];
                               if(!TextUtils.isEmpty(cotent)&&UIUtils.isEnglish(cotent)){
                                   textView.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           tanslatedata(cotent);
                                       }
                                   });
                               }
                               data.addView(textv);
                            }
                        }else{
                            View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                            TextView textView = textv.findViewById(R.id.text);
                            textView.setText(translateResult);
                            data.addView(textv);
                        }
                    }
                    translate_linyout.addView(translate_requestdata);
                    if(!TextUtils.isEmpty(requst_data.image)){
                        translate_iamge.setVisibility(View.VISIBLE);
                        requst_dataimage = requst_data.image;
                        UIUtils.loadImageView(mContext,requst_data.image,translate_iamge);
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                 translate_linyout.removeAllViews();
                 TextView text1 = momessage_view.findViewById(R.id.text);
                 if(message.equals("网络不可用")){
                     TranslateBean returndata =   offlinetranlate(input_editext_titl.getText().toString().trim());
                     if(returndata!=null){
                         translate_linyout.removeAllViews();
                         data.removeAllViews();
                         String translateResult = returndata.contentTwo;
                         if(!TextUtils.isEmpty(translateResult)) {
                             if (translateResult.indexOf(";") > 0) {
                                 String[] line = translateResult.split(";");
                                 for (int i = 0; i < line.length; i++) {
                                     View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                                     TextView textView = textv.findViewById(R.id.text);
                                     textView.setText(line[i]+"\n");
                                     final String cotent = line[i];
                                     if(!TextUtils.isEmpty(cotent)&&UIUtils.isEnglish(cotent)){
                                         textView.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 tanslatedata(cotent);
                                             }
                                         });
                                     }
                                     data.addView(textv);
                                 }
                             }else{
                                 View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                                 TextView textView = textv.findViewById(R.id.text);
                                 textView.setText(translateResult);
                                 data.addView(textv);
                             }
                             translate_linyout.addView(translate_requestdata);
//                             if(!TextUtils.isEmpty(requst_data.image)){
//                                 translate_iamge.setVisibility(View.VISIBLE);
//                                 requst_dataimage = requst_data.image;
//                                 UIUtils.loadImageView(mContext,requst_data.image,translate_iamge);
//                             }
                         }
                     }
                 }else {
                     text1.setText(message);
                 }
                translate_linyout.addView(momessage_view);
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
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

    @Override
    public void onPause() {
        super.onPause();
        mConvenientBanner.stopTurning();
    }
    @OnClick(R.id.shouc_image)
    public void shouc(){
        if(requst_data!=null&&!TextUtils.isEmpty(requst_data.translateResult)) {
            if (!isshouc) {
                if (requst_data != null) {
                    collectionDictionary(0);
                }
            } else {
                  collectionDictionary(1);
            }
        }

    }

    private void collectionDictionary(final int type){
        Observable observable =
                ApiUtils.getApi().collectionDictionary(BaseActivity.getLanguetype(mContext),
                        BaseActivity.getuser().id+"",
                        clickindex,
                        input_editext_titl.getText().toString().trim(),
                        requst_data.translateResult,
                        choicecd.id+"",
                        "1",
                        type)
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
                isshouc = !isshouc;
                if(type==0) {
                    shouc_image.setImageResource(R.mipmap.shoucang2);
                }else{
                    shouc_image.setImageResource(R.mipmap.shoucang3);
                }

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }
    private int dictionaryId  = 0;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setdata(CollectionListbean mdata){
        input_editext_titl.setText(mdata.content);
        translate_linyout.removeAllViews();
        input_text.setText(mdata.content);
        madpater.setclickde(mdata.type);
        clickindex = mdata.type;
        haveenglish = false;
        if(madpater.getclicdname(mdata.type).indexOf("英")>=0){
            haveenglish = true;
        }
        getdictionary(clickindex);
        translate_linyout.removeAllViews();
        data.removeAllViews();
        isshouc = true;

        shouc_image.setImageResource(R.mipmap.shoucang2);
        String translateResult = mdata.translateContent;
        if(!TextUtils.isEmpty(translateResult)) {
            if (translateResult.indexOf(";") > 0) {
                String[] line = translateResult.split(";");
                for (int i = 0; i < line.length; i++) {
                    View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                    TextView textView = textv.findViewById(R.id.text);
                    textView.setText(line[i]);
                    data.addView(textv);
                }
            }else{
                View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                TextView textView = textv.findViewById(R.id.text);
                textView.setText(translateResult);
                data.addView(textv);
            }
        }
        translate_linyout.addView(translate_requestdata);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(TranslateFagment2.this);
    }
    private String requst_dataimage = "";
    @OnClick(R.id.translate_iamge)
    public void lookimage(){
        if(!TextUtils.isEmpty(requst_dataimage))
        Look_imageActivity.startactivity(mContext,requst_dataimage);
    }


    void getlanguagelist(){

        Observable observable =
                ApiUtils.getApi().getDicitionaryLanguageList()
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LanguageBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<LanguageBean> stringStatusCode) {
                new LogUntil(mContext,TAG+"zixunmessage",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode!=null&&stringStatusCode.getData()!=null&&stringStatusCode.getData().list.size()>0){
                    languagelist.clear();
                    languagelist.addAll(stringStatusCode.getData().list);
                    madpater.notifyDataSetChanged();
                    madpater.steallclickfalse();
                    madpater.setclickindex(0);
                    getdictionary(languagelist.get(0).id);
                }

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);

    }


    @OnClick(R.id.shared_image)
    public void sharedmessage(){
        UMImage image = new UMImage(mContext, R.mipmap.logo);//资源文件
        UMWeb  web = new UMWeb(Api.Shared_LODURL);
        web.setTitle(getResources().getString(R.string.app_name));//标题
        web.setThumb(image);  //缩略图
        web.setDescription(getResources().getString(R.string.share_text_content));//描述
        new ShareAction(TranslateFagment2.this.getActivity())
                .withMedia(web)
                .setCallback(shareListener)
                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                .open();
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


    private String mdata = "";
    Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.arg1==102){
                mdata = msg.obj+"";
                if(mdata.indexOf("[ERR]")>=0){
                    showdialog(mdata);
                }else{
                    showdialog(mdata);
                }
            }else if(msg.arg1==103){
                ThreeTranslateBean datasj = (ThreeTranslateBean)msg.obj;
                translate_linyout.removeAllViews();
                data.removeAllViews();
                requst_data = new TranslateBean();
                String mdata = "";
                if(!TextUtils.isEmpty(datasj.from)) {
                    title.setVisibility(View.VISIBLE);
                    title.setText(datasj.from);
                }else{
                    title.setVisibility(View.GONE);
                }

                if(!TextUtils.isEmpty(datasj.date)) {
                    datadata.setVisibility(View.VISIBLE);
                    datadata.setText(datasj.date);
                }else{
                    datadata.setVisibility(View.GONE);
                }
                for (int i = 0; i < datasj.sentenceDTOList.size(); i++) {
                    View textv = UIUtils.inflate(mContext, R.layout.layout_text);
                    TextView textView = textv.findViewById(R.id.text);
                    LinearLayout fmlinlayout = textv.findViewById(R.id.linlayout);
//                          mlinlayout.removeAllViews();
                    fmlinlayout.setVisibility(View.GONE);
                    if(!TextUtils.isEmpty(datasj.sentenceDTOList.get(i).definition)){
//                              textView.setText( stringStatusCode.getData().sentenceDTOList.get(i).definition+"\n");
                        final String cotent = datasj.sentenceDTOList.get(i).definition;
                        layoutContent(textView,cotent,i+1);
                        mdata = mdata+datasj.sentenceDTOList.get(i).definition+";";

                        if(datasj.sentenceDTOList.get(i).examples.size()>0){
                            fmlinlayout.setVisibility(View.VISIBLE);
                            fmlinlayout.removeAllViews();
                            for(int j = 0;j<datasj.sentenceDTOList.get(i).examples.size();j++){

                                View mview = UIUtils.inflate(mContext,R.layout.view_threretranslatetext);
                                TextView mtext = mview.findViewById(R.id.text);
                                mtext.setText(datasj.sentenceDTOList.get(i).examples.get(j));
                                fmlinlayout.addView(mview);
                            }
                        }

                    }else
                    if(!TextUtils.isEmpty(datasj.sentenceDTOList.get(i).content)){
//                            textView.setText( stringStatusCode.getData().sentenceDTOList.get(i).content+"\n");
                        final String cotent = datasj.sentenceDTOList.get(i).content;
                        layoutContent(textView,cotent,i+1);
                        mdata = mdata+datasj.sentenceDTOList.get(i).content+";";

                    }
                    if(!TextUtils.isEmpty(mdata)) {
                        mdata = mdata.substring(0, mdata.length() - 1);
                        requst_data.translateResult = mdata;
                    }

                    mlinlayout.addView(textv);

                }
                data.addView(threretranslaterequestdata);
                translate_linyout.addView(translate_requestdata);
            }

        }
    };

    private void tanslatedata(final String centent){




        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    okPost("en_zh-cn",centent,102);
                }catch (Exception e){
                    new LogUntil(mContext,TAG+"content",e.getMessage());
                }

            }
        }.start();


    }

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
                mhandler.sendMessage(msg);
            }
        });
//        return response.body().string();
    }
    private void showdialog(String data){
        AlertView alertView = new AlertView("快捷翻译", data, null, null, new String[]{"取消"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {


            }
        });
        alertView.show();
    }


    private void translateforniujin(String text,int type){
        getcollectionstats(text);


        Observable observable =
                ApiUtils.getApi().translateFromThree(text,BaseActivity.getLanguetype(mContext),BaseActivity.getuser().id+"",type)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<ThreeTranslateBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<ThreeTranslateBean> stringStatusCode) {
                new LogUntil(mContext,TAG+"translateFromThree",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
//                String translateResult = stringStatusCode.getData().translateResult;
                  if(stringStatusCode.getData()!=null&&stringStatusCode.getData().sentenceDTOList.size()>0) {

                      Message msg = new Message();
                      msg.arg1 = 103;
                      msg.obj =stringStatusCode.getData();
                      mhandler.sendMessage(msg);

                  }else{
                      translate_linyout.addView(momessage_view);
                  }

            }

            @Override
            protected void _onError(String message) {
                if(message.equals("网络不可用")){
                    TranslateBean returndata =   offlinetranlate(input_editext_titl.getText().toString().trim());
                    if(returndata!=null){
                        translate_linyout.removeAllViews();
                        data.removeAllViews();
                        String translateResult = returndata.contentTwo;
                        if(!TextUtils.isEmpty(translateResult)) {
                            if (translateResult.indexOf(";") > 0) {
                                String[] line = translateResult.split(";");
                                for (int i = 0; i < line.length; i++) {
                                    View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                                    TextView textView = textv.findViewById(R.id.text);
                                    textView.setText(line[i]+"\n");
                                    final String cotent = line[i];
                                    if(!TextUtils.isEmpty(cotent)&&UIUtils.isEnglish(cotent)){
                                        textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                tanslatedata(cotent);
                                            }
                                        });
                                    }
                                    data.addView(textv);
                                }
                            }else{
                                View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                                TextView textView = textv.findViewById(R.id.text);
                                textView.setText(translateResult);
                                data.addView(textv);
                            }
                            translate_linyout.addView(translate_requestdata);
                            if(!TextUtils.isEmpty(requst_data.image)){
                                translate_iamge.setVisibility(View.VISIBLE);
                                requst_dataimage = requst_data.image;
                                UIUtils.loadImageView(mContext,requst_data.image,translate_iamge);
                            }
                        }
                    }
                }
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uodateuser( UpdateUserEvent uodate){
        mConvenientBanner.setVisibility(View.GONE);
    }

    private boolean translatetype = true;

    /**
     * 接受是否有网络状态
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netupdate(NetEvent event){
        if(event.type>=0){
            translatetype = true;
        }else{
            translatetype = false;
        }
    }



    private void layoutContent(TextView textView, String content,int mindex) {
        if (!content.contains(" ")) {
            textView.setText(content);
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(content);

        int index = 0;
//        Today's weather
        String datam = content.trim()
                .replace("'","")
                .replace(" ","")
                .replace("<","")
                .replace(">","")
                .replace("-","");
        if(UIUtils.isEnglish((datam))) {
            if(content.indexOf(" ")>=0||content.indexOf(",")>=0){
                String[] contentlist = null;
                if(content.indexOf(" ")>=0){
                    contentlist  = content.split(" ");
                }
                if(content.indexOf(",")>=0){
                    contentlist  = content.split(" ");
                }

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
                    },index);
                    builder.setSpan(clickableSpan, atIndex, sIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                }
            }
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(builder);
    }

    private boolean havecidian(String name){
        if(filelist!=null&&filelist.size()>0) {
            for (String data : filelist) {
                if (data.equals(name)) {
                    return true;
                }
            }
            return false;
        }return false;
    }

    private TranslateBean offlinetranlate(String inputtext){
        new LogUntil(mContext,TAG,"缓存地址"+filename);
        File cidianfile = new File(filename);
        if(!cidianfile.exists()){

            ToastUtils.makeText("本地未离线当前词典");
            return null;
        }

        if(TextUtils.isEmpty(inputtext)){
            return null;
        }

        String jsondata = UIUtils.getJson(filename);
        if (!TextUtils.isEmpty(jsondata)) {
            List<TranslateBean> mlistdata = UIUtils.jsonToArrayList(jsondata,TranslateBean.class);
            for(TranslateBean data:mlistdata){
                if(data.contentOne.equals(inputtext)){
                    return data;
                }
            }
        }

        return null;
    }


    private void getcollectionstats(String content){
        Observable observable =
                ApiUtils.getApi().getCollectionStatus(BaseActivity.getLanguetype(mContext),BaseActivity.getuser().id+"",content,choicecd.id,1)
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

                        shouc_image.setImageResource(R.mipmap.shoucang2);
                        isshouc = true;


                    }else{
                        shouc_image.setImageResource(R.mipmap.shoucang3);
                        isshouc = false;
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void choicelangevage(LanuageListBean data){
        clickindex = data.id;
        PreferencesUtils.getInstance().putInt(Contans.PERFICE_TRANSLATE_TITLE_CLICK,clickindex);
        haveenglish = false;
        if(data.name.indexOf("英")>=0){
            haveenglish = true;
        }
        madpater.setclickde(data.id);
        dictionaryId = 0;
        getdictionary(clickindex);
        translate_linyout.removeAllViews();
    }

}
