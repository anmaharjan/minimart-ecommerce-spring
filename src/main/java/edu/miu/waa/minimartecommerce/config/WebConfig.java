package edu.miu.waa.minimartecommerce.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcConfigurerAdapter {
    @Bean
    public ModelMapper modelMapper(){return new ModelMapper();}

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
                        .allowCredentials(true);
            }
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imagePath = "file:" + System.getProperty("user.dir") + File.separator + "images/";
        registry.addResourceHandler("/images/**")
                .addResourceLocations(imagePath)
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.DAYS).cachePublic());
    }
}
