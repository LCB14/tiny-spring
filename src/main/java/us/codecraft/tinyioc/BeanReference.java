package us.codecraft.tinyioc;

/**
 * @author yihua.huang@dianping.com
 *
 * Object 如果是 BeanReference 类型，则说明其是一个引用，其中保存了引用的名字，
 * 需要用先进行解析，转化为对应的实际 Object。
 */
public class BeanReference {

    private String name;

    private Object bean;

    public BeanReference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}
