package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author TwinkleDing
 * 检查用户是否完成登录
 */
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUri = request.getRequestURI();

        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

        boolean check = checkString(urls, requestUri);
        String employee = "employee";
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getSession().getAttribute(employee) != null) {
            log.info("用户已登录，用户id为{}", request.getSession().getAttribute(employee));
            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        String notLogin = "NOTLOGIN";
        response.getWriter().write(JSON.toJSONString(Request.error(notLogin)));
    }

    /**
     * 路径匹配，是否需要放行
     *
     * @param urls       白名单
     * @param requestUri url
     * @return 是否放行
     */
    public boolean checkString(String[] urls, String requestUri) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUri);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
