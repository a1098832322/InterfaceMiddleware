package com.zl.middleware.core.config;

import com.zl.middleware.core.component.*;
import com.zl.middleware.core.utils.HttpRequestUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统组件配置
 *
 * @author zl
 * @since 2021/11/18 10:42
 */
@Configuration
public class SystemComponentConfig implements WebMvcConfigurer {
    /**
     * 因为在所有的 HttpMessageConverter 实例集合中，StringHttpMessageConverter 要比其它的 Converter 排得靠前一些。
     * 我们需要将处理 Object 类型的 HttpMessageConverter 放得靠前一些。
     * 防止方法返回类型为String时，通过{@link DataResponseBodyAdvice}中强转后，再次按String进行序列化出错.
     *
     * @param converters converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new MappingJackson2HttpMessageConverter());
    }

    /**
     * restTemplate配置
     *
     * @return restTemplate
     */
    @Bean
    public RestTemplate flowProcessEngineRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        //设置字符串解码器为UTF-8
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    /**
     * http请求工具
     *
     * @param restTemplate restTemplate
     * @return http请求工具
     */
    @Bean
    public HttpRequestUtil httpRequestUtil(RestTemplate restTemplate) {
        return new HttpRequestUtil(restTemplate);
    }

    /**
     * 单条接口数据直接发送器
     *
     * @return 单条接口数据直接发送器
     */
    @Bean
    @Scope("prototype")
    public DirectSingleMessageSender directSingleMessageSender() {
        return new DirectSingleMessageSender();
    }

    /**
     * 多条接口数据直接发送器
     *
     * @return 多条接口数据直接发送器
     */
    @Bean
    @Scope("prototype")
    public DirectArrayMessageSender directArrayMessageSender() {
        return new DirectArrayMessageSender();
    }

    /**
     * form表单参数渲染器
     *
     * @return form表单参数渲染器
     */
    @Bean
    public ParameterRender parameterRender() {
        return new ParameterRender();
    }

    /**
     * json數據渲染器
     *
     * @return json數據渲染器
     */
    @Bean
    public JsonRender jsonRender() {
        return new JsonRender();
    }

    /**
     * header数据渲染器
     *
     * @return header数据渲染器
     */
    @Bean
    public HeaderRender headerRender() {
        return new HeaderRender();
    }

    /**
     * MQ消息接收器
     *
     * @return MQ消息接收器
     */
    @Bean
    public MessageMiddlewareMQReceiver mqReceiver() {
        return new MessageMiddlewareMQReceiver();
    }
}
