package us.codecraft.tinyioc.beans.factory;

/**
 * bean的容器
 *
 * 接口，标识一个 IoC 容器。通过 getBean(String) 方法来 获取一个对象
 *
 * @author yihua.huang@dianping.com
 */
public interface BeanFactory {

    Object getBean(String name) throws Exception;

}
