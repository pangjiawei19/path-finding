package com.mt.server.pathfinding;

import com.mt.server.pathfinding.map.MapManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        MapManager.init();
        SpringApplication.run(Application.class, args);
    }

    // -- Mvc configuration ---------------------------------------------------

//    @Bean
//    WebMvcConfigurer createWebMvcConfigurer(HandlerInterceptor[] interceptors) {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addResourceHandlers(ResourceHandlerRegistry registry) {
//                registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//            }
//
//            @Override
//            public void addInterceptors(InterceptorRegistry registry) {
//                Arrays.stream(interceptors).forEach(registry::addInterceptor);
//            }
//        };
//    }

}
