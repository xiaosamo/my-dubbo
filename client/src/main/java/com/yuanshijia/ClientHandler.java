package com.yuanshijia;

import com.yuanshiji.common.RpcResponse;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static RpcResponse response;

    public static RpcResponse getResponse() {
        return response;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        ClientHandler.response = response;
    }
}

