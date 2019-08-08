package com.yuanshiji.common.serialization;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 * 序列化的协议，实现两个方法，序列化和反序列化
 *
 * 可选用的序列化的协议很多，比如：
 *
 * jdk 的序列化方法。（不推荐，不利于之后的跨语言调用）
 * json 可读性强，但是序列化速度慢，体积大。
 * protobuf，kyro，Hessian 等都是优秀的序列化框架，也可按需选择。
 *
 */
public interface Serialization {
    <T> byte[] serialize(T obj);

    <T> T doSerialize(byte[] data, Class<T> clazz);
}
