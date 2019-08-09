package com.yuanshijia.client;

import com.yuanshijia.common.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class RpcInvoker<T> implements InvocationHandler {
    private Class<T> clazz;
    public RpcInvoker(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     *  invoke 方法，主要三个作用
     *
     * 1.生成 RequestId。
     * 2.拼装 RpcRequest。
     * 3.调用 Transports 发送请求，获取结果。
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();

        String requestId = UUID.randomUUID().toString().replace("-", "");
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();

        // 如果 toString、hashCode 和 equals 等方法被子类重写了，这里也直接调用
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return clazz.toString();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return clazz.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return clazz.equals(args[0]);
        }


        request.setRequestId(requestId);
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameterTypes(parameterTypes);
        request.setParameters(args);

        // 调用 Transports 发送请求，获取结果
        return Transporters.send(request).getResult();
    }
}
