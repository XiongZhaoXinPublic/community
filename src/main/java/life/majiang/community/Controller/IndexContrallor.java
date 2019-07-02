package life.majiang.community.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexContrallor {

    @GetMapping("/")
    public String index(){return  "index";}
}
