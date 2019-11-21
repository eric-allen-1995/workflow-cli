package com.example.workflowcli.common.core.base;

import com.example.workflowcli.common.core.annotation.RunningLog;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LogAop {

    /**
     * LogEnable.getLogger方法的名称
     */
    private static final String LOG_ENABLE_LOGGER_GETTER_NAME = "getLogger";

    /**
     * 前置log 记录格式的后缀
     */
    private static final String LOG_SUBFIX_BEGIN = "() begin.";
    /**
     * 后置log 记录格式的后缀
     */
    private static final String LOG_SUBFIX_END = "() end.";

    /**
     * 定义切点（ 切点位置：使用RunningLog的 [ 类 or 方法 ] ）
     */
    @Pointcut(value = "@within(com.example.workflowcli.common.core.annotation.RunningLog) || @annotation(com.example.workflowcli.common.core.annotation.RunningLog)")
    public void cutService() {
    }

    /**
     * 切面
     *
     * @param point 切点
     */
    @Around("cutService()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {

        // 获取日志切点相关的信息（handle）
        LogInfo handle = null;
        try {
            handle = handle(point);
        } catch (Exception e) {
            log.error("aop日志记录出错!", e);
        }


        // 如果 AROUND或BEFORE ，则 前置log
        if (handle != null && (LogInfo.LOG_TYPE_AROUND.equals(handle.logType) || LogInfo.LOG_TYPE_BEFORE.equals(handle.logType))) {
            writeLog(handle.writer, handle.logLevel, handle.methodName + LOG_SUBFIX_BEGIN);
        }

        // 执行业务
        Object result = point.proceed();

        // 如果 AROUND或AFTER ，则 后置log
        if (handle != null && (LogInfo.LOG_TYPE_AROUND.equals(handle.logType) || LogInfo.LOG_TYPE_AFTER.equals(handle.logType))) {
            writeLog(handle.writer, handle.logLevel, handle.methodName + LOG_SUBFIX_END);
        }

        return result;
    }

    /**
     * 获取 日志切点 的相关信息
     */
    private LogInfo handle(ProceedingJoinPoint point) throws Exception {
        // 获取拦截的方法名
        Signature sig = point.getSignature();
        MethodSignature msig;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法or类");
        }
        msig = (MethodSignature) sig;

        // 获取切点目标类对象
        Object target = point.getTarget();  // 作用的目标类
        // 获取 类名
        String className = target.getClass().getName();
        // 获取 方法名
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        String methodName = currentMethod.getName();
        // 获取 方法参数
        Object[] params = point.getArgs();

        // 类 和 方法 上的RunningLog对象（可能有一个是null，视 注解位置 情况而定）
        RunningLog annotationMethod = currentMethod.getAnnotation(RunningLog.class);
        RunningLog annotationClass = target.getClass().getAnnotation(RunningLog.class);

        return new LogInfo(target, className, methodName, params, annotationClass, annotationMethod);
    }

    private void writeLog(Logger logger, List levelList, String msg) {
        if (levelList.contains(Level.TRACE)) {
            logger.trace(msg);
        }
        if (levelList.contains(Level.DEBUG)) {
            logger.debug(msg);
        }
        if (levelList.contains(Level.INFO)) {
            logger.info(msg);
        }
        if (levelList.contains(Level.WARN)) {
            logger.warn(msg);
        }
        if (levelList.contains(Level.ERROR)) {
            logger.error(msg);
        }
    }

    /**
     * 日志相关信息（LogAop.内部类）
     */
    @Data
    public static class LogInfo {

        // 类型常量 供外部调用
        /**
         * 类型常量：环绕
         */
        public static final String LOG_TYPE_AROUND = "around";
        /**
         * 类型常量：前置
         */
        public static final String LOG_TYPE_BEFORE = "before";
        /**
         * 类型常量：后置
         */
        public static final String LOG_TYPE_AFTER = "after";

        /**
         * 构造函数
         *
         * @param target             log注解 的目标类对象
         * @param className          log注解 的目标类名称
         * @param methodName         log注解 作用的方法名
         * @param params             log注解 作用的方法的参数
         * @param annotationOfClass  类 的 log注解对象
         * @param annotationOfMethod 方法 的 log注解对象
         */
        LogInfo(Object target, String className, String methodName, Object[] params, RunningLog annotationOfClass, RunningLog annotationOfMethod) {
            this.target = target;
            this.className = className;
            this.simpleClassName = className.substring(className.lastIndexOf(".") + 1);
            this.methodName = methodName;
            this.params = params;

            if (!isSingleAnnotationAndThenInitInfoBySingleAnnotation(annotationOfClass, annotationOfMethod)) {
                this.isLogEnable = annotationOfClass.logEnable() && annotationOfMethod.logEnable();
                this.logType = StringUtils.isEmpty(annotationOfMethod.type()) ? annotationOfClass.type() : annotationOfMethod.type();
                this.logLevel = mergeType(annotationOfClass, annotationOfMethod);
            }

            // writer.1、如果实现了LogEnable接口，就使用target类中的logger
            if (this.isLogEnable) {
                try {
                    //获取 LogEnable接口对象 的日志对象
                    Method getLoggerMethod = target.getClass().getMethod(LOG_ENABLE_LOGGER_GETTER_NAME);
                    writer = (Logger) getLoggerMethod.invoke(target);
                } catch (Exception e) {
                    log.warn("Running.logEnable = true， 但目标对象" + target.getClass() + " 未实现LogEnable接口");
                }
            }

            // writer.2、如果未实现了LogEnable接口，就使用LogInfo的logger
            if (StringUtils.isEmpty(writer)) {
                writer = log;
                this.methodName = className + "." + methodName;
            }

        }

        /**
         * 是否（类和方法上）只有一个RunningLog，如果是，赋值三个属性
         *
         * @param annotationOfClass  类注解对象
         * @param annotationOfMethod 方法注解对象
         * @return 是否 只有一个注解
         */
        private boolean isSingleAnnotationAndThenInitInfoBySingleAnnotation(RunningLog annotationOfClass, RunningLog annotationOfMethod) {

            if (StringUtils.isEmpty(annotationOfClass) || StringUtils.isEmpty(annotationOfMethod)) {
                RunningLog annotationSingle = StringUtils.isEmpty(annotationOfClass) ? annotationOfMethod : annotationOfClass;

                this.logLevel = Arrays.asList(annotationSingle.level());
                this.logType = annotationSingle.type();
                this.isLogEnable = annotationSingle.logEnable();

                return true;
            }
            return false;
        }

        /**
         * 合并 类和方法 上的type
         *
         * @param annotationOfClass  类注解对象
         * @param annotationOfMethod 方法注解对象
         * @return 合并后的 Type List
         */
        private List mergeType(RunningLog annotationOfClass, RunningLog annotationOfMethod) {
            List levelListClass = getAnnotationLogLevel(annotationOfClass);
            List levelListMethod = getAnnotationLogLevel(annotationOfMethod);

            levelListMethod.addAll(levelListClass);
            levelListMethod.stream().distinct().collect(Collectors.toList());

            return levelListMethod;
        }

        /**
         * 获取 Log 的 Level
         * 由于 @AliasFor注解 没有起作用，所以才定义了一个手动获取的方法
         */
        private List getAnnotationLogLevel(RunningLog annotation) {
            return getAnnotationLogLevel(Arrays.asList(annotation.value()), Arrays.asList(annotation.level()));
        }

        private List getAnnotationLogLevel(List value, List level) {
            List logLevel;
            if (level.size() == value.size()) {
                logLevel = (value.size()) == 1 && Level.INFO.equals(value.get(0)) ? level : level;
            } else {
                logLevel = (level.size() > value.size()) ? level : value;
            }
            return logLevel;
        }

        // 日志 记录 相关信息
        private Object target;
        private String className;
        private String simpleClassName;
        private String methodName;
        private Object[] params;

        // 日志 注解 相关信息
        private List logLevel;
        private String logType;
        private boolean isLogEnable;

        // 日志 记录 对象
        private Logger writer;

    }

}
