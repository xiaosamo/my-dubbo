package com.yuanshijia;

import com.yuanshijia.common.RpcResponse;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    public static final Object lock = new Object();

    private static RpcResponse response;

    public static RpcResponse getResponse(){
        return response;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        log.info("response=" + response);
        ClientHandler.response = response;
        synchronized (lock) {
            // 释放
            lock.notifyAll();
        }
    }
}

