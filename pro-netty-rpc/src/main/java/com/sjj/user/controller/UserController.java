package com.sjj.user.controller;

import org.springframework.stereotype.Controller;

import java.util.List;

import javax.annotation.Resource;

import com.sjj.netty.util.Response;
import com.sjj.netty.util.ResponseUtil;
import com.sjj.user.bean.User;
import com.sjj.user.service.UserService;

@Controller
public class UserController {

	@Resource
	private UserService userService;
	
	public Response saveUser(User user) {
		
		userService.saveUser(user);
		return ResponseUtil.createSuccessResponse(user);
	}
	
	public Response saveUsers(List<User> userlist){
		userService.saveUserList(userlist);
	
		
		return ResponseUtil.createSuccessResponse(userlist);
	}
	
}
