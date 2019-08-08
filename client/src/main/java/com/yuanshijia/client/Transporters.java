package com.yuanshijia.client;

import com.yuanshiji.common.RpcRequest;
import com.yuanshiji.common.RpcResponse;
import com.yuanshijia.NettyClient;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class Transporters {
    public static RpcResponse send(RpcRequest request){
        NettyClient nettyClient = new NettyClient("127.0.0.1", 9090);
        nettyClient.connect();
        RpcResponse response = nettyClient.send(request);
        return response;
    }
}
