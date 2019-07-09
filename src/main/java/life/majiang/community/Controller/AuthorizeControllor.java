package life.majiang.community.Controller;


import life.majiang.community.Provide.GithubProvide;
import life.majiang.community.dto.AccesstokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
public class AuthorizeControllor {
    @Autowired
    private GithubProvide githubProvide;

    @Value("${github.client.id}")
    private String Client;

    @Value("${github.client.secret}")
    private String ClientSecret;

    @Value("${github.redirect.uri}")
    private String RedirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id(Client);
        accesstokenDTO.setClient_secret(ClientSecret);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(RedirectUri);
        accesstokenDTO.setState(state);
        String acessToken = githubProvide.getAcessToken(accesstokenDTO);
        GithubUser gitUser = githubProvide.getUser(acessToken);
        if (gitUser!=null){
            //登入成功，写cookie和session
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitUser.getName());
            user.setAccountId(String.valueOf(gitUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModefied(user.getGmtCreate());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));

            request.getSession().setAttribute("user",gitUser);
            return "redirect:/";
        }else {
            //登入失败，重新登入
            return "redirect:/";
        }
    }
}
