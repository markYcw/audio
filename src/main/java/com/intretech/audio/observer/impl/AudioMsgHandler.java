package com.intretech.audio.observer.impl;

import com.intretech.audio.callback.AudioUiCallBack;
import com.intretech.audio.callback.CommandCallBack;
import com.intretech.audio.common.AudioContent;
import com.intretech.audio.common.CommonConstant;
import com.intretech.audio.observer.IPublisher;
import com.intretech.audio.observer.ISubscriber;
import com.intretech.audio.responsibility.FirstLevelHandler;
import com.intretech.audio.responsibility.Request;
import com.intretech.audio.responsibility.SecondLevelHandler;
import com.intretech.audio.responsibility.ThreeLevelHandler;
import com.intretech.audio.utils.CommandHandle;
import com.intretech.audio.utils.ContextUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * 音频信息处理类
 *
 * @author mark
 * date 2022年08月26日 15:38:51
 */
@Slf4j
@Component
@Data
public class AudioMsgHandler implements IPublisher, CommandCallBack, AudioUiCallBack {

    @Resource
    private FirstLevelHandler firstLevelHandler;

    @Resource
    private SecondLevelHandler secondLevelHandler;

    @Resource
    private ThreeLevelHandler threeLevelHandler;

    /**
     * 观察者容器
     * key 是观察者id ，value是观察者
     */
    private HashMap<Integer, ISubscriber> observers = new HashMap<>();

    /**
     * UI所对应的id以及M层处理任务id映射容器
     * key 是UI的专属id ，value是M层处理任务id
     */
    private HashMap<Integer, Integer> idPool = new HashMap<>();

    /**
     * 音频文件路径
     */
    private static final String PATH = "src./";

    /**
     * 输出命令第一个单词
     */
    private static final String CHANGE = "change";


    /**
     * 初始化各级音频处理器的上级音频处理器
     */
    @PostConstruct
    private void initFileHandlerSuperior() {
        firstLevelHandler.setSuperior(secondLevelHandler);
        secondLevelHandler.setSuperior(threeLevelHandler);
    }

    /**
     * 给第一级音频文件处理器设置操作命令
     *
     * @param command 操作命令
     */
    @Override
    public void setCommand(Integer id, String command) {
        // 操作命令池 key是命令操作类型，value是操作命令内容
        HashMap<String, String> commandPool = new HashMap<>();
        /*
         * 操作命令集合
         * key是M层的专属ID，value是M层要处理的所接收到的命令集合
         */
        HashMap<Integer, LinkedList> commandList = new HashMap<>();
        // 操作命令集合，操作命令顺序由他维护
        LinkedList<String> commands = new LinkedList<>();
        // 输出到屏幕和文件等输出命令比较特殊在第一行进行判断特殊处理
        if (command.substring(2, 8).equals(CHANGE)) {
            commands.add(command);
            commandList.put(idPool.get(id), commands);
            // 把操作命令加入音频文件处理器
            firstLevelHandler.setCommandList(commandList);
            return;
        }
        // 分割命令字符串得到命令字符串数组
        String[] array = command.split("--"); //以 -- 分割
        for (String s : array) {
            // 字符串数字array第一个字符是空字符串要特殊处理
            if (s.equals("")) {
                continue;
            }
            // 将操作内容和操作命令用空格分开得到操作命令
            String[] split = s.split(" ");
            // 现在数组里面只有两个元素第一个是操作命令，第二个是操作内容
            // 取出操作命令判断是否重复，如重复删除之前的
            String temCommand = split[0];
            if (commands.contains(temCommand)) {
                commands.remove(temCommand);
                commandPool.remove(temCommand);
            }
            // 把命令加入容器
            commands.add(temCommand);
            // 只有添加操作split.length才会大于1可以把操作命令和操作内容存储进容器，而--clear-add-output清除操作长度等于1
            if (split.length > 1) {
                // 取出操作内容把操作内容存入容器
                commandPool.put(idPool.get(id) + temCommand, split[1]);
            }
        }
        // 把命令加入音频文件处理器之前先把之前容器里的内容清空
        firstLevelHandler.clearCommand();
        // 把操作命令加入音频文件处理器
        commandList.put(idPool.get(id), commands);
        firstLevelHandler.setCommandList(commandList);
        // 把操作内容加入音频文件处理器
        firstLevelHandler.setCommandPool(commandPool);
    }

