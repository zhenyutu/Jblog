package cn.tzy.Jblog.service;

import cn.tzy.Jblog.dao.ArticleTagDao;
import cn.tzy.Jblog.dao.TagDao;
import cn.tzy.Jblog.model.ArticleTag;
import cn.tzy.Jblog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by tuzhenyu on 17-8-14.
 * @author tuzhenyu
 */
@Service
public class TagService {
    @Autowired
    private TagDao tagDao;

    @Autowired
    private ArticleTagDao articleTagDao;

    public Tag selectByName(String name){
        return tagDao.selectByName(name);
    }

    public int addTag(Tag tag){
        return tagDao.insertTag(tag)>0?tag.getId():0;
    }

    public int addArticleTag(ArticleTag articleTag){
        return articleTagDao.insertArticleTag(articleTag);
    }

    public void updateCount(int tagId,int count){
        tagDao.updateCount(tagId,count);
    }
}
