package com.intretech.audio.responsibility;

import com.intretech.audio.common.CommonConstant;
import com.intretech.audio.observer.ISubscriber;
import com.intretech.audio.observer.impl.AudioMsgHandler;
import com.intretech.audio.utils.CommandHandle;
import com.intretech.audio.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 第三级别音频文件处理器
 *
 * @author mark
 * @date 2022年08月29日 18:44:55
 */
@Component
public class ThreeLevelHandler extends FileContentHandler {

    @Autowired
    private CommandHandle commandHandle;

    /**
     * 观察者集合
     */
    private ArrayList<ISubscriber> observers = new ArrayList();

    @Override
    public void handleRequest(Request request) {
        // 第三级别校验
        // 真正需要校验的数据
        String needCheck = request.getContent().substring(11);
        if (specialCharacters(needCheck)) {
            request.setContent(CommonConstant.ERROR);
        }
        // 先处理操作命令再回显
        commandHandle.dealCommand(request);
        // 处理输出文件状态
        commandHandle.dealOutputState(request, ContextUtils.getBean(AudioMsgHandler.class));
    }

    // 判断字符串包含特殊字符
    private boolean specialCharacters(String str) {
        boolean flag = false;
        Pattern pattern = Pattern.compile("[@!#]");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            flag = true;
        }
        return flag;
    }
}
