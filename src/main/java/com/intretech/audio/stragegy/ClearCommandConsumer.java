package com.intretech.audio.stragegy;

import com.intretech.audio.responsibility.FirstLevelHandler;
import com.intretech.audio.responsibility.Request;
import com.intretech.audio.utils.ContextUtils;

/**
 * 清除头部、中部、尾部的追加内容
 *
 * @author mark
 * @date 2022年08月31日 11:25:41
 */
public class ClearCommandConsumer implements ICommandConsumer{

    @Override
    public void consumeCommand(Request request) {
           // 清除头部、中部、尾部的追加内容
           ContextUtils.getBean(FirstLevelHandler.class).clearCommand();
    }
}
