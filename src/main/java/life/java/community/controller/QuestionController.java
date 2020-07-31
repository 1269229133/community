package life.java.community.controller;

import life.java.community.mapper.QuestionMapper;
import life.java.community.mapper.UserMapper;
import life.java.community.modle.Question;
import life.java.community.modle.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class QuestionController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/question")
    public String question(HttpServletRequest request) {
        //获取cookie并判断实现刷新页面海存在登陆
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    //如果有就把user放入session里面。
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        return "question";
    }

    @PostMapping("/question")
    //提交提问表单
    public String doQuestion(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request, Model model) {

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

        //初始化user，然后获得cookie和user，用于判断是否登陆
        User user=null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    //如果有就把user放入session里面。
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }

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
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        //写入数据库
        questionMapper.create(question);
        return "redirect:/";
    }
}
