package com.sjj.netty.medium;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.sjj.netty.annotation.Remote;
import com.sjj.netty.annotation.RemoteInvoke;

@Component
public class InitialMedium implements BeanPostProcessor{

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean.getClass().isAnnotationPresent(Remote.class)){
			Method[] methods = bean.getClass().getDeclaredMethods();//客户端那里用的是接口，所以getSuperClass
			for(Method m : methods){
				String key = bean.getClass().getInterfaces()[0].getName()+"."+m.getName();
				//String key = m.getName();//修改
				HashMap<String, BeanMethod> map = Medium.mediamap;
				BeanMethod beanMethod = new BeanMethod();
				beanMethod.setBean(bean);
				beanMethod.setMethod(m);
				map.put(key,beanMethod);
				System.out.println(key);
			}
		}
		return bean;
	}

	
	
}
