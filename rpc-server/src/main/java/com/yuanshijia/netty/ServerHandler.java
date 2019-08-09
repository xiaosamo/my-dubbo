package com.yuanshijia.netty;

import com.yuanshijia.common.RpcInterface;
import com.yuanshijia.common.RpcRequest;
import com.yuanshijia.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.Method;
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


    public static Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {

        log.info("server get request:{}", request);
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
        Class<?> serviceClass = Class.forName(request.getClassName());

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

        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);

        // 通过反射，调用目标service的方法，并传入参数
        return method.invoke(serviceBean, parameters);
    }

}
