package translatedemo.com.translatedemo.bean;

import java.io.Serializable;

/**
 *  "appid":"wxcf83e4f906c7ecf8",
 *     "partnerid":"1526216041",
 *     "package":"Sign=WXPay",
 *     "noncestr":"0Lhbcr52xVmFPgGI",
 *     "timestamp":1551259418,
 *     "prepayid":"wx271723386802317e75be30371703565136",
 *     "sign":"8EA7C788D9E52A7B8DCF296530DAA4A1"
 */

public class WeChartPayben implements Serializable {
    public String appid;
    public String partnerid;
    public String noncestr;
    public String timestamp;
    public String prepayid;
    public String sign;

}
