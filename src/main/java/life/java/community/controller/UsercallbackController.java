package life.java.community.controller;

import life.java.community.dto.AccesstokenDto;
import life.java.community.dto.GithubUser;
import life.java.community.mapper.UserMapper;
import life.java.community.modle.User;
import life.java.community.provide.GithubProvide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class UsercallbackController {

    @Autowired
    private GithubProvide githubProvide;//定义调用GithubProvide类的变量
    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")//读取application的配置
    private String ClientId;
    @Value("${github.client.secret}")
    private String ClientSecret;
    @Value("${github.client.uri}")
    private String ClientUri;

    //获取github信息控制层

    @GetMapping("callback")
    //获得code 和 state
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
        AccesstokenDto accesstokenDto = new AccesstokenDto();//创建AccesstokenDto对象写入token信息
        accesstokenDto.setClient_id(ClientId);
        accesstokenDto.setClient_secret(ClientSecret);
        accesstokenDto.setCode(code);
        accesstokenDto.setRedirect_uri(ClientUri);
        accesstokenDto.setState(state);
        String assessToken = githubProvide.getAssessToken(accesstokenDto);
        //使用github登陆，然后写入信息到GithubUser
        GithubUser GithubUser = githubProvide.getUser(assessToken);
        //判断登陆是否成功
        if (GithubUser != null) {
            //获取github用户信息
            User user = new User();
            //生成token
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(GithubUser.getName());
            user.setAccountId(String.valueOf(GithubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(GithubUser.getAvatarUrl());
            //把信息存入数据库
            userMapper.insert(user);
            //把token放入response的cookie里
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        } else {
            //失败返回
            return "redirect:/";
        }
    }
}
