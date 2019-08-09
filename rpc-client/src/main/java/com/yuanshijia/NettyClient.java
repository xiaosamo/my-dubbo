package com.yuanshijia;

import com.yuanshijia.common.RpcRequest;
import com.yuanshijia.common.RpcResponse;
import com.yuanshijia.common.coder.RpcDecoder;
import com.yuanshijia.common.coder.RpcEncoder;
import com.yuanshijia.common.serialization.JsonSerialization;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class NettyClient {

    private String host;
    private int port;

    private Channel channel;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RpcResponse send(RpcRequest request) {
        try {
            // 向server发送请求
            channel.writeAndFlush(request);
            // 同步阻塞，等待返回
            synchronized (ClientHandler.lock) {
                ClientHandler.lock.wait();
                return ClientHandler.getResponse();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void connect(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));
//                        pipeline.addLast(new RpcCodec(RpcResponse.class, new JsonSerialization()));
                        pipeline.addLast(new RpcEncoder(RpcRequest.class,new JsonSerialization()));
                        pipeline.addLast(new RpcDecoder(RpcResponse.class,new JsonSerialization()));

                        
                        pipeline.addLast(new ClientHandler());
                    }
                });

        try {
            this.channel = bootstrap.connect(this.host, this.port).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
