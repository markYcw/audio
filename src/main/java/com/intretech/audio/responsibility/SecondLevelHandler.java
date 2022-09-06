package com.intretech.audio.responsibility;

import com.intretech.audio.common.CommonConstant;
import com.intretech.audio.common.NumberConstant;
import com.intretech.audio.observer.impl.AudioMsgHandler;
import com.intretech.audio.utils.CommandHandle;
import com.intretech.audio.utils.ContextUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 第二级别音频文件处理器
 *
 * @author mark
 * @date 2022年08月29日 18:35:04
 */
@Component
public class SecondLevelHandler extends FileContentHandler {

    @Resource
    private CommandHandle commandHandle;

    @Override
    public void handleRequest(Request request) {
        // 第二级别数据校验
        // 真正需要校验的数据
        String needCheck = request.getContent().substring(11);
        if (testIsNum(needCheck)) {
            // 如果校验不通过给每个ID所对应的UI都设置内容
            request.getIds().stream().forEach(id -> request.getContentPool().put(id, CommonConstant.ERROR));
        } else {
            // 如果校验通过给每个ID所对应的UI都设置内容
            request.getIds().stream().forEach(id -> request.getContentPool().put(id, request.getContent()));
        }
        if (request.getLevel() > NumberConstant.SECOND_LEVEL) {
            if (superior != null) {
                superior.handleRequest(request);
            }
        } else {
            // 先处理操作命令再回显
            commandHandle.dealCommand(request);
            // 处理输出文件状态
            commandHandle.dealOutputState(request, ContextUtils.getBean(AudioMsgHandler.class));
        }
    }

    // 判断字符串包含数字
    private boolean testIsNum(String str) {
        boolean flag = false;
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            flag = true;
        }
        return flag;
    }

}
