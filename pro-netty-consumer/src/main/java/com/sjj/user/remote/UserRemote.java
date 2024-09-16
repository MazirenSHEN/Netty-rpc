package com.sjj.user.remote;

import java.util.List;

import com.sjj.consumer.client.param.Response;
import com.sjj.user.model.User;

public interface UserRemote {
	public Response saveUser(User user);
	public Response saveUsers(List<User> userlist);
}
