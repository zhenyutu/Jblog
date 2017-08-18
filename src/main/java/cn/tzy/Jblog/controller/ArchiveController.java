package cn.tzy.Jblog.controller;

import cn.tzy.Jblog.dao.ArchiveDao;
import cn.tzy.Jblog.model.Archive;
import cn.tzy.Jblog.model.ViewObject;
import cn.tzy.Jblog.service.JedisService;
import cn.tzy.Jblog.util.RedisKeyUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tuzhenyu on 17-8-18.
 * @author tuzhenyu
 */
@Controller
public class ArchiveController {
    @Autowired
    private ArchiveDao archiveDao;

    @Autowired
    private JedisService jedisService;

    @RequestMapping("/archive")
    public String archive(Model model){
        List<Archive> archives = archiveDao.seletArticleGroupByTime();
        Map<String,List<Archive>> map = new HashMap<>();
        for (Archive archive : archives){
            String date = archive.getYear()+"-"+archive.getMonth();
            List<Archive> list = map.getOrDefault(date,new ArrayList<>());
            list.add(archive);
            map.put(date,list);
        }
        List<ViewObject> vos = new ArrayList<>();
        for (Map.Entry entry:map.entrySet()){
            ViewObject vo = new ViewObject();
            vo.set("date",entry.getKey());
            vo.set("list",entry.getValue()  );
            vos.add(vo);
        }
        model.addAttribute("vos",vos);

        ViewObject clickCount = new ViewObject();
        String currentPage = jedisService.get(RedisKeyUntil.getClickCountKey("/archive"));
        String sumPage = jedisService.get(RedisKeyUntil.getClickCountKey("SUM"));
        clickCount.set("currentPage",currentPage);
        clickCount.set("sumPage",sumPage);
        model.addAttribute("clickCount",clickCount);

        return "archive";
    }
}
