package com.xdclass.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
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
        //不进行拦截
       /* if(!StringUtils.isEmpty(uri) && uri.toLowerCase().contains("order")){
            return true;
        }*/

        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext= RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        System.out.println("拦截了--"+request.getRequestURI());
        String tokenStr = "token";
        String cookieStr = "cookie";

        //进行逻辑处理
        String token = request.getHeader(tokenStr);
        System.out.println("token --"+token);
        if(StringUtils.isEmpty(token)){
             token = request.getParameter(tokenStr);
        }
        System.out.println("token --"+token);

        //进行cookie的获取
        String cookie = request.getHeader(cookieStr);
        System.out.println("cookie1 is "+cookie);
        if(StringUtils.isEmpty(cookie)){
            cookie = request.getParameter(cookieStr);
        }
        System.out.println("cookie2 is "+cookie);

        //根据token 进行登录校验逻辑的处理，根据公司的情况来自定义 JWT

        if(StringUtils.isEmpty(token)){
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }


        return null;
    }
}
