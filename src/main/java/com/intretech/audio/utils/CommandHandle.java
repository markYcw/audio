package com.intretech.audio.utils;

import com.intretech.audio.callback.AudioUiCallBack;
import com.intretech.audio.common.AudioContent;
import com.intretech.audio.common.CommonConstant;
import com.intretech.audio.responsibility.Request;
import com.intretech.audio.stragegy.CommandConsumerFactory;
import com.intretech.audio.stragegy.ICommandConsumer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * 处理操作命令类
 *
 * @author mark
 * @date 2022年08月31日 11:33:13
 */
@Slf4j
@Data
@Component
public class CommandHandle {

    /**
     * 用于存储到出书文件的音频文件内容
     */
    private static List<String> outputFile = new LinkedList<>();

    /**
     * 输出文件状态
     * 0：输出到屏幕
     * 1：输出到文件，随机文件名将音频输出到文件
     * 2：同时在屏幕和文件中输出
     */
    private Integer outputState = 0;


    /**
     * 处理操作命令
     *
     * @param request 音频文件处理请求
     */
    public void dealCommand(Request request) {
        // 如果没有操作命令则不做处理
        if (request.getCommandList().isEmpty()) {
            return;
        }
        // 如果一，二，三级审核没过则不进行下面的添加操作
        if (request.getContent().equals(CommonConstant.ERROR)) {
            return;
        }
        Iterator<Integer> ids = request.getIds().iterator();
        while (ids.hasNext()) {
            Integer id = ids.next();
            // 取出操作命令对音频文件内容进行相应操作
            LinkedList<String> commandList = request.getCommandList().get(id);
            if (!CollectionUtils.isEmpty(commandList)) {
                Iterator iterator = commandList.iterator();
                while (iterator.hasNext()) {
                    // 先设置当前正在操作的UI的ID
                    request.setId(id);
                    String command = (String) iterator.next();
                    // 此处采取简单工厂+策略模式针对七种不同命令进行消费命令
                    ICommandConsumer consumer = ContextUtils.getBean(CommandConsumerFactory.class).getConsumer(command);
                    consumer.consumeCommand(request);
                }
            }
        }
    }

    /**
     * 处理输出文件状态
     *
     * @param request 文件处理请求
     */
    public void dealOutputState(Request request, AudioUiCallBack callBack) {
        // 0：输出到屏幕 1：输出到文件，随机文件名将音频输出到文件 2：同时在屏幕和文件中输出
        Integer outputState = this.outputState;
        switch (outputState) {
            case 2:
                // 存储输出到文件的内容
                this.storeFileContent(request.getContent());
                // 把内容传递给handler
                this.contentToHandler(request, callBack);
                break;
            case 1:
                // 存储输出到文件的内容
                this.storeFileContent(request.getContent());
                break;
            default:
                // 把内容传递给handler
                this.contentToHandler(request, callBack);
                break;
        }
    }

    /**
     * 保存要输出到文件的内容
     *
     * @param fileContent
     */
    public void storeFileContent(String fileContent) {
        outputFile.add(fileContent + "\n");
    }

    /**
     * 输出到文件
     *
     * @param filePath 输出文件全路径名称
     */
    public static void outputFile(String filePath) {
        try {
            if (filePath != null) {
                File file = new File(filePath);
                FileWriter fw = new FileWriter(file);
                Iterator<String> iterator = outputFile.iterator();

                while (iterator.hasNext()) {
                    String content = iterator.next();
                    fw.write(content);
                }
                fw.close();
            }
        } catch (Exception e) {
            log.error("======输出音频到文件异常:{}", e);
        }
    }

    /**
     * 音频文件内容传递给Handler
     *
     * @param request
     */
    public void contentToHandler(Request request, AudioUiCallBack callBack) {
        AudioContent audioContent = new AudioContent();
        audioContent.setContentPool(request.getContentPool());
        callBack.ContentToHandler(audioContent);
    }

}
