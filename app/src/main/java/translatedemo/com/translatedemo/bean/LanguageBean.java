package translatedemo.com.translatedemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * "list": [
 *                        {
 * 				"id": 13427,
 * 				"name": "汉藏"
 *            }
 * 		],
 * 		"page": 1,
 * 		"size": 1,
 * 		"totalElements": 1,
 * 		"totalPages": 1
 */
public class LanguageBean implements Serializable {

    public Integer page;
    public  Integer size;
    public Integer totalElements;
    public Integer totalPages;
    public List<LanuageListBean> list  = new ArrayList<>();

}
