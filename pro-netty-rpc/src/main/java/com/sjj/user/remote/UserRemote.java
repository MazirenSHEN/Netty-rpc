package com.sjj.user.remote;

import java.util.List;

import com.sjj.netty.util.Response;
import com.sjj.user.bean.User;

public interface UserRemote {
	public Response saveUser(User user);
	public Response saveUsers(List<User> userlist);
}
