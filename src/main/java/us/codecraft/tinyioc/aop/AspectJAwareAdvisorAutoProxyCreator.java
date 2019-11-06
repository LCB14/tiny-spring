package us.codecraft.tinyioc.aop;

import org.aopalliance.intercept.MethodInterceptor;
import us.codecraft.tinyioc.beans.BeanPostProcessor;
import us.codecraft.tinyioc.beans.factory.AbstractBeanFactory;
import us.codecraft.tinyioc.beans.factory.BeanFactory;

import java.util.List;

/**
 * AspectJAwareAdvisorAutoProxyCreator是实现 AOP 植入的关键类
 *
 * 1、AspectJAwareAdvisorAutoProxyCreator（实现了 BeanPostProcessor 接口）在实例化所有的 Bean 前，最先被实例化。
 *
 * 2、其他普通 Bean 被实例化、初始化，在初始化的过程中，AspectJAwareAdvisorAutoProxyCreator 加载 BeanFactory
 * 中所有的 PointcutAdvisor（这也保证了 PointcutAdvisor 的实例化顺序优于普通 Bean。），然后依次使用 PointcutAdvisor
 * 内置的 ClassFilter，判断当前对象是不是要拦截的类。
 *
 * 3、如果是，则生成一个 TargetSource（要拦截的对象和其类型），并取出 AspectJAwareAdvisorAutoProxyCreator 的
 * MethodMatcher（对哪些方法进行拦截）、Advice（拦截的具体操作），再，交给 AopProxy 去生成代理对象。
 *
 * 4、AopProxy 生成一个 InvocationHandler，在它的 invoke 函数中，首先使用 MethodMatcher 判断是不是要拦截的方法，
 * 如果是则交给 Advice 来执行（Advice 由用户来编写，其中也要手动/自动调用原始对象的方法），如果不是，
 * 则直接交给 TargetSource 的原始对象来执行。
 *
 * @author yihua.huang@dianping.com
 */
public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {

	private AbstractBeanFactory beanFactory;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
		if (bean instanceof AspectJExpressionPointcutAdvisor) {
			return bean;
		}
		if (bean instanceof MethodInterceptor) {
			return bean;
		}
		List<AspectJExpressionPointcutAdvisor> advisors = beanFactory
				.getBeansForType(AspectJExpressionPointcutAdvisor.class);
		for (AspectJExpressionPointcutAdvisor advisor : advisors) {
			if (advisor.getPointcut().getClassFilter().matches(bean.getClass())) {
                ProxyFactory advisedSupport = new ProxyFactory();
				advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
				advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

				TargetSource targetSource = new TargetSource(bean, bean.getClass(), bean.getClass().getInterfaces());
				advisedSupport.setTargetSource(targetSource);

				return advisedSupport.getProxy();
			}
		}
		return bean;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws Exception {
		this.beanFactory = (AbstractBeanFactory) beanFactory;
	}
}
