package life.java.community.service;

import life.java.community.dto.PaginationDto;
import life.java.community.dto.QuestionDTO;
import life.java.community.mapper.QuestionMapper;
import life.java.community.mapper.UserMapper;
import life.java.community.model.Question;
import life.java.community.model.QuestionExample;
import life.java.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
        Integer totalcount = (int) questionMapper.countByExample(new QuestionExample());
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
        //

        //获取Select * from question结果集
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
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

    public PaginationDto list(Long userId, Integer page, Integer size) {
        PaginationDto paginationDto = new PaginationDto();
        //获得总信息数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalcount = (int) questionMapper.countByExample(questionExample);
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
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
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
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    //创建或更新问题
    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }
        else {
            question.setGmtModified(question.getGmtCreate());
            Question updataQuestion = new Question();
            updataQuestion.setGmtModified(System.currentTimeMillis());
            updataQuestion.setTitle(question.getTitle());
            updataQuestion.setDescription(question.getDescription());
            updataQuestion.setTag(question.getTag());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(updataQuestion, example);
        }
    }

    //更新浏览数
    public void inView(Long id) {
        //查询获取问题id
        Question question = questionMapper.selectByPrimaryKey(id);
        Question updateQuestion = new Question();
        //点击问题后获取之前的数+1，写入Question
        updateQuestion.setViewCount(question.getViewCount()+1);
        QuestionExample questionExample = new QuestionExample();
        //添加id字段等于value条件
        questionExample.createCriteria()
                .andIdEqualTo(id);
        questionMapper.updateByExampleSelective(updateQuestion, questionExample);
    }
}
