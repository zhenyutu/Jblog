package cn.tzy.Jblog.dao;

import cn.tzy.Jblog.model.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by tuzhenyu on 17-8-13.
 * @author tuzhenyu
 */
@Mapper
public interface ArticleDao {
    String TABLE_NAEM = " article ";
    String INSERT_FIELDS = " title, content, created_Date, user_Id, comment_Count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAEM,"(",INSERT_FIELDS,") values (#{title},#{content}," +
            "#{createdDate},#{userId},#{commentCount})"})
    int insertQuestion(Article article);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAEM,"where id=#{id}"})
    Article seletById(int id);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAEM,"order by id desc limit #{offset},#{limit}"})
    List<Article> selectLatestArticles(@Param("offset") int offset, @Param("limit") int limit);

    @Update({"update",TABLE_NAEM,"set comment_count = #{commentCount} where id = #{questionId}"})
    void updateCommentCount(@Param("questionId") int questionId,@Param("commentCount") int commentCount);

    @Delete({"delete from",TABLE_NAEM,"where id=#{id}"})
    void deleteById(int id);
}
