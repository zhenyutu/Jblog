package cn.tzy.Jblog.controller;

import cn.tzy.Jblog.model.Article;
import cn.tzy.Jblog.model.ViewObject;
import cn.tzy.Jblog.service.ArticleService;
import com.sun.javafx.sg.prism.NGShape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by tuzhenyu on 17-8-13.
 * @author tuzhenyu
 */
@Controller
public class IndexController {
    @Autowired
    private ArticleService articleService;

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

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}
