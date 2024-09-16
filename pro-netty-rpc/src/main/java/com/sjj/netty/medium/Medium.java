package com.sjj.netty.medium;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.sjj.netty.handler.param.ServerRequest;
import com.sjj.netty.util.Response;

public class Medium {
	public static final HashMap<String, BeanMethod> mediamap = new HashMap<String,BeanMethod>();
	private static Medium media = null;
	
	
	private Medium(){}
	
	public static Medium newInstance(){
		if(media == null){
			media = new Medium();
		}
		
		return media;
	}
	
	//反射业务处理代码
	public Response process(ServerRequest request){
		Response result = null;
		try {
			String command = request.getCommand();//command是key
			BeanMethod beanMethod = mediamap.get(command);
			if(beanMethod == null){
				return null;
			}
			
			Object bean = beanMethod.getBean();
			Method method = beanMethod.getMethod();
			Class type = method.getParameterTypes()[0];//先只实现1个参数的方法
			Object content = request.getContent();
			Object args = JSONObject.parseObject(JSONObject.toJSONString(content), type);
			
			result = (Response) method.invoke(bean, args);
			result.setId(request.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
}
