package com.example.workflowcli.common.core.interfaces;

import org.slf4j.Logger;

public interface LogEnable {

    /**
     * 获取日志对象
     *
     * @return slf4j
     */
    Logger getLogger();


    /**
     * 使用默认的日志对象来记录日志
     *
     * @param level 日志等级
     * @param msg   日志消息
     */
    void log(String level, String msg);

}
