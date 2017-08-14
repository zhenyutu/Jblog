package cn.tzy.Jblog.dao;

import cn.tzy.Jblog.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by tuzhenyu on 17-7-19.
 * @author tuzhenyu
 */
@Mapper
public interface UserDao {
    String TABLE_NAEM = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ,role ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAEM,"(",INSERT_FIELDS,") values (#{name},#{password},#{salt},#{headUrl},#{role})"})
    public void insertUser(User user);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAEM,"where id=#{id}"})
    public User seletById(int id);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAEM,"where name=#{name}"})
    public User seletByName(@Param("name") String name);

    @Delete({"delete from",TABLE_NAEM,"where id=#{id}"})
    public void deleteById(int id);
}
