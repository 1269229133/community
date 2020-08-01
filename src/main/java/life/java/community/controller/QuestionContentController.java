package life.java.community.controller;

import life.java.community.dto.QuestionDTO;
import life.java.community.mapper.QuestionMapper;
import life.java.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//问题内容控制层
@Controller
public class QuestionContentController {
    @Autowired
     private QuestionService questionService;

    @GetMapping("/questioncontent/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){
        QuestionDTO questionDTO= questionService.getById(id);
        model.addAttribute("questioncontent",questionDTO);
        return "questioncontent";
    }
}
