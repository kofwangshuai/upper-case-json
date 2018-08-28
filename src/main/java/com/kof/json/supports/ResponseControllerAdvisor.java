package com.kof.json.supports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestControllerAdvice
@Configuration
public class ResponseControllerAdvisor implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body == null) {
            return null;
        }

        boolean flag=true;
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String x_support_first_upper_chars = servletRequest.getHeader("x_support_first_upper_char");
        if ("supoort".equals(x_support_first_upper_chars)){
            flag=false;
        }
        String reqFlag=servletRequest.getParameter("support");
        if (reqFlag.equals("support")){
            flag=false;
        }

        if (!flag){

            ObjectMapper newobjectMapper = new ObjectMapper();
            newobjectMapper.setPropertyNamingStrategy(new PropertyNamingStrategy() {
                private static final long serialVersionUID = 1L;

                // 反序列化时调用
                @Override
                public String nameForSetterMethod(MapperConfig<?> config,
                                                  AnnotatedMethod method, String defaultName) {
                    return method.getName().substring(3);
                }

                // 序列化时调用
                @Override
                public String nameForGetterMethod(MapperConfig<?> config,
                                                  AnnotatedMethod method, String defaultName) {
                    return method.getName().substring(3);
                }
            });


            try {
                String data=newobjectMapper.writeValueAsString(body);
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(data,Object.class);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return body;
    }
}
