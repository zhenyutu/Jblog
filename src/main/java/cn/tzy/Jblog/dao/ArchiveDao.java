package cn.tzy.Jblog.dao;

import cn.tzy.Jblog.model.Archive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by tuzhenyu on 17-8-18.
 * @author tuzhenyu
 */
@Mapper
public interface ArchiveDao {

    @Select("SELECT article.id AS article_id,article.title AS article_title, YEAR(article.created_date) AS year,MONTH(article.created_date) AS month " +
            "FROM article GROUP BY YEAR(article.created_date), MONTH(article.created_date),article.id,article.title order by article_id desc")
    public List<Archive> seletArticleGroupByTime();

}
