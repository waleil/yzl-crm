package cn.net.yzl.crm.config;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.UUIDGenerator;
import cn.net.yzl.logger.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {


    @Autowired
    private IgnoreUrlConfig ignoreUrlConfig;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {


        String spanId = UUIDGenerator.getUUID();
        QueryIds.tranceId.set (request.getHeader("traceId"));
        QueryIds.spanId.set(spanId);
        QueryIds.userNo.set (request.getHeader("userNo"));
//        QueryIds.userName.set (request.getHeader("userName"));
        String requestURI = request.getRequestURI();
        boolean isIgnore = false;
        if (!CollectionUtils.isEmpty(ignoreUrlConfig.getUrlsSet())) {
            isIgnore = ignoreUrlConfig.getUrlsSet().contains(requestURI);
        }
        if (isIgnore) {
            return true;
        }

        // 网关验证
        String gateway = request.getHeader("gateway");
        if (StringUtils.isNotBlank(gateway) && "true".equalsIgnoreCase(gateway)) {
            return true;
        }

        // 验证token
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            log.error("token为空");
            returnJson(response, ComResponse.fail(ResponseCodeEnums.TOKEN_INVALID_ERROR_CODE.getCode(),ResponseCodeEnums.TOKEN_INVALID_ERROR_CODE.getMessage()));
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        QueryIds.tranceId.remove();
        QueryIds.spanId.remove();
        QueryIds.userNo.remove();
//        QueryIds.userName.remove();
    }

    private void returnJson(HttpServletResponse response, ComResponse<Object> result) {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JacksonUtil.toJsonString(result));
        } catch (IOException e) {
            log.error("response error", e);
        }
    }
}