package translatedemo.com.translatedemo.activity;

import org.greenrobot.eventbus.EventBus;

import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.eventbus.OverMainactivty;
import translatedemo.com.translatedemo.eventbus.TranslateEvent;

public class TransDataActivty extends BaseActivity {

    public static final String DATA = "stringdata";
    public static final String TYPE = "inttypedata";
    public static final String REQUESTDATA = "requestdata";
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        super.initData();
       final String data = getIntent().getStringExtra(TransDataActivty.DATA);
       final  int type = getIntent().getIntExtra(TransDataActivty.TYPE,-1);
        if(type<=0){
            finish();
        }
        switch (type){
            case 1:
                EventBus.getDefault().post(new OverMainactivty());
                LoginActivity.startactivity(mcontent);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try{
                            Thread.sleep(1000);
                            EventBus.getDefault().post(new TranslateEvent(data));

                        }catch (Exception e){

                        }
                    }
                }.start();

                break;
            case 2:
                EventBus.getDefault().post(new OverMainactivty());
                LoginActivity.startactivity(mcontent);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try{
                            Thread.sleep(1000);
                            EventBus.getDefault().post(new TranslateEvent(data));

                        }catch (Exception e){

                        }
                    }
                }.start();


                break;
        }
        finish();
    }
}
