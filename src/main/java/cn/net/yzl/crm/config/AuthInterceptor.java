package cn.net.yzl.crm.config;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.UUIDGenerator;
//import cn.net.yzl.dmc.remote.OauthRemoteService;
import cn.net.yzl.logger.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
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
    private NacosValue nacosValue;

    @Autowired
    private IgnoreUrlConfig ignoreUrlConfig;

//    @Autowired
//    private OauthRemoteService oauthRemoteService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

//        String serverGatewayUrl = nacosValue.getServerGatewayUrl();
//        if (!"noCheck".equalsIgnoreCase(serverGatewayUrl)) {
//            String proto = request.getHeader("x-forwarded-proto");
//            String host = request.getHeader("x-forwarded-host");
//            String forwardedUrl = proto + "://" + host;
//            if (StringUtils.isBlank(host) || !forwardedUrl.equalsIgnoreCase(serverGatewayUrl)) {
//                log.error("host:[{}],非法请求！", host);
//                returnJson(response, ComResponse.fail(ResponseCodeEnums.AUTHOR_ERROR_CODE));
//                return false;
//            }
//        }

        String spanId = UUIDGenerator.getUUID();
        QueryIds.tranceId.set (request.getHeader("traceId"));
        QueryIds.spanId.set(spanId);
        QueryIds.userNo.set (request.getHeader("userNo"));
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
            returnJson(response, ComResponse.fail(ResponseCodeEnums.TOKEN_INVALID_ERROR_CODE));
            return false;
        }

//        var generalResult = oauthRemoteService.verifyToken(token);
//        if (HttpStatus.OK.value() != generalResult.getCode()) {
//            log.error("token:[{}],校验失败:[{}]", token, generalResult.getMessage());
//            returnJson(response, ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE));
//            return false;
//        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        QueryIds.tranceId.remove();
        QueryIds.spanId.remove();
        QueryIds.userNo.remove();
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