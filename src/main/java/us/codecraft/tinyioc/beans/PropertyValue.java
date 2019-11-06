package us.codecraft.tinyioc.beans;

/**
 * 用于bean的属性注入
 * @author yihua.huang@dianping.com
 */
public class PropertyValue {

    /**
     * 属性名
     */
    private final String name;

    /**
     * 属性值
     */
    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
