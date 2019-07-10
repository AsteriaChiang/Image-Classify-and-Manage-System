package com.photo.photo.config;

import com.photo.photo.utils.LoginInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc //这个注解使得默认配置失效
public class DefineConfig extends WebMvcConfigurerAdapter {

    private static final Log logger = LogFactory.getLog(DefineConfig.class);

    //这里是实现用户拦截的具体操作，类LoginInterceptor是用户登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
         *  多个拦截器组成一个拦截器链
         *  excludePathPatterns 用户排除拦截
         *  addPathPatterns 用于添加拦截规则
         * */
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/user/login","/user/register","/user/email");//多个排除拦截的话，只需要用逗号隔开即可
        //这边还可以加好几个拦截器组成拦截器链
        //super.addInterceptors(registry);
    }

//    //可以方便的将一个请求映射成视图，无需书写控制器，addViewCOntroller("请求路径").setViewName("请求页面文件路径")
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/user/login").setViewName("login");
//    }
//
//    //自定义资源拦截路径可以和springBoot默认的资源拦截一起使用，但是我们如果自己定义的路径与默认的拦截重复，那么我们该方法定义的就会覆盖默认配置
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //这里也可以采用ResourceUtils.CLASSPATH_URL_PREFIX 它是：classpath:
//        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/WEB-INF/resources/templates/");
//        super.addResourceHandlers(registry);
//    }
}
