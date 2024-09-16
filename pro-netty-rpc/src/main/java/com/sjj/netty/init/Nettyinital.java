package com.sjj.netty.init;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.sjj.netty.constant.Constants;
import com.sjj.netty.factory.ZookeeperFactory;
import com.sjj.netty.handler.ServerHandler;
import com.sjj.netty.handler.SimpleServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

@Component
public class Nettyinital implements ApplicationListener<ContextRefreshedEvent>{

	public void start() {
		
		EventLoopGroup parentGroup =new NioEventLoopGroup();
		EventLoopGroup childGroup =new NioEventLoopGroup();
		
		try {
			
			ServerBootstrap bootstrap =new ServerBootstrap();
			bootstrap.group(parentGroup,childGroup);
			bootstrap.option(ChannelOption.SO_BACKLOG,128)
					 .childOption(ChannelOption.SO_KEEPALIVE, false)
					 .channel(NioServerSocketChannel.class)
					 .childHandler(new ChannelInitializer<SocketChannel>() {
						 	@Override
							public void initChannel(SocketChannel ch) throws Exception {
								// 设置\r\n为分隔符
								ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
								ch.pipeline().addLast(new StringDecoder());//字符串解码器
								ch.pipeline().addLast(new IdleStateHandler(60, 45, 20, TimeUnit.SECONDS));
								ch.pipeline().addLast(new ServerHandler());//业务逻辑处理处
								ch.pipeline().addLast(new StringEncoder());//字符串编码器
								
							}
						   
						 
					 });
			
			
			ChannelFuture f = bootstrap.bind(8080).sync();
			CuratorFramework client = ZookeeperFactory.create();
			InetAddress netAddress = InetAddress.getLocalHost();
			if(client != null){
				System.out.println(client);
				//String path = "/netty/192.168.1.3";
			    
			    // Check if node already exists
			    if (client.checkExists().forPath(Constants.SERVER_PATH+netAddress.getHostAddress()) == null) {
			        // If the node doesn't exist, create it
			    	client.create().withMode(CreateMode.EPHEMERAL).forPath(Constants.SERVER_PATH+netAddress.getHostAddress());
			    } else {
			        System.out.println("Node already exists: " + Constants.SERVER_PATH+netAddress.getHostAddress());
			    }
				//client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(Constans.SERVER_PATH+"/"+address.getHostAddress()+"#"+port+"#");
				//client.create().withMode(CreateMode.EPHEMERAL).forPath(Constants.SERVER_PATH+netAddress.getHostAddress());
				System.out.println("成功");

			}
			
			
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
		
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		this.start();
	}
	
}
