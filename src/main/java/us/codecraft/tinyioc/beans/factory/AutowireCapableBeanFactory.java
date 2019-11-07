package us.codecraft.tinyioc.beans.factory;

import us.codecraft.tinyioc.BeanReference;
import us.codecraft.tinyioc.aop.BeanFactoryAware;
import us.codecraft.tinyioc.beans.BeanDefinition;
import us.codecraft.tinyioc.beans.PropertyValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 可自动装配内容的BeanFactory
 *
 * 可以实现自动装配的 BeanFactory。在这个工厂中，实现了 doCreateBean 方法，该方法分三步：
 * 1，通过 BeanDefinition 中保存的类信息实例化一个对象；
 * 2，把对象保存在 BeanDefinition 中，以备下次获取；
 * 3，为其装配属性。装配属性时，通过 BeanDefinition 中维护的 PropertyValues 集合类，
 * 把 String - Value 键值对注入到 Bean 的属性中去。如果 Value 的类型是 BeanReference
 * 则说明其是一个引用（对应于 XML 中的 ref），通过 getBean 对其进行获取，然后注入到属性中。
 *
 * @author yihua.huang@dianping.com
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory {

	@Override
    protected void applyPropertyValues(Object bean, BeanDefinition mbd) throws Exception {
		if (bean instanceof BeanFactoryAware) {
			((BeanFactoryAware) bean).setBeanFactory(this);
		}
		for (PropertyValue propertyValue : mbd.getPropertyValues().getPropertyValues()) {
			Object value = propertyValue.getValue();
			if (value instanceof BeanReference) {
				BeanReference beanReference = (BeanReference) value;
				value = getBean(beanReference.getName());
			}

			try {
				Method declaredMethod = bean.getClass().getDeclaredMethod(
						"set" + propertyValue.getName().substring(0, 1).toUpperCase()
								+ propertyValue.getName().substring(1), value.getClass());
				declaredMethod.setAccessible(true);

				declaredMethod.invoke(bean, value);
			} catch (NoSuchMethodException e) {
				Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
				declaredField.setAccessible(true);
				declaredField.set(bean, value);
			}
		}
	}
}
