package com.yuanshijia;

import com.yuanshijia.common.api.HelloService;
import com.yuanshijia.client.ProxyFactory;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class Test {
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.create(HelloService.class);
        System.out.println("调用service结果: " + helloService.hello("yuan"));
    }
}
