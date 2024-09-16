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

public class TcpClient {
	
	static final Bootstrap boot =new Bootstrap();
	static ChannelFuture f = null;
	static {
		EventLoopGroup work = new NioEventLoopGroup();
		
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
		String host = "localhost";
		int port = 8080;
		try {
			f = boot.connect(host, port).sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static Response send(ClientRequest request){
		f.channel().writeAndFlush(JSONObject.toJSONString(request));
		f.channel().writeAndFlush("\r\n");
		//System.out.println(f.channel().read());
		ResultFuture future = new ResultFuture(request);
 		return future.get();
	}
	
}
