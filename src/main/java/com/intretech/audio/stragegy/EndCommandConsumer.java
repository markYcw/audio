package com.intretech.audio.stragegy;

import com.intretech.audio.common.CommonConstant;
import com.intretech.audio.responsibility.Request;

/**
 * 尾部添加命令消费者
 *
 * @author mark
 * @date 2022年08月31日 11:01:30
 */
public class EndCommandConsumer implements ICommandConsumer {

    @Override
    public void consumeCommand(Request request) {
        // 先得到要添加的内容再进行相应操作
        String temContent = request.getCommandPool().get(request.getId() + CommonConstant.ADD_END_OUTPUT);
        //设置要返显的内容
        request.getContentPool().put(request.getId(), request.getContent() + temContent);
    }
}
