package us.codecraft.tinyioc.beans;

/**
 * 从配置中读取BeanDefinition
 *
 * 解析 BeanDefinition 的接口。通过 loadBeanDefinitions(String) 来从一个地址加载类定义。
 *
 * @author yihua.huang@dianping.com
 */
public interface BeanDefinitionReader {

    void loadBeanDefinitions(String location) throws Exception;
}
