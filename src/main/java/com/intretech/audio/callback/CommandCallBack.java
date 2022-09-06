package com.intretech.audio.callback;

import com.intretech.audio.observer.ISubscriber;

/**
 * 命令传递接口
 *
 * @author mark
 * @date 2022年09月02日 14:45:46
 */
public interface CommandCallBack {

    /**
     * 按固定播放速率读取音频文件
     *
     * @param file 音频文件
     */
    void dealPlaybackRate(String file);

    /**
     * 给第一级音频文件处理器设置操作命令
     *
     * @param id ui的唯一id
     * @param command 操作命令
     */
    void setCommand(Integer id,String command);

    /**
     * 添加观察者到发布者内部
     * @param id
     * @param subscriber
     */
    void addSubscribe(Integer id, ISubscriber subscriber);
}
