package life.java.community.controller;

import life.java.community.dto.AccesstokenDto;
import life.java.community.dto.GithubUser;
import life.java.community.provide.GithubProvide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsercallbackController {

    @Autowired
    private GithubProvide githubProvide;//定义调用GithubProvide类的变量

    @Value("${github.client.id}")//读取application的配置
    private String ClientId;
    @Value("${github.client.secret}")
    private String ClientSecret;
    @Value("${github.client.uri}")
    private String ClientUri;

    @GetMapping("callback")
    //获得code 和 state
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name = "state") String state){
        AccesstokenDto accesstokenDto = new AccesstokenDto();//创建AccesstokenDto对象获得token信息
        accesstokenDto.setClient_id(ClientId);
        accesstokenDto.setClient_secret(ClientSecret);
        accesstokenDto.setCode(code);
        accesstokenDto.setRedirect_uri(ClientUri);
        accesstokenDto.setState(state);
        String assessToken = githubProvide.getAssessToken(accesstokenDto);
        GithubUser user = githubProvide.getUser(assessToken);
        System.out.println(user.getName());
        return "index";
    }
}
