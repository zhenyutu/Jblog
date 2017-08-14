package cn.tzy.Jblog.controller;

import cn.tzy.Jblog.model.Article;
import cn.tzy.Jblog.model.HostHolder;
import cn.tzy.Jblog.model.User;
import cn.tzy.Jblog.model.ViewObject;
import cn.tzy.Jblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by tuzhenyu on 17-8-14.
 * @author tuzhenyu
 */
@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/page/{pageId}")
    public String article(Model model, @PathVariable("pageId")int pageId){
        List<Article> articles = articleService.getLatestArticles((pageId-1)*4,4);
        ViewObject pagination = new ViewObject();
        int count = articleService.getArticleCount();

        pagination.set("current",pageId);
        pagination.set("nextPage",pageId+1);
        pagination.set("prePage",pageId-1);
        pagination.set("lastPage",count/4+1);

        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
        }

        model.addAttribute("articles",articles);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
