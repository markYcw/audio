package com.intretech.audio.responsibility;

import com.intretech.audio.common.CommonConstant;
import com.intretech.audio.common.NumberConstant;
import com.intretech.audio.observer.impl.AudioMsgHandler;
import com.intretech.audio.utils.CommandHandle;
import com.intretech.audio.utils.ContextUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 第一级别音频文件处理器
 *
 * @author mark
 * date 2022年08月29日 18:33:42
 */
@Component
@Data
@Slf4j
public class FirstLevelHandler extends FileContentHandler {

    /**
     * 新创建的处理音频文件内容任务的id集合
     */
    private ArrayList<Integer> ids = new ArrayList<>();

    /**
     * 操作命令集合
     * key是ui的专属ID，value是UI所接收到的命令集合
     */
    HashMap<Integer, LinkedList> commandList = new HashMap<>();

    /**
     * 操作内容池 key是操作命令类型,value是操作内容
     */
    HashMap<String, String> commandPool = new HashMap<>();

    @Autowired
    private CommandHandle commandHandle;

    /**
     * 清除过期的容器内容
     */
    public void clearCommand() {
        commandList.clear();
        commandPool.clear();
    }

    /**
     * 新增处理音频文件内容任务id
     */
    public Integer createTask() {
        //得到一个两位数的id
        Integer id = getUniqueId(2);
        this.ids.add(id);
        return id;
    }

    /**
     * 获取指定位数的ui专属id
     *
     * @return
     */
    private Integer getUniqueId(int length) {
        return (int) ((Math.random() * 9 + 1) * Math.pow(10, length - 1));
    }

    @Override
    public void handleRequest(Request request) {
        // 先把ID加入到request的ID集合
        if (!CollectionUtils.isEmpty(ids)) {
            ids.stream().forEach(a -> request.getIds().add(a));
        }
        // 先设置操作命令和操作内容
        request.setCommandList(commandList);
        request.setCommandPool(commandPool);
        // 第一级别数据校验
        // 获取需要校验的数据
        String content = request.getContent();
        String needCheck = content.substring(11);
        if (needCheck.contains("_")) {
            // 如果校验不通过给每个ID所对应的UI都设置校验不通过内容
            request.getIds().stream().forEach(id -> request.getContentPool().put(id, CommonConstant.ERROR));
        } else {
            // 如果校验通过给每个ID所对应的UI都设置内容
            request.getIds().stream().forEach(id -> request.getContentPool().put(id, request.getContent()));
        }
        if (request.getLevel() > NumberConstant.FIRST_LEVEL) {
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


}
