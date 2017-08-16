package cn.tzy.Jblog.controller;

import cn.tzy.Jblog.model.HostHolder;
import cn.tzy.Jblog.model.User;
import cn.tzy.Jblog.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by tuzhenyu on 17-7-25.
 * @author tuzhenyu
 */
@Controller
public class LikeController {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;


    @RequestMapping(path = "/like/{articleId}")
    public String like(@PathVariable("articleId")int articleId){
        User user = hostHolder.getUser();
       if (user==null){
           return "redirect:/in?next=/article/"+articleId;
       }

       likeService.like(user.getId(),articleId);
       return "redirect:/article/"+articleId;
    }

    @RequestMapping(path = "/dislike/{articleId}")
    public String dislike(@PathVariable("articleId")int articleId){
        User user = hostHolder.getUser();
        if (user==null){
            return "redirect:/in?next=/article/"+articleId;
        }
        likeService.dislike(user.getId(),articleId);
        return "redirect:/article/"+articleId;
    }
}
