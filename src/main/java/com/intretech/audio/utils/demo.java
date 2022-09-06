package com.intretech.audio.utils;

/**
 * demo
 *
 * @author mark
 * @date 2022年08月31日 11:51:51
 */
public class demo {
    public static void main(String[] args) {
        int a = 35;
        int b = 2;
        System.out.println("普通取整结果"+"a/b="+(a/b));//a/b=2.0
        System.out.println("向上取整结果"+"a/b="+Math.ceil((double)a/b));//a/b=3.0
        String s = "0123456789";
        String substring = s.substring(0, 5);
        System.out.println("0-3:"+substring);
        String substring1 = s.substring(5);
        System.out.println("3-9:"+substring1);
    }
}
