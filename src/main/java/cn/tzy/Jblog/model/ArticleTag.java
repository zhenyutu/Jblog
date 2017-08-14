package cn.tzy.Jblog.model;

/**
 * Created by tuzhenyu on 17-8-14.
 * @author tuzhenyu
 */
public class ArticleTag {
    private int id;
    private int articleId;
    private int tagId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
