package com.intretech.audio.stragegy;

import com.intretech.audio.common.CommonConstant;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 命令消费者工厂
 *
 * @author mark
 * @date 2022年08月31日 10:55:20
 */
@Component
public class CommandConsumerFactory {

    /**
     * 用于存放不同类型的命令内容消费者
     * key 是操作命令 value是操作命令所对应的消费者
     */
    private Map<String, ICommandConsumer> consumers = new HashMap<>();

    /**
     * 初始化命令消费者容器
     */
    @PostConstruct
    private void initConsumers(){
        consumers.put(CommonConstant.ADD_BEGIN_OUTPUT,new BeginCommandConsumer());
        consumers.put(CommonConstant.ADD_MIDDLE_OUTPUT,new MiddleCommandConsumer());
        consumers.put(CommonConstant.ADD_END_OUTPUT,new EndCommandConsumer());
        consumers.put(CommonConstant.CLEAR_ADD_OUTPUT,new ClearCommandConsumer());
        consumers.put(CommonConstant.OUTPUT_SCREEN,new OutPutScreenCommandConsumer());
        consumers.put(CommonConstant.OUTPUT_FILE,new OutPutFileCommandConsumer());
        consumers.put(CommonConstant.OUTPUT_SCREEN_FILE,new OutPutScreenFileCommandConsumer());
    }

    /**
     *根据命令类型得到对应的命令消费者
     * @param command 具体的命令类型
     * @return
     */
    public ICommandConsumer getConsumer(String command){
       return consumers.get(command);
    }
}
