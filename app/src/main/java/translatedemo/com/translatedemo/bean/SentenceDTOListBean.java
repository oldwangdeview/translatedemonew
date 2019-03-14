package translatedemo.com.translatedemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * "definition":"feeling or showing pleasure or contentment",
 *  *                 "examples":[
 *  *                     "they are happy to see me doing well",
 *  *                     "Melissa came in looking happy and excited",
 *  *                     "we're just happy that he's still alive"
 *  *                 ]
 *  *             },
 */
public class SentenceDTOListBean implements Serializable {

    public String definition;
    public List<String> examples = new ArrayList<>();
    public String content;

}
