package life.java.community.controller;

import life.java.community.dto.CommentCreateDto;
import life.java.community.dto.ResultDto;
import life.java.community.model.Comment;
import life.java.community.model.User;
import life.java.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    //用于把对象序列化成json，发至前端
    @ResponseBody
    @RequestMapping(value = "/comment" , method = RequestMethod.POST)
    //@RequestBody接收json数据变成对象
    public Object post(@RequestBody CommentCreateDto commentCreateDto,
                       HttpServletRequest request){
        //回复问题
        User user =(User) request.getSession().getAttribute("user");
        if(user==null){
            return ResultDto.errorOf(500,"用户未登录");
        }
        if(commentCreateDto==null||commentCreateDto.getContent()==null||commentCreateDto.getContent()==""){
            return ResultDto.errorOf(500,"内容不能为空");
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDto.getParentId());
        comment.setContent(commentCreateDto.getContent());
        comment.setType(commentCreateDto.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDto.ok();
    }
}
