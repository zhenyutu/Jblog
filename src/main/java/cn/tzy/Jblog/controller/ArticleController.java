package cn.tzy.Jblog.controller;

import cn.tzy.Jblog.model.*;
import cn.tzy.Jblog.service.*;
import cn.tzy.Jblog.util.JblogUtil;
import cn.tzy.Jblog.util.RedisKeyUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by tuzhenyu on 17-8-14.
 * @author tuzhenyu
 */
@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private JedisService jedisService;

    @RequestMapping(path = "/page/{pageId}")
    public String article(Model model, @PathVariable("pageId")int pageId){
        List<ViewObject> vos = new ArrayList<>();
        List<Article> articles = articleService.getLatestArticles((pageId-1)*4,4);
        for (Article article:articles){
            ViewObject vo = new ViewObject();
            List<Tag> tags = tagService.getTagByArticleId(article.getId());
            String clickCount = jedisService.get(RedisKeyUntil.getClickCountKey("/article/"+article.getId()));
            if (clickCount==null)
                clickCount = "0";
            vo.set("clickCount",clickCount);
            vo.set("article",article);
            vo.set("tags",tags);
            vos.add(vo);
        }
        model.addAttribute("vos",vos);

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

        List<Tag> tags = tagService.getAllTag();
        model.addAttribute("tags",tags);
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

        ViewObject clickCount = new ViewObject();
        String currentPage = jedisService.get(RedisKeyUntil.getClickCountKey("/page/"+pageId));
        String sumPage = jedisService.get(RedisKeyUntil.getClickCountKey("SUM"));
        clickCount.set("currentPage",currentPage);
        clickCount.set("sumPage",sumPage);
        model.addAttribute("clickCount",clickCount);

        List<Article> hotArticles = new ArrayList<>();
        Set<String> set = jedisService.zrevrange("hotArticles",0,6);
        for (String str : set){
            int articleId = Integer.parseInt(str.split(":")[1]);
            Article article = articleService.getArticleById(articleId);
            hotArticles.add(article);
        }
        model.addAttribute("hotArticles",hotArticles);

        return "index";
    }

    @RequestMapping("/articleAdd")
    public String addArticle(@RequestParam("title")String title,@RequestParam("category")String category,
                             @RequestParam("tag")String tag,@RequestParam("describe")String describe,
                             @RequestParam("content")String content){
        Article article = new Article();
        article.setTitle(title);
        article.setDescribes(describe);
        article.setCreatedDate(new Date());
        article.setCommentCount(0);
        article.setContent(JblogUtil.tranfer(content));
        article.setCategory(category);
        int articleId = articleService.addArticle(article);

        String[] tags = tag.split(",");
        for (String t : tags){
            Tag tag1 = tagService.selectByName(t);
            if (tag1==null){
                Tag tag2 = new Tag();
                tag2.setName(t);
                tag2.setCount(1);
                int tagId = tagService.addTag(tag2);

                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tagId);
                articleTag.setArticleId(articleId);
                tagService.addArticleTag(articleTag);
            }else {
                tagService.updateCount(tag1.getId(),tag1.getCount()+1);

                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag1.getId());
                articleTag.setArticleId(articleId);
                tagService.addArticleTag(articleTag);
            }
        }

        String categoryKey = RedisKeyUntil.getCategoryKey(category);
        jedisService.incr(categoryKey);

        return "redirect:/";
    }

    @RequestMapping(value = "/category/{categoryName}",method = RequestMethod.GET)
    public String category(Model model, @PathVariable("categoryName")String categoryName, @RequestParam("pageId")int pageId){
        List<Article> articles = articleService.getArticlesByCategory(categoryName,(pageId-1)*4,4);
        List<ViewObject> vos = new ArrayList<>();
        for (Article article:articles){
            ViewObject vo = new ViewObject();
            List<Tag> tags = tagService.getTagByArticleId(article.getId());
            String clickCount = jedisService.get(RedisKeyUntil.getClickCountKey("/article/"+article.getId()));
            if (clickCount==null)
                clickCount = "0";
            vo.set("clickCount",clickCount);
            vo.set("article",article);
            vo.set("tags",tags);
            vos.add(vo);
        }
        model.addAttribute("vos",vos);


        ViewObject pagination = new ViewObject();
        int count = articleService.getArticleCountByCategory(categoryName);

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

        List<Tag> tags = tagService.getAllTag();
        model.addAttribute("tags",tags);
        model.addAttribute("pagination",pagination);
        model.addAttribute("category",categoryName);

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

        ViewObject clickCount = new ViewObject();
        String currentPage = jedisService.get(RedisKeyUntil.getClickCountKey("/category/"+categoryName));
        String sumPage = jedisService.get(RedisKeyUntil.getClickCountKey("SUM"));
        clickCount.set("currentPage",currentPage);
        clickCount.set("sumPage",sumPage);
        model.addAttribute("clickCount",clickCount);

        List<Article> hotArticles = new ArrayList<>();
        Set<String> set = jedisService.zrevrange("hotArticles",0,6);
        for (String str : set){
            int articleId = Integer.parseInt(str.split(":")[1]);
            Article article = articleService.getArticleById(articleId);
            hotArticles.add(article);
        }
        model.addAttribute("hotArticles",hotArticles);

        return "category";
    }

    @RequestMapping(value = "/tag/{tagId}",method = RequestMethod.GET)
    public String tag(Model model, @PathVariable("tagId")int tagId, @RequestParam("pageId")int pageId){
        List<Article> articles = articleService.getArticlesByTag(tagId,(pageId-1)*4,4);
        List<ViewObject> vos = new ArrayList<>();
        for (Article article:articles){
            ViewObject vo = new ViewObject();
            List<Tag> tags = tagService.getTagByArticleId(article.getId());
            String clickCount = jedisService.get(RedisKeyUntil.getClickCountKey("/article/"+article.getId()));
            if (clickCount==null)
                clickCount = "0";
            vo.set("clickCount",clickCount);
            vo.set("article",article);
            vo.set("tags",tags);
            vos.add(vo);
        }
        model.addAttribute("vos",vos);

        ViewObject pagination = new ViewObject();
        int count = articleService.getArticleCountByTag(tagId);

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

        List<Tag> tags = tagService.getAllTag();
        model.addAttribute("tags",tags);
        model.addAttribute("pagination",pagination);
        model.addAttribute("tagId",tagId);

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

        ViewObject clickCount = new ViewObject();
        String currentPage = jedisService.get(RedisKeyUntil.getClickCountKey("/tag/"+tagId));
        String sumPage = jedisService.get(RedisKeyUntil.getClickCountKey("SUM"));
        clickCount.set("currentPage",currentPage);
        clickCount.set("sumPage",sumPage);
        model.addAttribute("clickCount",clickCount);

        List<Article> hotArticles = new ArrayList<>();
        Set<String> set = jedisService.zrevrange("hotArticles",0,6);
        for (String str : set){
            int articleId = Integer.parseInt(str.split(":")[1]);
            Article article = articleService.getArticleById(articleId);
            hotArticles.add(article);
        }
        model.addAttribute("hotArticles",hotArticles);

        return "tag";
    }

    @RequestMapping("/article/{articleId}")
    public String singleArticle(Model model, @PathVariable("articleId")int articleId){
        Article article = articleService.getArticleById(articleId);
        List<Tag> tags = tagService.getTagByArticleId(article.getId());
        model.addAttribute("article",article);
        model.addAttribute("tags",tags);

        ViewObject clickCount = new ViewObject();
        String currentPage = jedisService.get(RedisKeyUntil.getClickCountKey("/article/"+articleId));
        String sumPage = jedisService.get(RedisKeyUntil.getClickCountKey("SUM"));
        clickCount.set("currentPage",currentPage);
        clickCount.set("sumPage",sumPage);
        model.addAttribute("clickCount",clickCount);

        if (hostHolder.getUser()==null)
            model.addAttribute("liked",0);
        else
            model.addAttribute("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),articleId));
        model.addAttribute("likeCount",likeService.getLikeCount(articleId));
        model.addAttribute("dislikeCount",likeService.getDislikeCount(articleId));

        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment: comments){
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        model.addAttribute("commentsCount",comments.size());

        String articleClickCount = jedisService.get(RedisKeyUntil.getClickCountKey("/article/"+article.getId()));
        if (articleClickCount==null)
            articleClickCount = "0";
        model.addAttribute("articleClickCount",articleClickCount);

        return "article";
    }
}
