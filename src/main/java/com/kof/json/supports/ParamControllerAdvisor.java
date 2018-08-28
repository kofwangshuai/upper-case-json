package com.kof.json.supports;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.lang.reflect.Type;


@RestControllerAdvice
@Configuration
public class ParamControllerAdvisor extends RequestBodyAdviceAdapter {


    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        boolean assignableFrom = AbstractJackson2HttpMessageConverter.class.isAssignableFrom(aClass);
        boolean aaaaaaaaaaaaaaaaaaa=methodParameter.getParameterAnnotation(SupportFirstUpperChar.class) != null;
        boolean aa=  methodParameter.getParameterAnnotation(SupportFirstUpperChar.class) != null;
        return aa;
    }

    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> selectedConverterType) throws IOException {

        SupportFirstUpperChar ann = (SupportFirstUpperChar)methodParameter.getParameterAnnotation(SupportFirstUpperChar.class);
        Assert.state(ann != null, "No JsonView annotation");
        Class<?>[] classes = ann.value();
        if (!(classes.length == 2&&(classes[0].getName().equals( String.class.getName())) )) {
            throw new IllegalArgumentException("@JsonView only supported for request body advice with exactly 1 class argument: " + methodParameter);
        } else {
            MappingJacksonInputMessage mappingJacksonInputMessage = new MappingJacksonInputMessage(inputMessage.getBody(), inputMessage.getHeaders(), classes[0]);
            return mappingJacksonInputMessage;
        }
    }

    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (body instanceof String){
            String data=(String) body;
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
                Class<?> parameterType = parameter.getParameterType();
                body=newobjectMapper.readValue(data,parameterType);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return body;
        }

        return body;
    }


}