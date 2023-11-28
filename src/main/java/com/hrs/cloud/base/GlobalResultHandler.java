package com.hrs.cloud.base;


import com.baomidou.mybatisplus.extension.api.R;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalResultHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        final String returnTypeName = returnType.getParameterType().getName();
        return !"com.google.common.collect.RegularImmutableMap".equals(returnTypeName)
                && !"GlobalResult".equals(returnTypeName);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType
            , MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType
            , ServerHttpRequest request, ServerHttpResponse response) {
        final String returnTypeName = returnType.getParameterType().getName();
        if ("void".equals(returnTypeName)) {
            return R.ok(null);
        }
        return body;
    }



    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ Throwable.class })
    public R handleThrowable(Throwable e) {
        log.error(Throwables.getStackTraceAsString(e));
        return R.failed(e.getMessage());
    }
}
