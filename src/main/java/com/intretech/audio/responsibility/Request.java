package com.intretech.audio.responsibility;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @Description: 音频文件内容处理请求类
 * @Author: mark
 * @createTime: 2022年08月29日 17:19:27
 */
@Data
public class Request {

    /**
     * UI对应的ID集合
     */
    private ArrayList<Integer> Ids = new ArrayList<>();

    /**
     * 当前正在操作的UI对应的ID
     */
    private Integer id;

    /**
     * 读取到的音频文件内容
     */
    private String content;

    /**
     * 处理级别
     */
    private Integer level;

    /**
     * 操作命令集合
     * key是ui的专属ID，value是UI所接收到的命令集合
     */
    HashMap<Integer, LinkedList> commandList = new HashMap<>();

    /**
     * 操作内容池 key是操作命令类型,value是操作内容
     */
    private HashMap<String,String> commandPool;

    /**
     * 给UI进行返显的内容池 key是UI对应的ID,value是要进行返显的内容
     */
    private HashMap<Integer,String> contentPool = new HashMap<>();


}
