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

    public static String getCategoryKey(String category){
        return BIZ_CATEGORY+SPLITE+category.toUpperCase()+SPLITE+"COUNT";
    }


}
