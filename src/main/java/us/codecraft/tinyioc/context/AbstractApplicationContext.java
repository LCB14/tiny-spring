package us.codecraft.tinyioc.context;

import us.codecraft.tinyioc.beans.BeanPostProcessor;
import us.codecraft.tinyioc.beans.factory.AbstractBeanFactory;

import java.util.List;

/**
 * ApplicationContext 的抽象实现，内部包含一个 BeanFactory 类。主要方法有 getBean() 和 refresh() 方法。
 * getBean() 直接调用了内置 BeanFactory 的 getBean() 方法，refresh() 则用于实现 BeanFactory 的刷新，
 * 也就是告诉 BeanFactory 该使用哪个资源（Resource）加载类定义（BeanDefinition）信息，
 * 该方法留给子类实现，用以实现 从不同来源的不同类型的资源加载类定义 的效果。
 *
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
	protected AbstractBeanFactory beanFactory;

	public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/**
	 * 用于实现 BeanFactory 的刷新，也就是告诉 BeanFactory 该使用哪个资源（Resource）加载类定义（BeanDefinition）信息
	 *
	 * @throws Exception
	 */
	public void refresh() throws Exception {
		// 加载类定义，并注入到内置的 BeanFactory 中，这里的可扩展性在于，未对加载方法进行要求，也就是可以从不同来源的不同类型的资源进行加载。
		loadBeanDefinitions(beanFactory);

		// 获取所有的 BeanPostProcessor，并注册到 BeanFactory 维护的 BeanPostProcessor 列表去。
		registerBeanPostProcessors(beanFactory);

		// 以单例的方式，初始化所有 Bean。tiny-spring 只支持 singleton 模式。
		onRefresh();
	}

	protected abstract void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception;

	protected void registerBeanPostProcessors(AbstractBeanFactory beanFactory) throws Exception {
		List beanPostProcessors = beanFactory.getBeansForType(BeanPostProcessor.class);
		for (Object beanPostProcessor : beanPostProcessors) {
			beanFactory.addBeanPostProcessor((BeanPostProcessor) beanPostProcessor);
		}
	}

	protected void onRefresh() throws Exception{
        beanFactory.preInstantiateSingletons();
    }

	@Override
	public Object getBean(String name) throws Exception {
		return beanFactory.getBean(name);
	}
}
