package com.intretech.audio.stragegy;

import com.intretech.audio.responsibility.Request;
import com.intretech.audio.utils.CommandHandle;
import com.intretech.audio.utils.ContextUtils;

/**
 * 输出到屏幕
 *
 * @author mark
 * @date 2022年08月31日 16:43:16
 */

public class OutPutScreenCommandConsumer implements ICommandConsumer{

    @Override
    public void consumeCommand(Request request) {
           // 输出到屏幕
        ContextUtils.getBean(CommandHandle.class).setOutputState(0);
    }
}
