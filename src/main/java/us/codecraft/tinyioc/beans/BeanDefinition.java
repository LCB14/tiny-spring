package us.codecraft.tinyioc.beans;

/**
 * bean的内容及元数据，保存在BeanFactory中，包装bean的实体
 *
 * 该类保存了 Bean 定义。包括 Bean 的 名字 String beanClassName、类型 Class beanClass、
 * 属性 PropertyValues propertyValues。根据其 类型 可以生成一个类实例，然后可以把 属性 注入进去。
 *
 * propertyValues 里面包含了一个个 PropertyValue 条目，每个条目都是键值对 String - Object，
 * 分别对应要生成实例的属性的名字与类型。在 Spring 的 XML 中的 property 中，键是 key ，值是 value 或者 ref。
 *
 * 对于 value 只要直接注入属性就行了，但是 ref 要先进行解析。Object 如果是 BeanReference 类型，
 * 则说明其是一个引用，其中保存了引用的名字，需要用先进行解析，转化为对应的实际 Object。
 *
 * @author yihua.huang@dianping.com
 */
public class BeanDefinition {

	private Object bean;

	private Class beanClass;

	private String beanClassName;

	private PropertyValues propertyValues = new PropertyValues();

	public BeanDefinition() {
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public Class getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class beanClass) {
		this.beanClass = beanClass;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
		try {
			this.beanClass = Class.forName(beanClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Object getBean() {
		return bean;
	}

	public PropertyValues getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(PropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}
}
