package life.java.community.service;

import life.java.community.dto.PaginationDto;
import life.java.community.dto.QuestionDTO;
import life.java.community.mapper.QuestionMapper;
import life.java.community.mapper.UserMapper;
import life.java.community.model.Question;
import life.java.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//问题控制层
@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public PaginationDto list(Integer page, Integer size) {

        PaginationDto paginationDto = new PaginationDto();
        //获得总信息数
        Integer totalcount = questionMapper.count();
        //写入页面数信息
        paginationDto.setPagination(totalcount, page, size);
        //控制页数界限
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDto.getTotalpage()) {
            page = paginationDto.getTotalpage();
        }

        //设置传入主页的分页信息
        Integer offset = size * (page - 1);
        if (offset < 0) {
            offset = 0;
        }
        //获取Select * from question结果集
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //spring方法把questions拷贝到questionDTO里（questionDTO.set(question.get)）
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            //把user和question所需的数据传递给dtolist集合（问题内容）
            questionDTOList.add(questionDTO);
        }
        paginationDto.setQuestions(questionDTOList);
        return paginationDto;
    }

    public PaginationDto list(Integer userId, Integer page, Integer size) {
        PaginationDto paginationDto = new PaginationDto();
        //获得总信息数
        Integer totalcount = questionMapper.countByuser(userId);
        //写入页面数信息
        paginationDto.setPagination(totalcount, page, size);
        //控制页数界限
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDto.getTotalpage()) {
            page = paginationDto.getTotalpage();
        }

        //设置传入主页的分页信息
        Integer offset = size * (page - 1);
        if (offset < 0) {
            offset = 0;
        }
        //获取Select * from question结果集
        List<Question> questions = questionMapper.listByuser(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //spring方法把questions拷贝到questionDTO里（questionDTO.set(question.get)）
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            //把user和question所需的数据传递给dtolist集合（问题内容）
            questionDTOList.add(questionDTO);
        }
        paginationDto.setQuestions(questionDTOList);
        return paginationDto;
    }

    //点击内容时，跳转内容页面查询的内容查询
    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }
        else {
            question.setGmtModified(question.getGmtCreate());
            questionMapper.update(question);
        }
    }
}
