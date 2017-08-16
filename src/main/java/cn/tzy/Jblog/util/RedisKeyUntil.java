package cn.tzy.Jblog.util;

/**
 * Created by tuzhenyu on 17-7-25.
 * @author tuzhenyu
 */
public class RedisKeyUntil {
    private static String SPLITE = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_CATEGORY = "CATEGORY";
    private static String BIZ_CLICK = "CLICK";

    public static String getCategoryKey(String category){
        return BIZ_CATEGORY+SPLITE+category+SPLITE+"COUNT";
    }

    public static String getClickCountKey(String url){
        return BIZ_CLICK+SPLITE+url+SPLITE+"COUNT";
    }


}
