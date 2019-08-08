package com.yuanshijia.netty;

import com.yuanshiji.common.RpcRequest;
import com.yuanshiji.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {


        System.out.println("server get request" + request);
        
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            // 收到请求后开始处理请求
            Object result = handler(request);
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

//        clazz.getDeclaredClasses()[0].get;
        Object serverBean = clazz.getDeclaredConstructor().newInstance();

        String methodName = request.getMethodName();
        
        // 获取请求的参数和类型
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // 根本思路还是获取类名和方法名，利用反射实现调用
        FastClass fastClass = FastClass.create(serverBean.getClass());
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);

        // 通过反射，调用目标service的方法，并传入参数
        return fastMethod.invoke(serverBean, parameters);
    }
}
