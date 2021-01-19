package cn.net.yzl.crm.annotations;

import cn.net.yzl.crm.filters.HttpRequestTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterHandle {
    HttpRequestTypeEnum httpRequestType() default HttpRequestTypeEnum.GET;
}
