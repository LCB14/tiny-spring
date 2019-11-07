package us.codecraft.tinyioc.beans.factory;

import us.codecraft.tinyioc.beans.BeanDefinition;
import us.codecraft.tinyioc.beans.BeanPostProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanFactory 的一种抽象类实现，规范了 IoC 容器的基本结构，但是把生成 Bean 的具体实现方式留给子类实现。
 * IoC 容器的结构：AbstractBeanFactory 维护一个 beanDefinitionMap 哈希表用于保存类的定义信息（BeanDefinition）。
 * 获取 Bean 时，如果 Bean 已经存在于容器中，则返回之，否则则调用 doCreateBean 方法装配一个 Bean。（所谓存在于容器中，
 * 是指容器可以通过 beanDefinitionMap 获取 BeanDefinition 进而通过其 getBean() 方法获取 Bean。）
 *
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractBeanFactory implements BeanFactory {

	/**
	 * 存储bean的容器
	 */
	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

	/**
	 * 存储bean的名称
	 */
	private final List<String> beanDefinitionNames = new ArrayList<String>();

	/**
	 * 存储bean初始化过程可能被用到的后置处理器
	 */
	private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

	@Override
	public Object getBean(String name) throws Exception {
		BeanDefinition beanDefinition = beanDefinitionMap.get(name);
		if (beanDefinition == null) {
			throw new IllegalArgumentException("No bean named " + name + " is defined");
		}
		Object bean = beanDefinition.getBean();
		if (bean == null) {
			// 实例化bean
			bean = doCreateBean(beanDefinition);
			// 初始化bean
            bean = initializeBean(bean, name);
            // 设置bean实例
            beanDefinition.setBean(bean);
		}
		return bean;
	}

	protected Object initializeBean(Object bean, String name) throws Exception {
		/**
		 * 依次取出 BeanPostProcessor 执行 bean = postProcessBeforeInitialization(bean,beanName) 。
		 *
		 * 此处为实现 AOP 的代理提供了可能！
		 * 假设方法要求传入的对象实现了 IObj 接口，实际传入的对象是 Obj，那么在方法中，通过动态代理，
		 * 可以 生成一个实现了 IObj 接口并把 Obj 作为内置对象的代理类 Proxy 返回，此时 Bean 已经被偷偷换成了它的代理类。
 		 */
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
		}

		// 初始化方法 -- 暂未实现
		// TODO:call initialize method
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
		}
        return bean;
	}

	protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
		return beanDefinition.getBeanClass().newInstance();
	}

	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws Exception {
		beanDefinitionMap.put(name, beanDefinition);
		beanDefinitionNames.add(name);
	}

	public void preInstantiateSingletons() throws Exception {
		for (Iterator it = this.beanDefinitionNames.iterator(); it.hasNext();) {
			String beanName = (String) it.next();
			getBean(beanName);
		}
	}

	protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		// 生成一个新实例
		Object bean = createBeanInstance(beanDefinition);
		beanDefinition.setBean(bean);

		/**
		 * 注入属性，包括依赖注入的过程。在依赖注入的过程中，如果 Bean 实现了BeanFactoryAware 接口，则将容器的引用传入到 Bean 中去，
		 * 这样，Bean 将获取对容器操作的权限，也就允许了 编写扩展 IoC 容器的功能的 Bean。
 		 */
		applyPropertyValues(bean, beanDefinition);

		return bean;
	}

	/**
	 * 把生成 Bean 的具体实现方式留给子类实现。-- 模板方法模式在spring使用的很多。
	 *
	 * @param bean
	 * @param beanDefinition
	 * @throws Exception
	 */
	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {

	}

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
		this.beanPostProcessors.add(beanPostProcessor);
	}

	public List getBeansForType(Class type) throws Exception {
		List beans = new ArrayList<Object>();
		for (String beanDefinitionName : beanDefinitionNames) {
			if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
				beans.add(getBean(beanDefinitionName));
			}
		}
		return beans;
	}

}
