package com.kof.json.config;

import com.kof.json.supports.MyRequestResponseBodyMethodProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Configuration
 public class MymvcConfig  extends WebMvcConfigurationSupport {

    @Autowired
     private RequestMappingHandlerAdapter adapter;

    @PostConstruct
    public void injectSelfMethodArgumentResolver() {
        Map<String, Object> beansWithAnnotationMap = super.getApplicationContext().getBeansWithAnnotation(RestControllerAdvice.class);
        List<Object> aaa =new ArrayList();
        for (String  key :beansWithAnnotationMap.keySet() ){
            aaa.add(beansWithAnnotationMap.get(key));
        }
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
        List<HttpMessageConverter<?>> messageConverters1111 = getMessageConverters();
        List<HttpMessageConverter<?>> messageConverters2222 = adapter.getMessageConverters();
        argumentResolvers.add(new MyRequestResponseBodyMethodProcessor( getMessageConverters(),mvcContentNegotiationManager() ,aaa));
        argumentResolvers.addAll(adapter.getArgumentResolvers());
        adapter.setArgumentResolvers(argumentResolvers);
    }


}