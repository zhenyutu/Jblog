package cn.tzy.Jblog.model;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by tuzhenyu on 17-7-19.
 * @author tuzhenyu
 */
public class ViewObject {
    private Map<String,Object> objects = new HashedMap();

    public void set(String key, Object value){
        objects.put(key,value);
    }

    public Object get(String key){
        return objects.get(key);
    }
}
