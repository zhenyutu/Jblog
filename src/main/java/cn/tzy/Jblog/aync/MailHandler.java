package cn.tzy.Jblog.aync;

import cn.tzy.Jblog.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tuzhenyu on 17-7-26.
 * @author tuzhenyu
 */
@Component
public class MailHandler implements EventHandler{

    @Autowired
    private MailSender mailSender;

    @Override
    public void doHandler(EventModel model) {
        Map<String,Object> map = new HashMap<>();
        map.put("username",model.getExts("username"));
        map.put("articleId",model.getExts("articleId"));
        mailSender.sendWithHTMLTemplate(model.getExts("email"),"有新的评论啦！","mail.html",map);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT);
    }
}
