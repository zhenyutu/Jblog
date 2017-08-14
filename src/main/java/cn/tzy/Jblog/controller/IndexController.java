package cn.tzy.Jblog.controller;

import cn.tzy.Jblog.model.Article;
import cn.tzy.Jblog.model.ViewObject;
import cn.tzy.Jblog.service.ArticleService;
import cn.tzy.Jblog.service.UserService;
import com.sun.javafx.binding.StringFormatter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by tuzhenyu on 17-8-13.
 * @author tuzhenyu
 */
@Controller
public class IndexController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/","/index"})
    public String index(Model model){
        List<Article> articles = articleService.getLatestArticles(0,4);
        ViewObject pagination = new ViewObject();
        int count = articleService.getArticleCount();
        pagination.set("current",1);
        pagination.set("nextPage",2);
        pagination.set("lastPage",count/4+1);
        model.addAttribute("articles",articles);
        model.addAttribute("pagination",pagination);
        return "index";
    }

    @RequestMapping("/in")
    public String in(Model model,@RequestParam(value = "next",required = false)String next){
        model.addAttribute("next",next);
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model, HttpServletResponse httpResponse,
                           @RequestParam String username, @RequestParam String password
            ,@RequestParam(value = "next",required = false)String next){
        Map<String,String> map = userService.register(username,password);
        if (map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket"));
            cookie.setPath("/");
            httpResponse.addCookie(cookie);

            if (StringUtils.isNotBlank(next))
                return "redirect:"+next;
            else
                return "redirect:/";
        }else {
            model.addAttribute("msg",map.get("msg"));
            return "login";
        }
    }

    @RequestMapping("/login")
    public String login(Model model, HttpServletResponse httpResponse,
                        @RequestParam String username,@RequestParam String password,@RequestParam(value = "next",required = false)String next){
        Map<String,String> map = userService.login(username,password);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket",map.get("ticket"));
            cookie.setPath("/");
            httpResponse.addCookie(cookie);

            if (StringUtils.isNotBlank(next)){
                return "redirect:"+next;
            }

            return "redirect:/";
        }else {
            model.addAttribute("msg", map.get("msg"));
            return "login";
        }
    }
}
