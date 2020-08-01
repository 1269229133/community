package life.java.community.mapper;

import life.java.community.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {
    //插入信息
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) " +
            "values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    //查询从第几开始几条数（分页用）
    @Select("Select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, Integer size);

    //查询总数
    @Select("Select count(1) from question")
    Integer count();

    //查询自己发布的从第几开始几条（分页用）
    @Select("Select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> listByuser(Integer userId, Integer offset, Integer size);

    //查询自己总发布数
    @Select("Select count(1) from question where creator=#{userId}")
    Integer countByuser(Integer userId);

    //查询某条信息
    @Select("Select * from question where id=#{id}")
    Question getById(Integer id);

    //更新问题内容
    @Update("Update question set title = #{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    void update(Question question);
}
