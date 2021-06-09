package com.xdclass.apigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单限流
 * @author chengcheng123
 * @date 2021/6/9 0:35
 */
//@Component
public class OrderRateLimiterFilter extends ZuulFilter {


    //每秒创建一千个令牌
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(1000);


    @Override
    public String filterType() {
        //这个类里面记录了很多拦截器的类型，PRE表示前置
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -4;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        //如果请求的接口是订单接口的话，那么就进行拦截
        String requestURI = request.getRequestURI();

        if(!StringUtils.isEmpty(requestURI) && requestURI.toLowerCase().contains("order")){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        //进行限流的处理
        RequestContext currentContext = RequestContext.getCurrentContext();
        if(!RATE_LIMITER.tryAcquire()){
            //如果没有拿到令牌,则返回一个错误码
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
        }

        return null;
    }
}
