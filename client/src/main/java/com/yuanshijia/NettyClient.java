package com.yuanshijia;

import com.yuanshiji.common.RpcRequest;
import com.yuanshiji.common.RpcResponse;
import com.yuanshiji.common.coder.RpcCodec;
import com.yuanshiji.common.coder.RpcDecoder;
import com.yuanshiji.common.coder.RpcEncoder;
import com.yuanshiji.common.serialization.JsonSerialization;
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
            channel.writeAndFlush(request).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ClientHandler.getResponse();
//        return clientHandler.getRpcResponse(request.getRequestId());
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
                        pipeline.addLast(new RpcEncoder(RpcResponse.class,new JsonSerialization()));
                        pipeline.addLast(new RpcDecoder(RpcRequest.class,new JsonSerialization()));

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
