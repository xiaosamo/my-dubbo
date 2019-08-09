package com.yuanshijia.common;

import lombok.Data;
import lombok.ToString;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
@Data
@ToString
public class RpcResponse{
    /**
     * 调用编号
     */
    private String requestId;

    /**
     * 抛出的异常
     */
    private Throwable throwable;

    /**
     * 返回结果
     */
    private Object result;
}
