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
    private static String BIZ_ARTICLE_CLICK = "ARTICLE_CLICK";

    public static String getCategoryKey(String category){
        return BIZ_CATEGORY+SPLITE+category+SPLITE+"COUNT";
    }

    public static String getClickCountKey(String url){
        return BIZ_CLICK+SPLITE+url+SPLITE+"COUNT";
    }

    public static String getArticleClickCountKey(String url){
        return BIZ_ARTICLE_CLICK+SPLITE+url+SPLITE+"COUNT";
    }


    public static void main(String[] args) {
        String str = "前端学习笔记(3)-DOM 基础前端学";
        System.out.println(str.length());
    }
}
