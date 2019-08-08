package com.yuanshiji.common.api;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 */
public interface HelloService {
    String hello(String name);

    public static void main(String[] args) {
        Class<HelloService> clzz = HelloService.class;

        Class<?>[] classes = clzz.getDeclaredClasses();
        for (Class<?> c : classes) {
//            Object serverBean = c.getDeclaredConstructor().newInstance();
            System.out.println(c);
//
        }
        System.out.println("12");
    }

}
