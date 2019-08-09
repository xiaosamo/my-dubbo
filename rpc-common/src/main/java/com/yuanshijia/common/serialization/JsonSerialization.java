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
        try {
            return JSON.toJSONBytes(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public <T> T doSerialize(byte[] data, Class<T> clazz) {
        try {
            return JSON.parseObject(data, clazz);
        }   catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
