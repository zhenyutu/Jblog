package cn.tzy.Jblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by tuzhenyu on 17-8-13.
 * @author tuzhenyu
 */
@Controller
public class IndexController {
    @RequestMapping(path = {"/","/index"})
    public String index(){
        return "index";
    }
}
