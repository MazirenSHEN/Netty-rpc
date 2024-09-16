package com.sjj.consumer.client.handler;

import com.alibaba.fastjson.JSONObject;
import com.sjj.consumer.client.core.ResultFuture;
import com.sjj.consumer.client.param.Response;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter implements ChannelHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println(msg.toString());
		if(msg.toString().equals("ping")) {
			ctx.channel().writeAndFlush("ping\r\n");
			return;
		}
		//ctx.channel().attr(AttributeKey.valueOf("ssss")).set(msg);
		Response response = JSONObject.parseObject(msg.toString(),Response.class);
		ResultFuture.receive(response);
		//ctx.channel().close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);
	}

	
	
}
