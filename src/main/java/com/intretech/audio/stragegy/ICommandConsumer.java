package com.intretech.audio.stragegy;

import com.intretech.audio.responsibility.Request;

/**
 * 消费命令信息接口
 *
 * @author mark
 * @date 2022年08月31日 10:50:00
 */
public interface ICommandConsumer {

    /**
     * 消费命令
     * @param request
     */
    void consumeCommand(Request request);

}
