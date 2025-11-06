package com.abhishek.insurance_policy_cqrs.config;

import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow frontend (React dev server)
        config.addAllowedOriginPattern("http://localhost:3000");
        config.addAllowedOriginPattern("http://127.0.0.1:3000");

        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/**")
    //                     .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000")
    //                     .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
    //                     .allowedHeaders("*")
    //                     .allowCredentials(true)
    //                     .maxAge(3600);
    //         }
    //     };
    // }
}
