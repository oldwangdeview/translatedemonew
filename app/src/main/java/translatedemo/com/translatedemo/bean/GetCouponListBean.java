package translatedemo.com.translatedemo.bean;

import java.io.Serializable;

/**
 * Created by oldwang on 2019/1/14 0014.
 */

public class GetCouponListBean implements Serializable{
               public int  id;
               public String name;
               public double fullPrice;
               public double reducePrice;
               public int overdue;
               public String  beginTime;
               public String endTime;
               public String createTime;
               public int count;
               public int type;
               public int day;
               public int isReceive;
               public double couponId;
}
