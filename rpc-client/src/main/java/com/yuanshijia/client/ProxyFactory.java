package com.yuanshijia.client;

import java.lang.reflect.Proxy;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 * 代理类
 */
public class ProxyFactory {
    /**
     * 当 proxyFactory 生成的类被调用的时候，就会执行 RpcInvoker 方法
     * @param interfaceClass
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> interfaceClass){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader()
                , new Class<?>[]{interfaceClass}
                , new RpcInvoker<T>(interfaceClass)
        );
    }
}
