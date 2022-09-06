package com.intretech.audio.stragegy;

import com.intretech.audio.responsibility.Request;
import com.intretech.audio.utils.CommandHandle;
import com.intretech.audio.utils.ContextUtils;

/**
 * 输出到文件
 *
 * @author mark
 * @date 2022年08月31日 16:50:46
 */
public class OutPutFileCommandConsumer implements ICommandConsumer{

    @Override
    public void consumeCommand(Request request) {
        // 输出到文件
        ContextUtils.getBean(CommandHandle.class).setOutputState(1);
    }
}
