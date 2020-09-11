package com.example.amscopy.configuration;

import com.alibaba.fastjson.JSONObject;
import com.example.amscopy.dto.UserLoginDto;
import com.example.amscopy.exception.AmsErrorCode;
import com.example.amscopy.exception.AmsException;
import com.example.amscopy.exception.LoginSessionTimeoutException;
import com.example.amscopy.model.LogModel;
import com.example.amscopy.service.api.RoleAuthService;
import com.example.amscopy.utils.RequestUtil;
import com.example.amscopy.utils.WebSessionUtil;
import com.example.amscopy.utils.http.RequestWrapper;
import com.example.amscopy.utils.http.ResponseWrapper;
import com.example.amscopy.utils.json.RequestJsonHandlerMethodArgumentResolver;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Value("${login.mandatory.check}")
    private boolean checkLogin;

    @Value("${login.sso.check}")
    private boolean checkSso;

    @Autowired
    private RoleAuthService roleAuthService;

    @Autowired
    private RefererProperties properties;

    @Configuration
    @ConfigurationProperties(prefix = "referer")
    @Data
    class RefererProperties {
        private List<String> refererDomain;
    }

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class); //这个属性通常不需要手动配置，高版本spring会自动检测
        return viewResolver;
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new RequestJsonHandlerMethodArgumentResolver());
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/ams").setViewName("forward:/static/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        super.addResourceHandlers(registry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
//        super.addInterceptors(registry);
        //csrf 检验拦截
        InterceptorRegistration refererInterceptor = registry.addInterceptor(new RefererInterceptor());
        refererInterceptor.addPathPatterns("/**");
        refererInterceptor.excludePathPatterns("/msg/notifyNonStdProductChanges");

        InterceptorRegistration authInterceptor = registry.addInterceptor(new AuthenticationInterceptor());
        authInterceptor.addPathPatterns("/**");
        authInterceptor.excludePathPatterns(
          "/user/getpass/*",
          "/msg/notifyNonStdProductChanges",
          "/user/login",
          "/user/captcha",
          "/user/logout",
          "/static/**",
          "/hand/**",       //测试接口预留
           "/**/*.css",
           "/**/*.js",
           "/**/*.png",
           "/**/*.jpg",
           "/**/*.jpeg",
           "/**/*.ico",
           "/**/*.html",
           "/**/*.jsp",
           "/**/*.otf",
           "/**/*.ttf",
           "/**/*.woff"
        );

    }

    class AuthenticationInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            if (checkLogin) {
                UserLoginDto loginUser = WebSessionUtil.getLoginUser(request);
                if (loginUser == null) {
                    throw new LoginSessionTimeoutException("用户未登录，请重新登录");
                }
                if (checkSso) {
                    String sessionByLoginId = WebSessionUtil.getSessionByLoginId(request, loginUser.getLoginId());
                    String sessionId = WebSessionUtil.getSessionId(request);
                    if (sessionByLoginId == null || !sessionByLoginId.equalsIgnoreCase(sessionId)) {
                        log.warn("用户已经在其他地方登录，强制下线");
                        WebSessionUtil.removeLoginUser(request);
                        throw new LoginSessionTimeoutException("用户已经在其他地方登录，强制下线");
                    }
                }
                //权限校验
                List<String> roleCodes = loginUser.getRoleCodes();
                String requestURI = request.getRequestURI();
                if (!roleAuthService.auth(roleCodes, requestURI)) {
                    throw new AmsException(AmsErrorCode.NO_PERMISSION.getCode(), AmsErrorCode.NO_PERMISSION.getDesc());
                }
            }
            return true;
        }
    }

    class RefererInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String serverName = request.getServerName();
            if (HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
                String referer = request.getHeader("referer");
                if (referer == null) {
                    throw new IllegalAccessException("不允许跨站访问");
                }
                URL url = null;
                try {
                    url = new URL(referer);
                } catch (MalformedURLException e) {
                    throw new IllegalAccessException("提交请求的站点地址解析失败，不允许访问");
                }
                if (!serverName.equalsIgnoreCase(url.getHost())) {
                    if (properties.getRefererDomain() != null) {
                        for (String s : properties.getRefererDomain()) {
                            if (s.equals(url.getHost())) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            }
            return true;
        }
    }


    @WebFilter(urlPatterns = "/*")
    @Order(0)
    @Configuration
    public static class TimeConsumingCalculationFilter1 implements Filter {
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            long startTime = 0;
            startTime = System.nanoTime();
            log.info("<<<<<<< begin request [" + request.getRequestURI() + "]...");
            try {
                filterChain.doFilter(servletRequest, servletResponse);
            } finally {
                long endTime = System.nanoTime();
                log.info(">>>>>>> completed request [" + request.getRequestURI() + "][" + (endTime - startTime) / 1000 / 1000.0 + "ms]...");
            }

        }
    }

    @WebFilter(urlPatterns = "/*")
    @Order(1)
    @Configuration
    public static class AccessLogFilter implements Filter {

        private static List<String> excludeUrlList=new ArrayList<>();
        private static List<String> streamUrlList=new ArrayList<>();
        static {
            excludeUrlList.add("/index");
            excludeUrlList.add("/word");
            excludeUrlList.add("/user/captcha");

            streamUrlList.add("/file/upload");
            streamUrlList.add("/file/productExcel");
            streamUrlList.add("/file/download");
            streamUrlList.add("/judge/upload/document");
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest httpRequest= (HttpServletRequest) request;
            String requestURI = httpRequest.getRequestURI();
            if (excludeUrlList.contains(requestURI)
                    ||requestURI.contains("swagger")
                    ||requestURI.contains("api-docs")
                    ||requestURI.contains(".zz")
                    ||requestURI.contains(".exe")
                    ||requestURI.contains(".js")
                    ||requestURI.contains(".ico")
                    ||requestURI.contains(".html")
            ){
                chain.doFilter(request,response);
                return;
            }
            String loginUserId = WebSessionUtil.getLoginUserId(httpRequest);
            if (loginUserId==null){
                chain.doFilter(request,response);
                return;
            }
            LogModel logModel = new LogModel();
            logModel.setLogId(UUID.randomUUID().toString().replace("-",""));
            logModel.setPath(requestURI);
            logModel.setRequestTime(new Date());
            logModel.setSessionId(WebSessionUtil.getSessionId(httpRequest));
            if (streamUrlList.contains(requestURI)||requestURI.contains("/pageOffice")){
                chain.doFilter(request,response);
                return;
            }else {
                RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
                ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);

                String requestData="";
                if (HttpMethod.GET.matches(httpRequest.getMethod())){
                    requestData= JSONObject.toJSONString(httpRequest.getParameterMap());
                }else {
                    requestData= RequestUtil.getBodyString(requestWrapper);
                }
                chain.doFilter(request,response);
                String encoding = response.getCharacterEncoding();
                String result = responseWrapper.getResponseData(encoding);
                response.getOutputStream().write(result.getBytes(encoding));
                logModel.setRequestData(requestData);
                logModel.setResponseData(result);
                logModel.setResponseTime(new Date());
                // todo insert
            }

        }
    }
}
