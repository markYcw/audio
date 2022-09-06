package com.intretech.audio.callback;

import com.intretech.audio.common.AudioContent;

/**
 * 音频内容转发给UI的回调
 *
 * @author mark
 * @date 2022年09月05日 10:29:54
 */
public interface AudioUiCallBack {
    /**
     * 音频内容转发给UI
     * @param audioContent 音频内容
     */
    void ContentToHandler(AudioContent audioContent);
}
