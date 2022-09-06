package com.intretech.audio.common;

import lombok.Data;

import java.util.HashMap;

/**
 * 音频内容
 *
 * @author mark
 * @date 2022年09月02日 15:24:15
 */
@Data
public class AudioContent {
    /**
     * 给UI进行返显的内容池 key是UI对应的ID,value是要进行返显的内容
     */
    private HashMap<Integer,String> contentPool;

}
