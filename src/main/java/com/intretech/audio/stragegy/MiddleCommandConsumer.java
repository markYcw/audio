package com.intretech.audio.stragegy;

import com.intretech.audio.common.CommonConstant;
import com.intretech.audio.responsibility.Request;

/**
 * 中部添加命令消费者
 *
 * @author mark
 * @date 2022年08月31日 11:02:27
 */
public class MiddleCommandConsumer implements ICommandConsumer {

    @Override
    public void consumeCommand(Request request) {
        // 先得到要添加的内容再进行相应操作
        String temContent = request.getCommandPool().get(request.getId() + CommonConstant.ADD_MIDDLE_OUTPUT);
        // 得到插入的索引
        String content = request.getContent();
        Integer index = content.length() / 2;
        // 根据索引分割得到前半部分内容
        String startContent = content.substring(0, index);
        // 根据索引分割得到后半部分内容
        String endContent = content.substring(index);
        // 全部内容 = 前半部分内容 + 后半部分内容
        String totalContent = startContent + temContent + endContent;
        //设置要返显的内容
        request.getContentPool().put(request.getId(), totalContent);
    }
}
