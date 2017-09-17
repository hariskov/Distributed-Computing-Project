package com.dc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

@Configuration
@ComponentScan({"com.dc"})
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter{

    @Bean
    public UrlBasedViewResolver viewResolver(){
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setSuffix(".jsp");
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }

    @Bean
    public TilesViewResolver tilesViewResolver(){
        TilesViewResolver tilesViewResolver = new TilesViewResolver();
        tilesViewResolver.setViewClass(TilesView.class);

        return tilesViewResolver;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry){
        registry.viewResolver(tilesViewResolver());
    }

    @Bean
    public TilesConfigurer tilesConfigurer(){
        TilesConfigurer tc = new TilesConfigurer();
        tc.setDefinitions(new String[]{"/WEB-INF/tiles.xml","/WEB-INF/views/views.xml"});
        tc.setCheckRefresh(true);
        return tc;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // none for now , should add interceptor for calls to other servers
        // to verify whether they exist , add polling calls in here
    }

}
