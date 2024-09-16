package com.sjj.consumer.client.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import com.sjj.consumer.client.annonation.RemoteInvoke;
import com.sjj.consumer.client.core.TcpClient;
import com.sjj.consumer.client.param.ClientRequest;
import com.sjj.consumer.client.param.Response;

@Component
public class InvokeProxy implements BeanPostProcessor{

	public static Enhancer enhancer = new Enhancer();
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {


//		System.out.println(bean.getClass().getName());
		Field[] fields = bean.getClass().getDeclaredFields();
		for(Field field : fields){
			if(field.isAnnotationPresent(RemoteInvoke.class)){
				field.setAccessible(true);
				
				final HashMap<Method, Class> methodmap = new HashMap<Method, Class>();
				putMethodClass(methodmap,field);
//				Enhancer enhancer = new Enhancer();
				enhancer.setInterfaces(new Class[]{field.getType()});
				enhancer.setCallback(new MethodInterceptor() {
					
					public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
						ClientRequest clientRequest = new ClientRequest();
						clientRequest.setContent(args[0]);
						String command= methodmap.get(method).getName()+"."+method.getName();
						//String command = method.getName();//修改
//						System.out.println("InvokeProxy中的Command是:"+command);
						clientRequest.setCommand(command);
						
						Response response = TcpClient.send(clientRequest);
						return response;
					}
				});
				try {
					field.set(bean, enhancer.create());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return bean;
	}

	
	/**
	 * 对属性的所有方法和属性接口类型放入到一个map
	 * @param methodmap
	 * @param field
	 */
	private void putMethodClass(HashMap<Method, Class> methodmap, Field field) {
		Method[] methods = field.getType().getDeclaredMethods();
		for(Method method : methods){
			methodmap.put(method, field.getType());
		}
		
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}

}