    /**
     * 按固定播放速率读取音频文件
     *
     * @param file 音频文件
     */
    @Override
    public void dealPlaybackRate(String file) {
        FileInputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            // BufferedReader是可以按行读取文件
            inputStream = new FileInputStream(PATH + file);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            str = bufferedReader.readLine();
            String content = str.replaceAll("\\s+", "");
            // 得到播放速率
            double playbackRate = Double.parseDouble(content.substring(3));
            ReadWorker worker = new ReadWorker();
            // 开启读取音频文件工作线程
            worker.startWorker(PATH + file, playbackRate);

        } catch (Exception e) {
            log.error("读取音频文件播放速率发生异常{}", e);
        } finally {
            // Todo..
            // close
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addSubscribe(Integer id, ISubscriber subscriber) {
        observers.put(id, subscriber);
        Integer taskId = firstLevelHandler.createTask();
        idPool.put(id, taskId);
    }

    @Override
    public void publishMsg(Integer id, String msg) {
        observers.get(id).audioMsgToUi(msg);
    }

    @Override
    public void ContentToHandler(AudioContent audioContent) {
        // 遍历UI内容池，根据UI的唯一ID找到容器里存储对应UI进行内容返显
        Iterator<Map.Entry<Integer, String>> iterator = audioContent.getContentPool().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            publishMsg(getUid(entry.getKey()), entry.getValue());
        }
    }

    /**
     * 根据M层专属id找到UI的专属id
     *
     * @param id
     * @return
     */
    private Integer getUid(Integer id) {
        Iterator<Map.Entry<Integer, Integer>> iterator = idPool.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            if (entry.getValue() == id) {
                return entry.getKey();
            }
        }
        return null;
    }

}

@Slf4j
@Data
class ReadWorker implements Runnable {

    /**
     * 线程是否启动
     */
    private volatile boolean start = false;

    /**
     * 文件名称
     */
    private String file;

    /**
     * 音频播放速率
     */
    private long playbackRate;

    /**
     * 开启读取音频文件工作线程
     *
     * @param file         音频文件
     * @param playbackRate 播放速率
     */
    public void startWorker(String file, double playbackRate) {
        if (!start) {
            this.file = file;
            this.playbackRate = new Double(playbackRate * 1000).longValue();
            new Thread(this).start();
            start = true;
        }
    }

    @Override
    public void run() {
        try {
            // BufferedReader是可以按行读取文件
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            Integer startIndex = 0;
            while ((str = bufferedReader.readLine()) != null) {
                String audioType = str.substring(0, 3);
                if (audioType.equals(CommonConstant.MP3)) {
                    startIndex = 2;
                } else if (audioType.equals(CommonConstant.MP4)) {
                    startIndex = 1;
                } else {
                    // 获取校验级别
                    Integer level = Integer.valueOf(str.substring(startIndex - 1, startIndex));
                    Request request = new Request();
                    // 设置校验内容
                    request.setContent(str.substring(startIndex));
                    // 设置校验级别
                    request.setLevel(level);
                    // 进行音频文件内容处理
                    ContextUtils.getBean(FirstLevelHandler.class).handleRequest(request);
                    // 按播放速率读取
                    Thread.sleep(playbackRate);
                }
            }
            // 定义输出文件名
            String fileName = UUID.randomUUID() + "file.audio";
            String filePath = "D:\\code\\file\\" + fileName;
            // 输出内容到文件
            CommandHandle.outputFile(filePath);
            // close
            inputStream.close();
            bufferedReader.close();
        } catch (Exception e) {
            log.error("读取文件发生异常{}", e);
        }
    }
}
