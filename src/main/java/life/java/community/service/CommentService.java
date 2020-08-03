package life.java.community.service;

import life.java.community.dto.CommentDto;
import life.java.community.mapper.CommentMapper;
import life.java.community.mapper.QuestionMapper;
import life.java.community.mapper.UserMapper;
import life.java.community.model.*;
import life.java.community.enums.CommetTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getType() == CommetTypeEnum.COMMENT.getType()) {
            //回复评论
            commentMapper.insertSelective(comment);
        } else {
            //回复问题
            //查询问题id的回复数
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria()
                    .andParentIdEqualTo(comment.getParentId());
            Integer l = (int) commentMapper.countByExample(commentExample);
            //查询获取问题id相关信息
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            Question updateQuestion = new Question();
            //把问题回复数+1
            updateQuestion.setCommentCount(l + 1);
            QuestionExample questionExample = new QuestionExample();
            //获得回复数更新条件
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            //回复数更新
            questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            //写入回复数据库
            commentMapper.insertSelective(comment);
        }
    }

    //评论
    public List<CommentDto> listByQuestionId(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(CommetTypeEnum.QUESTION.getType());
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //因为评论是由多用户评论，避免重复获得多次评论者的id，使用列查找                     //.distinct()
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人转化为map，因为要多用户获取头像和用户名
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        //key和value
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment 为commentDto
        List<CommentDto> commentDtos = comments.stream().map(comment -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment,commentDto);
            commentDto.setUser(userMap.get(comment.getCommentator()));
            return commentDto;
        }).collect(Collectors.toList());
        return commentDtos;
    }
}
