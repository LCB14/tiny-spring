package us.codecraft.tinyioc.aop;

import us.codecraft.tinyioc.beans.factory.BeanFactory;

/**
 * 这个接口提供了对 BeanFactory 的感知，这样，尽管它是容器中的一个 Bean，却可以获取容器的引用，进而获取容器中所有的切点对象，
 * 决定对哪些对象的哪些方法进行代理。解决了 为哪些对象提供 AOP 的植入 的问题。
 *
 * @author yihua.huang@dianping.com
 */
public interface BeanFactoryAware {

    void setBeanFactory(BeanFactory beanFactory) throws Exception;
}
