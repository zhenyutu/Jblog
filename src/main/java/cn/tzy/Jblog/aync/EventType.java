package cn.tzy.Jblog.aync;

/**
 * Created by tuzhenyu on 17-7-26.
 * @author tuzhenyu
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2);

    private int value;
    EventType(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
