package com.yuanshijia.common.serialization;

import com.alibaba.fastjson.JSON;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public class JsonSerialization implements Serialization {


    public JsonSerialization(){
    }

    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T doSerialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }
}
