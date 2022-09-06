package com.intretech.audio.observer;

/**
 *发布消息接口 该接口定义添加订阅者、发送消息方法
 *
 * @author mark
 * @date 2022年08月26日 15:33:49
 */
public interface IPublisher {

    /**
     * 发布消息
     * @param id 订阅者专属id
     * @param msg 消息正文
     */
    void publishMsg(Integer id,String msg);

}
