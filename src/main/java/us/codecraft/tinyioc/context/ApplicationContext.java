package us.codecraft.tinyioc.context;

import us.codecraft.tinyioc.beans.factory.BeanFactory;

/**
 * 标记接口，继承了 BeanFactory。通常，要实现一个 IoC 容器时，需要先通过 ResourceLoader 获取一个 Resource，
 * 其中包括了容器的配置、Bean 的定义信息。接着，使用 BeanDefinitionReader 读取该 Resource 中的 BeanDefinition 信息。
 * 最后，把 BeanDefinition 保存在 BeanFactory 中，容器配置完毕可以使用。注意到 BeanFactory 只实现了 Bean 的 装配、获取，
 * 并未说明 Bean 的 来源 也就是 BeanDefinition 是如何 加载 的。
 *
 * 该接口把 BeanFactory 和 BeanDefinitionReader 结合在了一起。
 *
 * @author yihua.huang@dianping.com
 *
 * 注 1：在 Spring 的实现中，对 ApplicatinoContext 的分层更为细致。AbstractApplicationContext 中为了实现 不同来源 的 不同类型 的资源加载类定义，把这两步分层实现。
 * 以“从类路径读取 XML 定义”为例，首先使用 AbstractXmlApplicationContext 来实现 不同类型 的资源解析，接着，
 * 通过 ClassPathXmlApplicationContext 来实现 不同来源 的资源解析。
 *
 * 注 2：在 tiny-spring 的实现中，先用 BeanDefinitionReader 读取 BeanDefiniton 后，保存在内置的
 * registry （键值对为 String - BeanDefinition 的哈希表，通过 getRigistry() 获取）中，
 * 然后由 ApplicationContext 把 BeanDefinitionReader 中 registry 的键值对一个个赋值给
 * BeanFactory 中保存的 beanDefinitionMap。而在 Spring 的实现中，BeanDefinitionReader
 * 直接操作 BeanDefinition ，它的 getRegistry() 获取的不是内置的 registry，而是 BeanFactory 的实例。
 * 如何实现呢？以 DefaultListableBeanFactory 为例，它实现了一个 BeanDefinitonRigistry 接口，
 * 该接口把 BeanDefinition 的 注册 、获取 等方法都暴露了出来，这样，BeanDefinitionReader
 * 可以直接通过这些方法把 BeanDefiniton 直接加载到 BeanFactory 中去。
 */
public interface ApplicationContext extends BeanFactory {
}
