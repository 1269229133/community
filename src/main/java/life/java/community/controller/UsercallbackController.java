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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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



    @GetMapping("callback")
    //获得code 和 state
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) {
        AccesstokenDto accesstokenDto = new AccesstokenDto();//创建AccesstokenDto对象获得token信息
        accesstokenDto.setClient_id(ClientId);
        accesstokenDto.setClient_secret(ClientSecret);
        accesstokenDto.setCode(code);
        accesstokenDto.setRedirect_uri(ClientUri);
        accesstokenDto.setState(state);
        String assessToken = githubProvide.getAssessToken(accesstokenDto);
        GithubUser GirhubUser = githubProvide.getUser(assessToken);

        if (GirhubUser != null) {
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(GirhubUser.getName());
            user.setAccountId(String.valueOf(GirhubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登陆成功就写入cookie和session
            request.getSession().setAttribute("user", GirhubUser);
            return "redirect:/";
        } else {
            //失败返回
            return "redirect:/";
        }
    }
}
