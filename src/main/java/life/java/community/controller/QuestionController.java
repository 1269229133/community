package life.java.community.controller;

import life.java.community.dto.QuestionDTO;
import life.java.community.model.Question;
import life.java.community.model.User;
import life.java.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    //修改发布
    @GetMapping("/question/{id}")
    public String edit(@PathVariable(name = "id" )Integer id,
                       Model model) {
        QuestionDTO question = questionService.getById(id);
        //返回数据到页面
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag" ,question.getTag());
        model.addAttribute("id",question.getId());
        return "question";
    }

    @GetMapping("/question")
    public String question() {
        //获取cookie并判断实现刷新页面海存在登陆
        //页面登陆判断移植至WebConfig
        return "question";
    }

    @PostMapping("/question")
    //提交提问表单
    public String doQuestion(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam("id") Integer id,
            HttpServletRequest request, Model model) {
        //页面登陆判断移植至WebConfig，由Spring Mvc的Interceptors代替
        if(title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return "question";
        }
        if(description==null||description==""){
            model.addAttribute("error","内容不能为空");
            return "question";
        }
        if(tag==null||tag==""){
            model.addAttribute("error","标签不能为空");
            return "question";
        }

        //获得cookie和user，用于判断是否登陆
        User user = (User) request.getSession().getAttribute("user");
        if(user==null)
        {
            model.addAttribute("error","未登陆");
            return "question";
        }
        //写入表单信息
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        //question.setGmtCreate(System.currentTimeMillis());
       // question.setGmtModified(question.getGmtCreate());
        question.setId(id);
        questionService.createOrUpdate(question);
        //写入数据库
        //questionMapper.create(question);
        return "redirect:/";
    }
}
