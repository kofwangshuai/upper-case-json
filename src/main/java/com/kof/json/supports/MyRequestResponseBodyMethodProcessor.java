package com.kof.json.supports;

import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


public class MyRequestResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor {

    public MyRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    public MyRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager contentNegotiationManager) {
        super(converters, contentNegotiationManager);
    }


    public MyRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
        super(converters, manager, requestResponseBodyAdvice);
    }

    public MyRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
                                              @Nullable List<Object> requestResponseBodyAdvice) {

        super(converters, (ContentNegotiationManager)null, requestResponseBodyAdvice);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
//        return  (  parameter.hasParameterAnnotation(RequestBody.class)&&parameter.hasParameterAnnotation(SupportFirstUpperChar.class));
        return  (  parameter.hasParameterAnnotation(RequestBody.class));
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

        boolean flag=true;

        HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
        String x_support_first_upper_chars = servletRequest.getHeader("x_support_first_upper_char");
        if ("supoort".equals(x_support_first_upper_chars)){
            flag=false;
        }
        String reqFlag=servletRequest.getParameter("support");
        if (reqFlag.equals("support")){
            flag=false;
        }


        Object arg = null;
        String name=null;
        //if (flag&&parameter.hasParameterAnnotation(RequestBody.class) && (!parameter.hasParameterAnnotation(SupportFirstUpperChar.class)))
        if (flag) {
            /** advisor切面作用在这个方法上的  ：**/
            arg = this.readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
            name = Conventions.getVariableNameForParameter(parameter);
        } else {
            arg = this.readWithMessageConverters(webRequest, parameter, String.class);
            name = Conventions.getVariableNameForParameter(parameter);
        }

        if (binderFactory != null) {
            WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
            if (arg != null) {
                this.validateIfApplicable(binder, parameter);
                if (binder.getBindingResult().hasErrors() && this.isBindExceptionRequired(binder, parameter)) {
                    throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
                }
            }

            if (mavContainer != null) {
                mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
            }
        }

        return this.adaptArgumentIfNecessary(arg, parameter);

    }



    public boolean supportsReturnType(MethodParameter returnType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) || returnType.hasMethodAnnotation(ResponseBody.class);
    }


    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter, Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
        Object arg = this.readWithMessageConverters(inputMessage, parameter, paramType);
        if (arg == null && this.checkRequired(parameter)) {
            throw new HttpMessageNotReadableException("Required request body is missing: " + parameter.getExecutable().toGenericString());
        } else {
            return arg;
        }
    }

    protected boolean checkRequired(MethodParameter parameter) {
        RequestBody requestBody = (RequestBody)parameter.getParameterAnnotation(RequestBody.class);
        return requestBody != null && requestBody.required() && !parameter.isOptional();
    }

    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        mavContainer.setRequestHandled(true);
        ServletServerHttpRequest inputMessage = this.createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = this.createOutputMessage(webRequest);
        this.writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
    }
}
