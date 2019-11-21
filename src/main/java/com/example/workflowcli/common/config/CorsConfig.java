package com.example.workflowcli.common.config;

import com.example.workflowcli.common.core.base.Constant;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create Time: 2019/2/4 12:22
 * Description: 服务端处理跨域请求
 */

@Configuration
public class CorsConfig {
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setOrder(0);
        registrationBean.setName("CorsFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setFilter(new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
                httpResponse.setHeader("Access-Control-Allow-Methods", Constant.REQUEST_METHODS);
                httpResponse.setHeader("Access-Control-Max-Age", "0");
                httpResponse.setHeader("Access-Control-Allow-Headers", Constant.REQUEST_HEADERS);
                httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
                httpResponse.setHeader("XDomainRequestAllowed", "1");
                chain.doFilter(request, response);
            }
            @Override
            public void destroy() {
            }
        });
        return registrationBean;
    }
}