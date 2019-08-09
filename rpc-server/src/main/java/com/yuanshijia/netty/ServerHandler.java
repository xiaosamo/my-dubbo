package com.yuanshijia.netty;

import com.yuanshijia.common.RpcInterface;
import com.yuanshijia.common.RpcRequest;
import com.yuanshijia.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest>{

    private ApplicationContext applicationContext;

    public static Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {

        System.out.println("server get request" + request);
        
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            // 收到请求后开始处理请求
            Object result = handler(request);
            log.info("service 调用成功:{}", result);
            response.setResult(result);
        } catch (Throwable throwable) {
            //  如果有异常，设置到response中
            response.setThrowable(throwable);
            throwable.printStackTrace();
        }
        ctx.writeAndFlush(response);
    }

    /**
     * 通过反射，调用目标service的方法
     * @param request
     * @return
     * @throws Throwable
     */
    private Object handler(RpcRequest request) throws Throwable {
        // 获取要调用的service
        Class<?> clazz = Class.forName(request.getClassName());

//        ServiceLoader<?> loader = ServiceLoader.load(clazz);
//        Iterator<?> iterator = loader.iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//        }
//        clazz.getDeclaredClasses()[0].get;

        
//        Object serverBean = clazz.getDeclaredConstructor().newInstance();
//        Object serviceBean = applicationContext.getBean(clazz);

        Reflections reflections = new Reflections("com.yuanshijia");
        // 获取 @RpcInterfac 标注的接口
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(RpcInterface.class);
        for (Class<?> c : classSet) {
            ServerHandler.serviceMap.put(request.getClassName(), c.getDeclaredConstructor().newInstance());
        }
        // 获取service
        Object serviceBean = serviceMap.get(request.getClassName());

        String methodName = request.getMethodName();
        
        // 获取请求的参数和类型
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        FastClass fastClass = FastClass.create(serviceBean.getClass());
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);


        // 根本思路还是获取类名和方法名，利用反射实现调用
//        FastClass fastClass = FastClass.create(serviceBean.getClass());
//        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);

        // 通过反射，调用目标service的方法，并传入参数
        return fastMethod.invoke(serviceBean, parameters);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(">>>>>>>>>>> child-rpc provider netty server caught exception", cause);
        ctx.close();
    }
}
