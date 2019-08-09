package com.yuanshijia.service;

import com.yuanshijia.common.RpcInterface;
import com.yuanshijia.common.api.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
@RpcInterface
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }

}
