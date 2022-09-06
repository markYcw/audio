package com.intretech.audio.observer;

/**
 * 定义订阅者接口，接口定义收到消息监听方法，所有的订阅者都实现该接口
 *
 * @author: mark
 * @date 2022年08月26日 15:19:26
 */
public interface ISubscriber {

    /**
     * 给UI进行信息返显
     * @param msg 音频文件的信息
     */
    abstract public void audioMsgToUi(String msg);
}
