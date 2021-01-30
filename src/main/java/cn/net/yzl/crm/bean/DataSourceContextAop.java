package cn.net.yzl.crm.bean;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author : zhangruisong
 * @date : 2020/12/8 17:58
 * @description:
 */
@Slf4j
@Aspect
@Order(value = 1)
@Component
public class DataSourceContextAop {

    @Pointcut("@annotation(cn.net.yzl.crm.bean.DataSourceSelector)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object setDynamicDataSource(ProceedingJoinPoint pjp) throws Throwable {
        boolean clear = true;
        try {
            Method method = this.getMethod(pjp);
            DataSourceSelector dataSourceImport = method.getAnnotation(DataSourceSelector.class);
            clear = dataSourceImport.clear();
            String methodName= method.getName();
            if(methodName.startsWith("get")|| methodName.startsWith("select") || methodName.startsWith("find")){
                DataSourceContextHolder.set("slave");
            }
            else{
                DataSourceContextHolder.set("master");
            }
            return pjp.proceed();
        } finally {
            if (clear) {
                DataSourceContextHolder.clear();
            }

        }
    }

    private Method getMethod(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        return signature.getMethod();
    }

}
