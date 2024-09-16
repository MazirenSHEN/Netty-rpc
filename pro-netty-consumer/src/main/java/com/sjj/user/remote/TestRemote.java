package com.sjj.user.remote;

import com.sjj.consumer.client.param.Response;
import com.sjj.user.model.User;

public interface TestRemote {
	public Response testUser(User user);
}