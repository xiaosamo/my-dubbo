package com.yuanshijia;

import com.yuanshiji.common.api.HelloService;
import com.yuanshijia.client.ProxyFactory;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class Test {
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.create(HelloService.class);
        System.out.println(helloService.hello("yuan"));
    }
}
