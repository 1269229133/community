package life.java.community.controller;

import life.java.community.dto.PaginationDto;
import life.java.community.dto.QuestionDTO;
import life.java.community.mapper.UserMapper;
import life.java.community.modle.User;
import life.java.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
//主页控制层
@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    //访问首页时查询cookie，拿到网页的cookie然后去数据库查
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "7") Integer size) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
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

        //使用service层实现User和Question组装/获取头像和问题信息
        PaginationDto pagination = questionService.list(page,size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
