package com.sjj.netty.client;

import com.alibaba.fastjson.JSONObject;
import com.sjj.netty.handler.SimpleClientHandler;
import com.sjj.netty.util.Response;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

public class NettyClient {
	
	private static ChannelFuture f = null;
	public static void main(String[] args) {
		String host = "localhost";
		int port = 8080;
		
		EventLoopGroup work = new NioEventLoopGroup();
		try {
			Bootstrap boot = new Bootstrap();
			boot.group(work)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE,true)
				.handler(new ChannelInitializer<Channel>() {
	
							@Override
							public void initChannel(Channel ch) throws Exception {
								ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
								ch.pipeline().addLast(new StringDecoder());//字符串解码器
								ch.pipeline().addLast(new SimpleClientHandler());//业务逻辑处理处
								ch.pipeline().addLast(new StringEncoder());//字符串编码器
							}
				});
				
			f = boot.connect(host, port).sync();
			//f.channel().writeAndFlush("hello server");
			//f.channel().writeAndFlush("\r\n");
			
			//f.channel().closeFuture().sync();
		
			//Object result = f.channel().attr(AttributeKey.valueOf("ssss")).get();
			//System.out.println("获取到服务器返回的数据=="+ result);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static Response send(ClientRequest request){
		f.channel().writeAndFlush(JSONObject.toJSONString(request)+"\r\n");
		ResultFuture future = new ResultFuture(request);
		return future.get();
	}
}
