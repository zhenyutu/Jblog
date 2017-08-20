package cn.tzy.Jblog.aync;

import cn.tzy.Jblog.service.JedisService;
import cn.tzy.Jblog.util.RedisKeyUntil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tuzhenyu on 17-7-26.
 * @author tuzhenyu
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{
    @Autowired
    private JedisService jedisService;

    private Map<EventType,List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans!=null){
            for (Map.Entry<String,EventHandler> entry : beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type : eventTypes){
                    if (!config.containsKey(type)){
                        config.put(type,new ArrayList<>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUntil.getEventQueue();
                    List<String> events = jedisService.brpop(0,key);
                    for (String event : events){
                        if (event.equals(key))
                            continue;
                        EventModel model = JSON.parseObject(event,EventModel.class);
                        for (EventHandler handler:config.get(model.getType())){
                            handler.doHandler(model);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
