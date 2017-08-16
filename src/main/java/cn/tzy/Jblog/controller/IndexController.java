package cn.tzy.Jblog.controller;

import cn.tzy.Jblog.model.*;
import cn.tzy.Jblog.service.ArticleService;
import cn.tzy.Jblog.service.JedisService;
import cn.tzy.Jblog.service.TagService;
import cn.tzy.Jblog.service.UserService;
import cn.tzy.Jblog.util.JblogUtil;
import cn.tzy.Jblog.util.RedisKeyUntil;
import com.sun.javafx.binding.StringFormatter;
import com.sun.javafx.sg.prism.NGShape;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.ObjectView;
import java.util.ArrayList;
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

    @Autowired
    private TagService tagService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private JedisService jedisService;

    @RequestMapping(path = {"/","/index"})
    public String index(Model model){
        List<ViewObject> vos = new ArrayList<>();
        List<Article> articles = articleService.getLatestArticles(0,4);
        for (Article article:articles){
            ViewObject vo = new ViewObject();
            List<Tag> tags = tagService.getTagByArticleId(article.getId());
            vo.set("article",article);
            vo.set("tags",tags);
            vos.add(vo);
        }
        model.addAttribute("vos",vos);

        List<Tag> tags = tagService.getAllTag();
        model.addAttribute("tags",tags);

        ViewObject pagination = new ViewObject();
        int count = articleService.getArticleCount();
        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
        }
        pagination.set("current",1);
        pagination.set("nextPage",2);
        pagination.set("lastPage",count/4+1);
        model.addAttribute("pagination",pagination);

        ViewObject categoryCount = new ViewObject();
        for (String category: JblogUtil.categorys){
            String categoryKey = RedisKeyUntil.getCategoryKey(category);
            String num = jedisService.get(categoryKey);
            if (num!=null)
                categoryCount.set(JblogUtil.categoryMap.get(category),num);
            else
                categoryCount.set(JblogUtil.categoryMap.get(category),0);
        }
        model.addAttribute("categoryCount",categoryCount);

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

    @RequestMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

    @RequestMapping("/create")
    public String create(Model model){
        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
        }
        return "create";
    }

}
