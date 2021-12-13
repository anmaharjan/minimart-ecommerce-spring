package edu.miu.waa.minimartecommerce.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public ModelMapper modelMapper(){return new ModelMapper();}

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imagePath = "file:" + System.getProperty("user.dir") + File.separator + "images/";
        registry.addResourceHandler("/images/**")
                .addResourceLocations(imagePath)
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.DAYS).cachePublic());
    }
}
