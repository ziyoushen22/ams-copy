package com.example.amscopy.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
@Order(1)
@Configuration
@Slf4j
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response= (HttpServletResponse) servletResponse;
//        response.setContentType("text/html;charset=UTF-8");
//        response.setHeader("Access-Control-Max-Age","3600");
        //用来解决前端联调时的跨域问题
        response.setHeader("Access-Control-Allow-Origin","*");
//        response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
//        response.setHeader("Access-Control-Allow-Headers","Origin,No-Cache,X-Requested-With,If-Modified-Since,Prama,Last-Modified,Cache-Control,Expires,Content-Type,X-E4M-With,userId,token");
//        response.setHeader("Access-Control-Allow-Credentials","true");
//        response.setHeader("XDomainRequestAllowed","1");
       filterChain.doFilter(servletRequest,servletResponse);
}

    @Override
    public void destroy() {

    }
}
