package com.yuanshijia.service;

import com.yuanshiji.common.api.HelloService;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }

}
