package com.sjj.user.remote;

import java.util.List;

import javax.annotation.Resource;

import com.sjj.netty.annotation.Remote;
import com.sjj.netty.util.Response;
import com.sjj.netty.util.ResponseUtil;
import com.sjj.user.bean.User;
import com.sjj.user.service.UserService;



@Remote
public class UserRemoteImpl implements UserRemote{
	
	@Resource
	private UserService service;
	
	public Response saveUser(User user){
		service.saveUser(user);
		Response response = ResponseUtil.createSuccessResponse(user);
		
		return response;
	}
	
	public Response saveUsers(List<User> userlist){
		service.saveUserList(userlist);
		Response response = ResponseUtil.createSuccessResponse(userlist);
		
		return response;
	}
}
