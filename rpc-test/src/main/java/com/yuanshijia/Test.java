package com.yuanshijia;

import com.yuanshijia.client.ProxyFactory;
import com.yuanshijia.common.api.HelloService;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 */
public class Test {

    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.create(HelloService.class);
        System.out.println("调用service成功 -> " + helloService.hello("yuan"));
    }
}
