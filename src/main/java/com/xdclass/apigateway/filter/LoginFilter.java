package com.xdclass.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chengcheng123
 * @date 2021/6/7 0:20
 */

@Component
public class LoginFilter extends ZuulFilter {

    @Override
    public String filterType() {
        //这个类里面记录了很多拦截器的类型，PRE表示前置
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String uri = request.getRequestURI();
        System.out.println("uri--:::"+uri);

        if(!StringUtils.isEmpty(uri) && uri.toLowerCase().contains("order")){
            return true;
        }

        return false;
    }

    @Override
    public Object run() throws ZuulException {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        System.out.println("拦截了--"+request.getRequestURI());
        return null;
    }
}
