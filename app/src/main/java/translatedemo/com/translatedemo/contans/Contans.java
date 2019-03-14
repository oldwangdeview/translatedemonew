package translatedemo.com.translatedemo.contans;

/**
 * Author wangshifu
 * Dest  常量
 */

public interface Contans {
    /**权限请求码*/
    int  PERMISSION_REQUST_COND=2001;
    int GALLERY_REQUEST_CODE = 500;
    int CAMERA_REQUEST_CODE = 600;
    int CODE_RESULT_REQUEST = 0xa2;

    /**头像地址*/
     String headPicUrl="http://img.scshushe.com/";
    /**密码验证正则*/
    String PASSWORD_REGEX="^[0-9A-Za-z]{6,20}$";
    /**手机号正则*/
    String PHONE_REGEX="^[1][3,4,5,7,8,9][0-9]{9}$";
        String TANSLATEFORKRJ = "strideovertranslate";
     // 加载默认的状态
      int STATE_UNLOADED = 1;
     // 加载的状态
    int STATE_LOADING = 2;
     // 加载失败的状态
     int STATE_ERROR = 3;
     // 加载空的状态
     int STATE_EMPTY = 4;
     // 加载成功的状态
    int STATE_SUCCEED = 5;

    /**下架*/
     int STATE_SOLDOUT=6;
    /**传递intent数据*/
    String INTENT_DATA="intent_data";
    String INTENT_TYPE = "intent_type";
    int rows=20;
    /**楼盘预售*/
    int  HOUSE_PRESELL_TYPE=1;
    /**楼盘在售*/
    int HOUSE_ONSALE_TYPE=2;
    /**楼盘售罄*/
    int HOUSE_SELLOUT_TYPE=3;

    String PERFICE_TRANSLATE_TITLE_CLICK = "translate_click_index";

    String HOUSE_SOLD_OUT="该楼盘已下架";
    String SHARE_HOUSE_SOLD_OUT="该样板间已下架";
    String GOODS_SOLD_OUT="该商品已下架";
    String STORE_SOLD_OUT="该店铺已下架";
    String INPUT_HISTORY = "inputhistory";

    /**AESkey*/
    String AESKey="scmsw76449880330";
    int cow = 20;

//    boolean banertype = f

    
}
