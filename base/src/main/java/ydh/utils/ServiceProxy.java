package ydh.utils;

import java.lang.reflect.Method;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServiceProxy implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	
	/**
	 * 生成Service调用代理，支持0、1、多个Service
	 * @param serviceInterface  service实现的接口
	 * @return 代理对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T proxy(final Class<T> serviceInterface) {
		Enhancer enhancer = new Enhancer();
		enhancer.setInterfaces(new Class[]{serviceInterface});
		enhancer.setCallback(new MethodInterceptor() {
			public Object intercept(Object obj, Method method, Object[] args,  
		            MethodProxy proxy) throws Throwable {
				final Map<String, T> services = applicationContext.getBeansOfType(serviceInterface);
				for (T service : services.values()) {
					proxy.invoke(service, args);
				}
		        return null;
		    }
		});
		return (T)enhancer.create();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ServiceProxy.applicationContext = applicationContext;
	}
	
	
}
