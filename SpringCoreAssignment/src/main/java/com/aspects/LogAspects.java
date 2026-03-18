package com.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
@Aspect
@Component
public class LogAspects {

        @Before("execution (* com.repository.*.*(..))")
        public void logBefore(JoinPoint joinPoint) {
            System.out.println("AOP method execution started: " + joinPoint.getSignature().getName() + "Stated at :" + LocalTime.now());
        }

        @After("execution (* com.repository.*.*(..))")
        public void logAfter(JoinPoint joinPoint) {
            System.out.println("AOP method execution Ended: " + joinPoint.getSignature().getName() + "Ended at :" + LocalTime.now());
        }


}
