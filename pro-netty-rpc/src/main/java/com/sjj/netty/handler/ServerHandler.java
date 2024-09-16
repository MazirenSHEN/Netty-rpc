package com.sjj.netty.handler;

import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

import com.alibaba.fastjson.JSONObject;
import com.sjj.netty.handler.param.ServerRequest;
import com.sjj.netty.medium.Medium;
import com.sjj.netty.util.Response;

import io.netty.channel.ChannelHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHandler extends ChannelInboundHandlerAdapter implements ChannelHandler {
	private static final Executor exec = Executors.newFixedThreadPool(10);
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("服务器Handler:"+msg.toString());
		//ctx.channel().writeAndFlush("111\r\n");
//		exec.execute(new Runnable() {
//			
//			@Override
//			public void run() {
//				ServerRequest serverRequest = JSONObject.parseObject(msg.toString(), ServerRequest.class);
//				System.out.println(serverRequest.getCommand());
//				Medium medium = Medium.newInstance();//生成中介者模式
//				
//				Response response = medium.process(serverRequest);
//				
//				//向客户端发送Resonse
//				ctx.channel().writeAndFlush(JSONObject.toJSONString(response)+"\r\n");
//			}
//		});
		ServerRequest request = JSONObject.parseObject(msg.toString(),ServerRequest.class);
		
		Medium medium = Medium.newInstance();
		Response result = medium.process(request);
		ctx.channel().writeAndFlush(JSONObject.toJSONString(result));
		ctx.channel().writeAndFlush("\r\n");
		
		
		//Response resp = new Response();
		//resp.setId(request.getId());
		//resp.setResult("is ok");
			
		
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
