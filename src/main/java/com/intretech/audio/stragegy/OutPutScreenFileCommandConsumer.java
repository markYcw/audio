package com.intretech.audio.stragegy;

import com.intretech.audio.responsibility.Request;
import com.intretech.audio.utils.CommandHandle;
import com.intretech.audio.utils.ContextUtils;


/**
 * 同时在屏幕和文件中输出
 *
 * @author mark
 * @date 2022年08月31日 16:56:26
 */
public class OutPutScreenFileCommandConsumer implements ICommandConsumer{

    @Override
    public void consumeCommand(Request request) {
        //输出到屏幕和文件
        ContextUtils.getBean(CommandHandle.class).setOutputState(2);
    }
}
