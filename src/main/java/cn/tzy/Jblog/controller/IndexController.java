package cn.tzy.Jblog.controller;

import cn.tzy.Jblog.dao.ArticleDao;
import cn.tzy.Jblog.model.Article;
import cn.tzy.Jblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        List<Article> articles = articleService.getLatestArticles(0,10);
        if (articles==null){
            System.out.println("empty");
        }
        System.out.println("lal");
        return "index";
    }
}
