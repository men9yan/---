package com.wukong.logisticsproject.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;


/**
 * @description: 验证码配置
 * @author: Lxq
 * @date: 2020/1/19 11:30
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer(){
        Properties properties = new Properties();
        // 边框
        properties.put("kaptcha.border", "no");
        // 字体的颜色
        properties.put("kaptcha.textproducer.font.color", "black");
        // 字符间距
        properties.put("kaptcha.textproducer.char.space", "5");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
