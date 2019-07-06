package life.majiang.community.Controller;


import life.majiang.community.Provide.GithubProvide;
import life.majiang.community.dto.AccesstokenDTO;
import life.majiang.community.dto.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id(Client);
        accesstokenDTO.setClient_secret(ClientSecret);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(RedirectUri);
        accesstokenDTO.setState(state);
        String acessToken = githubProvide.getAcessToken(accesstokenDTO);
        GithubUser user = githubProvide.getUser(acessToken);
        System.out.println(user.getName());
        return "index";
    }



}
