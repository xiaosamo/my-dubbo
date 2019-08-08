package com.yuanshijia.netty;

import com.yuanshiji.common.RpcRequest;
import com.yuanshiji.common.RpcResponse;
import com.yuanshiji.common.coder.RpcCodec;
import com.yuanshiji.common.coder.RpcDecoder;
import com.yuanshiji.common.coder.RpcEncoder;
import com.yuanshiji.common.serialization.JsonSerialization;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class NettyServer {

    private ChannelFuture channelFuture;

    public NettyServer(){

    }

    private void start(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG)) // 输出日志
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        // 处理 tcp 请求中粘包的 coder
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));

                        // protocol 中实现的 序列化和反序列化 coder
//                        socketChannel.pipeline().addLast(new RpcCodec(RpcResponse.class, new JsonSerialization()));
                        pipeline.addLast(new RpcEncoder(RpcResponse.class,new JsonSerialization()));
                        pipeline.addLast(new RpcDecoder(RpcRequest.class,new JsonSerialization()));

                        // 处理请求的handler
                        pipeline.addLast(new ServerHandler());
                    }
                });


        this.channelFuture = serverBootstrap.bind(port).sync();
        System.out.println("Server startup...");
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer().start(9090);
    }
}
