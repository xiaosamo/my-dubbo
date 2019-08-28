package com.yuanshijia.client;

import com.yuanshijia.common.RpcRequest;
import com.yuanshijia.common.RpcResponse;
import com.yuanshijia.NettyClient;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class Transporters {
    public static RpcResponse send(RpcRequest request) {
        NettyClient nettyClient = new NettyClient("127.0.0.1", 8990);
        nettyClient.connect();

        // 发送请求并返回结果
        RpcResponse response = nettyClient.send(request);
        return response;
    }
}
