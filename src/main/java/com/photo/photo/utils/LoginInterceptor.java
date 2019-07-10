package com.photo.photo.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;

@Service
@EnableConfigurationProperties
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    protected static final Log logger = LogFactory.getLog(LoginInterceptor.class);

   @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

       String userId=null;
       String message = UserIdFromRequest.getUserId (request);
       switch (message)
       {
           case "error, 无session！":
           case "error, session中未能获取userId":
               break;
           default:
               userId = message;
       }
        //判断用户Name是否存在，不存在就跳转到登录界面
       if(userId==null ){
            //System.out.println("这是路径地址："+request.getRequestURI());
            response.sendRedirect("/user/login");
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        System.out.println(">>>LoginInterceptor>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println(">>>LoginInterceptor>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
    }

}
