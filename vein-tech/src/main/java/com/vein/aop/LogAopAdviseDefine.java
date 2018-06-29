package com.vein.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


//@Component
//@Aspect
public class LogAopAdviseDefine {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	
	/*//定义匹配表达式
	 @Pointcut("execution(public * *(..))")
	public void pointcut() {
	
	}
	
	 @Before("pointcut()")
    public void logMethodInvokeParam(JoinPoint joinPoint) {
        logger.info("请求参数为：-------Before method {} invoke, param: {}---", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }*/
	
}
