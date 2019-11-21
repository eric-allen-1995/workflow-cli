package com.example.workflowcli.common.core.base;

import com.example.workflowcli.common.core.interfaces.LogEnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class BaseLogEnable implements LogEnable {

    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(BaseLogEnable.class);

    /**
     * 日志对象-字段的变量名
     */
    private static final String LOGGER_FIELD_NAME = "log";

    @Override
    public Logger getLogger() {
        Logger log = null;

        try {
            Field field = this.getClass().getDeclaredField(LOGGER_FIELD_NAME);
            field.setAccessible(true);
            log = (Logger) field.get(this);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null == log ? this.log : log;
    }

    @Override
    public void log(String level, String msg) {
        Logger log = this.getLogger();
        switch (level) {
            case "debug":
                log.debug(msg);
                break;

            case "warn":
                log.warn(msg);
                break;
            case "error":
                log.error(msg);
                break;
            case "info":
            default:
                log.info(msg);
        }
    }
}
