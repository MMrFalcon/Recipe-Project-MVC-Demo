package falcon.mvc.recipes.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping({"","/index","/index.html"})
    public String getIndex(){
        System.out.println("test 123 321 234234");
        return "index";
    }
}
