package com.intretech.audio.responsibility;

/**
 * 音频文件内容处理器超类
 *
 * @author mark
 * @date 2022年08月29日 17:13:18
 */
public abstract class FileContentHandler {

    /**
     * 当前处理器的上级
     */
    protected FileContentHandler superior;

    /**
     * 设置上级音频文件内容处理器
     *
     * @param superior 上级音频文件内容处理器
     */
    public void setSuperior(FileContentHandler superior) {
        this.superior = superior;
    }

    /**
     * 处理音频文件请求
     *
     * @param request 具体的音频处理请求
     */
    abstract public void handleRequest(Request request);
}
