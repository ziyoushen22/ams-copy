package com.example.amscopy.utils.json;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.nio.charset.Charset;

public class RequestJsonHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private Charset defaultCharset = Charset.forName("UTF-8");

    public Charset getContentCharset(MediaType mediaType) {
        if (mediaType != null && mediaType.getCharset() != null) {
            return mediaType.getCharset();
        } else {
            return this.defaultCharset;
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestJson.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        RequestJson requestJson = parameter.getParameterAnnotation(RequestJson.class);
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String body = (String) request.getAttribute("~prevRemarkedBodyString");
        if (body == null) {
            BufferedReader reader = request.getReader();
            StringBuilder builder = new StringBuilder();
            char[] buf = new char[1024];
            int rd;
            while ((rd = reader.read(buf)) != -1) {
                builder.append(buf, 0, rd);
            }
            body = builder.toString();
            request.setAttribute("~prevRemarkedBodyString", body);
        }
        JSONObject jsonObject = JSONObject.parseObject(body);
        String parameterName = parameter.getParameterName();
        if (StringUtils.isNotEmpty(requestJson.value())) {
            parameterName = requestJson.value();
        }
        return jsonObject == null ? null : jsonObject.get(parameterName);
    }
}
