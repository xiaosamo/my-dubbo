package com.yuanshijia;

import com.yuanshijia.client.ProxyFactory;
import com.yuanshijia.common.api.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.create(HelloService.class);
        log.info("调用service成功 -> {}", helloService.hello("yuan"));
    }
}
