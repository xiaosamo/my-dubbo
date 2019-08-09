package com.yuanshijia.client;

import com.yuanshijia.ClientHandler;
import com.yuanshijia.common.RpcCallbackFuture;
import com.yuanshijia.common.RpcRequest;
import com.yuanshijia.common.RpcResponse;
import com.yuanshijia.NettyClient;

import java.util.concurrent.TimeoutException;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class Transporters {
    public static RpcResponse send(RpcRequest request) throws InterruptedException, TimeoutException {
        NettyClient nettyClient = new NettyClient("127.0.0.1", 9090);
        nettyClient.connect();

        // 发送请求并返回结果
        RpcResponse response = nettyClient.send(request);
        return response;
    }
}
