package com.yuanshijia.common;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 */
public class RpcCallbackFuture {

    /**
     *  过期，失效
     */
    public static ConcurrentMap<String, RpcCallbackFuture> futurePool = new ConcurrentHashMap<String, RpcCallbackFuture>();


    private RpcRequest request;
    private RpcResponse response;

    private boolean isDone = false;
    private final Object lock = new Object();


    public RpcCallbackFuture(RpcRequest request) {
        this.request = request;
        futurePool.put(request.getRequestId(), this);
    }


    public RpcResponse get(long timeoutMillis) throws InterruptedException, TimeoutException {
        if (!isDone) {
            synchronized (lock) {
                try {
                    lock.wait(timeoutMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        if (!isDone) {
            throw new TimeoutException(MessageFormat.format(">>>>>>>>>>>> child-rpc, netty request timeout at:{0}, request:{1}", System.currentTimeMillis(), request.toString()));
        }
        return response;
    }


    public RpcResponse getResponse() {
        return response;
    }
    public void setResponse(RpcResponse response) {
        this.response = response;
        // notify future lock
        synchronized (lock) {
            isDone = true;
            lock.notifyAll();
        }
    }


}
