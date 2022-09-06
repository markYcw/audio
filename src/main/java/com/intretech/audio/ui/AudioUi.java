package com.intretech.audio.ui;

import com.intretech.audio.callback.CommandCallBack;
import com.intretech.audio.common.CommonConstant;
import com.intretech.audio.observer.ISubscriber;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 音频文件UI 用于展示音频数据和输入命令等
 *
 * @author mark
 * @date 2022年08月25日 13:40:32
 */
@Slf4j
@Data
public class AudioUi implements ISubscriber, Cloneable {

    /**
     * ui的专属id
     */
    private Integer id;

    /**
     * 滚动面板
     */
    private JScrollPane jScrollPaneComponent;

    /**
     * 文本域
     */
    private JTextArea area;

    /**
     * 窗口
     */
    private JFrame frame;

    /**
     * 输入文本框
     */
    private JTextField textField;

    /**
     * 命令传递接口
     */
    private CommandCallBack commandCallBack;

    public AudioUi(CommandCallBack commandCallBack) {
        this.commandCallBack = commandCallBack;
    }

    /**
     * 初始化UI相关元素以及注册观察者到发布者
     */
    public void init() {
        // 创建 JFrame 实例
        frame = new JFrame();
        // Setting the width and height of frame
        frame.setSize(600, 600);
        // 创建面板，这个类似于 HTML 的 div 标签 我们可以创建多个面板并在 JFrame 中指定位置  面板中我们可以添加文本字段，按钮及其他组件。
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);
        // 调用用户定义的方法并添加组件到面板
        placeComponents(panel, this.id);
        // 设置界面可见
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    /**
     * 将组件添加到面板
     *
     * @param panel 面板
     */
    private void placeComponents(JPanel panel, Integer id) {

        // 布局部分 这边设置布局为 null
        panel.setLayout(null);
        // 创建 JLabel
        JLabel userLabel = new JLabel("Command:");
        // 这个方法定义了组件的位置。 setBounds(x, y, width, height) x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
        userLabel.setBounds(10, 40, 110, 25);
        panel.add(userLabel);
        // 创建文本域用于用户输入命令
        JTextField userCommand = new JTextField(100);
        this.textField = userCommand;
        userCommand.setBounds(100, 40, 450, 25);
        userCommand.setText(CommonConstant.TEXT_1_AUDIO);
        userCommand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = userCommand.getText();
                // 程序启动默认选择文件路径Test1.audio
                if (command.equals(CommonConstant.TEXT_1_AUDIO) || command.equals(CommonConstant.TEXT_2_AUDIO)) {
                    commandCallBack.dealPlaybackRate(command);
                } else {
                    // 给一级音频文件处理器设置操作命令
                    commandCallBack.setCommand(id, command);
                }
            }
        });
        panel.add(userCommand);
        // 发送按钮
        JButton sendButton = new JButton("新窗口");
        sendButton.setBounds(470, 460, 80, 25);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cloneWindow();
            }
        });
        panel.add(sendButton);
        // 用于返显服务端回馈的内容
        area = new JTextArea();
        // 设置文本域不可编辑
        area.setEditable(false);
        // 设置文本域自动换行
        area.setLineWrap(true);
        // 激活断行不断字功能
        area.setWrapStyleWord(true);
        jScrollPaneComponent = new JScrollPane(area);
        jScrollPaneComponent.setBounds(30, 100, 520, 350);
        panel.add(jScrollPaneComponent);
    }

    /**
     * 克隆窗口
     */
    private void cloneWindow() {
        try {
            this.clone();
        } catch (CloneNotSupportedException e) {
            log.error("====克隆音频窗口出现异常{}", e);
        }
    }

    @Override
    protected AudioUi clone() throws CloneNotSupportedException {
        Integer id = getUniqueId(2);
        AudioUi audioUi = new AudioUi(commandCallBack);
        audioUi.setId(id);
        audioUi.init();
        audioUi.textField.setText("");
        commandCallBack.addSubscribe(id, audioUi);
        return audioUi;
    }

    @Override
    public void audioMsgToUi(String msg) {
        area.append(msg + "\n");
    }

    /**
     * 获取指定位数的ui专属id
     *
     * @return
     */
    private Integer getUniqueId(int length) {
        return (int) ((Math.random() * 9 + 1) * Math.pow(10, length - 1));
    }

}
