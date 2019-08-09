package com.yuanshijia.client;

import com.yuanshijia.common.RpcInterface;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 */

@Configuration
@Slf4j
public class RpcConfig implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Reflections reflections = new Reflections("com.yuanshijia");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        // 获取 @RpcInterfac 标注的接口
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(RpcInterface.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            // 创建代理对象，并注册到 spring 上下文。
            beanFactory.registerSingleton(aClass.getSimpleName(),ProxyFactory.create(aClass));
        }
        log.info("afterPropertiesSet is {}",typesAnnotatedWith);

    }


}
