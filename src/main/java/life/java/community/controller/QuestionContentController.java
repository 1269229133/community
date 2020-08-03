package life.java.community.controller;

import life.java.community.dto.CommentCreateDto;
import life.java.community.dto.CommentDto;
import life.java.community.dto.QuestionDTO;
import life.java.community.service.CommentService;
import life.java.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//问题内容控制层
@Controller
public class QuestionContentController {
    @Autowired
     private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/questioncontent/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model){
        QuestionDTO questionDTO= questionService.getById(id);
        List<CommentDto> comments=commentService.listByQuestionId(id);

        //增加阅读数
        questionService.inView(id);
        model.addAttribute("questioncontent",questionDTO);
        model.addAttribute("comments",comments);

        return "questioncontent";
    }
}
