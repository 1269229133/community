package life.java.community.mapper;

import life.java.community.modle.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) " +
            "values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("Select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, Integer size);

    @Select("Select count(1) from question")
    Integer count();

    @Select("Select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> listByuser(Integer userId, Integer offset, Integer size);

    @Select("Select count(1) from question where creator=#{userId}")
    Integer countByuser(Integer userId);
}
