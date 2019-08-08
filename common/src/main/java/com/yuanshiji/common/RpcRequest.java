package com.yuanshiji.common;

import lombok.Data;
import lombok.ToString;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 * 方法调用对象实体
 */
@Data
@ToString
public class RpcRequest {
    /**
     * 调用编号
     */
    private String requestId;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 调用参数的数据类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 请求的参数
     */
    private Object[] parameters;

}
