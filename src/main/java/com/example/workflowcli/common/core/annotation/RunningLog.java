package com.example.workflowcli.common.core.annotation;

import com.example.workflowcli.common.core.base.LogAop;
import org.slf4j.event.Level;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RunningLog {

    @AliasFor("level")
    Level[] value() default Level.INFO;

    /**
     * 日志级别
     */
    @AliasFor("value")
    Level[] level() default Level.INFO;

    /**
     * 环绕类型：before / after / around
     */
    String type() default LogAop.LogInfo.LOG_TYPE_AROUND;

    /**
     * 是否继承 logEnable 接口
     * {true：使用类必须实现 LogEnable接口}
     */
    boolean logEnable() default true;

}
