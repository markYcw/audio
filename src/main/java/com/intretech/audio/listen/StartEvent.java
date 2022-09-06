package com.intretech.audio.listen;

import com.intretech.audio.observer.impl.AudioMsgHandler;
import com.intretech.audio.ui.AudioUi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * 服务初始化类：当容器完成初始化后时候初始化一些动作
 *
 * @author mark
 * @date 2022年08月25日 11:42:13
 */
@Slf4j
@Component
public class StartEvent implements CommandLineRunner {

    @Resource
    private AudioMsgHandler handler;

    @Override
    public void run(String... args) {
        AudioUi ui = new AudioUi(handler);
        // 初始化UI
        CompletableFuture.runAsync(() -> ui.init());
        // 得到两位数UI专属id
        int id = (int) ((Math.random() * 9 + 1) * Math.pow(10, 1));
        ui.setId(id);
        // 注册观察者
        handler.addSubscribe(id, ui);
    }


}
