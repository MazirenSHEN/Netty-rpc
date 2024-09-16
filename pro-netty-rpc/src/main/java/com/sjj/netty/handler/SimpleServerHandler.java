package com.sjj.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.sjj.netty.handler.param.ServerRequest;
import com.sjj.netty.util.Response;

import io.netty.channel.ChannelHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter implements ChannelHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//System.out.println("服务器Handler:"+msg.toString());
		//ctx.channel().writeAndFlush("111\r\n");
		ServerRequest request = JSONObject.parseObject(msg.toString(),ServerRequest.class);
		
		Response resp = new Response();
		resp.setId(request.getId());
		resp.setResult("is ok");
			
		ctx.channel().writeAndFlush(JSONObject.toJSONString(resp));
		ctx.channel().writeAndFlush("\r\n");
		//System.out.println("服务器Handler:");
		//ctx.channel().close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent){
			IdleStateEvent event = (IdleStateEvent)evt;
			
			if(event.state().equals(IdleState.READER_IDLE)){
				System.out.println("读空闲");
				ctx.channel().close();
			}
			if(event.state().equals(IdleState.WRITER_IDLE)){
				System.out.println("写空闲");
			}
			if(event.state().equals(IdleState.ALL_IDLE)){
				System.out.println("读写空闲");
				ctx.channel().writeAndFlush("ping\r\n");
			}
		}
	}
	
	
	
	
	
}
